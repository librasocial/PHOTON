const ApiError = require("../errors/handler");
const { assertNotNull } = require("../helpers/helper");
const { update } = require("../models/health-ids");
const HealthIdModel = require("../models/health-ids");
const appLogger = require("../../config/logger.js");
const fetch = require("node-fetch");
const { patch_members, BASE_URL } = require("../../config/config");
const s3 = require("../../config/aws-s3-config");
const config = require("../../config/config");
const kafka = require("../middlewares/kafka");

module.exports = {
  async store(req, res, next) {
    try {
      let authHeader = req.headers["authorization"];
      let idToken = req.headers["idToken"];
      appLogger.info("POST Call");
      const healthIdModel = new HealthIdModel(req.body);
      healthIdModel.status = "INITIALIZED";
      healthIdModel.initialize = {};
      healthIdModel.audit = {
        dateCreated: Date.now(),
        createdBy: req.authorization.username,
        dateModified: Date.now(),
        modifiedBy: req.authorization.username,
      };
      const dbResponse = await healthIdModel.save();
      res.send({ msg: "successful", id: dbResponse._id });
      kafka.produce({
        authHeader: authHeader,
        domainObject: dbResponse,
        idToken: idToken,
        type: "healthIdCreated",
      });
    } catch (e) {
      if (e.name === "ValidationError") {
        next(ApiError.mongooseValidationError(e));
      }
      if (e.toString().includes("buffering timed out")) {
        next(ApiError.databaseRequestTimeout(e));
      } else {
        next(e);
      }
    }
  },

  async patch(req, res, next) {
    const id = req.params.health_id;
    const reqBody = req.body;

    let updateData = {};
    try {
      switch (assertNotNull(reqBody.status)) {
        /* case 'MEMBERPATCHED':
                    updateData = {
                        status: 'GENERATED',
                        generatedTimeStamp: assertNotNull(reqBody.timestamp, 'timestamp'),
                        $set: {
                            'initialize.success': assertNotNull(reqBody.success, 'success')
                        },
                        healthId: assertNotNull(reqBody.success.healthId, 'success.healthId')
                    }
                    assertNotNull(reqBody.success.generatedTimeStamp, 'success.generatedTimeStamp')
                    break; */
        case "GENERATED":
          assertNotNull(reqBody.success.healthId, "success.healthId");
          assertNotNull(
            reqBody.success.generatedTimeStamp,
            "success.generatedTimeStamp"
          );
          assertNotNull(reqBody.timestamp, "timestamp");
          const healthIDRecord = await HealthIdModel.findById(id);
          if (!healthIDRecord) {
            return ApiError.notFound("HealthId record not found", "id");
          }
          const citizenId = healthIDRecord.citizenId;
          const data = {
            type: "Citizen",
            properties: {
              healthID: reqBody.success.healthId,
            },
          };
          try {
            // const response = await fetch(
            //   `${BASE_URL}/${patch_members}/${citizenId}`,
            //   {
            //     method: "PATCH", // *GET, POST, PUT, DELETE, etc.
            //     cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
            //     headers: {
            //       "Content-Type": "application/json",
            //       Authorization: req.headers.authorization,
            //       IdToken: req.headers.idtoken,
            //     },
            //     body: JSON.stringify(data), // body data type must match "Content-Type" header
            //   }
            // );
            // console.log(`${BASE_URL}/${patch_members}/${citizenId}`);
            // console.log(JSON.stringify(data));
            // appLogger.info(
            //   "response from membership service " +
            //     response.status +
            //     JSON.stringify(await response.json())
            // );
            // if (!req.headers.idtoken || response.status === 401) {
            //   // return res.send("asas");
            //   return res.send(
            //     ApiError.badRequest("Required IdToken in headers", "IdToken")
            //   );
            // }
            // if (response.status !== 200) {
            //   return res.send(
            //     ApiError.notFound("Cannot update citizen", "citizenId")
            //   );
            // }
            updateData = {
              status: "GENERATED",
              generatedTimeStamp: assertNotNull(reqBody.timestamp, "timestamp"),
              $set: {
                "initialize.success": assertNotNull(reqBody.success, "success"),
              },
              healthId: assertNotNull(
                reqBody.success.healthId,
                "success.healthId"
              ),
            };
          } catch (err) {
            console.log("error from membership services");
            console.log(err);
            return next(err);
          }

          break;
        case "FAILURE":
          updateData = {
            status: "FAILURE",
            $set: {
              "initialize.failure": assertNotNull(reqBody.failure, "failure"),
            },
          };
          assertNotNull(reqBody.failure.errorInfo, "failure.errorInfo");
          break;
        case "HANDEDOVER":
          updateData = {
            status: "HANDEDOVER",
            handoverTimestamp: assertNotNull(reqBody.timestamp, "timestamp"),
          };
          break;
        case "PRINTED":
          updateData = {
            status: "PRINTED",
            cardPrintedTimestamp: assertNotNull(reqBody.timestamp, "timestamp"),
          };
          break;
        case "EVIDENCED":
          assertNotNull(reqBody.evidenced, "evidenced");
          updateData = {
            status: "EVIDENCED",
            isEvidenced: assertNotNull(
              reqBody.evidenced.isEvidenced,
              "evidenced.isEvidenced"
            ),
          };
          break;
        default:
          break;
      }
    } catch (e) {
      console.log(e);
      e = JSON.parse(e.message);
      next(ApiError.partialContent(e.msg, e.field));
      return;
    }
    if (updateData.$set === undefined) {
      updateData.$set = {};
    }
    if (reqBody.faceId) {
      updateData.faceId = reqBody.faceId;
    }
    updateData.$set["audit.dateModified"] = Date.now();
    updateData.$set["audit.modifiedBy"] = req.authorization.username;

    try {
      let dbResponse;
      if (reqBody !== "GENERATED") {
        dbResponse = await HealthIdModel.findOneAndUpdate(
          { _id: id },
          updateData,
          { new: true }
        );
      } else {
        dbResponse = updateData;
      }
      if (dbResponse === null) {
        next(ApiError.notFound(`Cannot find document with _id ${id}`, "_id"));
        return;
      }
      res.send(dbResponse);
      if (
        dbResponse !== null &&
        req.headers["originator-service"] !== "PHC_SAGA"
      ) {
        let authHeader = req.headers["authorization"];
        let idToken = req.headers["idToken"];
        kafka.produce({
          authHeader: authHeader,
          domainObject: dbResponse,
          idToken: idToken,
          type: "healthIdUpdated",
        });
      }
    } catch (e) {
      if (e.name === "ValidationError") {
        next(ApiError.mongooseValidationError(e));
      } else if (e.toString().includes("buffering timed out")) {
        next(ApiError.databaseRequestTimeout(e));
      } else if (e.path === "_id") {
        next(ApiError.badRequest(e.message, e.path));
      } else {
        next(e);
      }
    }
  },

  async filter(req, res, next) {
    const { page, size: limit } = req.query;
    const { gender, staffId } = req.body;
    try {
      if (isNaN(+limit)) {
        next(ApiError.partialContent("no or invalid value found", "size"));
        return;
      }
      if (isNaN(+page)) {
        next(ApiError.partialContent("no or invalid value found", "page"));
        return;
      }
      const skip = (page - 1) * limit;
      const filter = {};
      if (staffId && staffId.length != 0) filter.staffId = { $in: staffId };
      if (gender && gender.length != 0) {
        filter["initialize.success.gender"] = { $in: gender };
        // filter.initialize.success = { $exists: true }
        // filter.initialize.success.gender = { $exists: true }
      }

      const response = {};
      const data = await HealthIdModel.find(filter).limit(+limit).skip(skip);
      // .sort({ name: 'asc' })
      const count = await HealthIdModel.find(filter).count();
      const meta = {
        totalPages: Math.ceil(count / limit),
        totalElements: count,
        number: +page,
        size: +limit,
      };
      response.meta = meta;
      response.data = data;
      res.send(response);
    } catch (err) {
      next(err);
      return;
    }
  },

  async fetch(req, res, next) {
    const healthId = req.params.health_id;
    try {
      appLogger.info("GET Call");
      const data = await HealthIdModel.findOne({ _id: healthId });
      if (data) res.send(data);
      else {
        next(
          ApiError.notFound(`HealthId not found for _id ${healthId}`, "_id")
        );
        return;
      }
    } catch (err) {
      next(ApiError.partialContent(err.message, err.path));
      return;
    }
  },

  async presignedUrl(req, res, next) {
    try {
      appLogger.info("Get Presigned URL");
      assertNotNull(req.query.healthId, "params.healthId");
      const urlKey = "images/" + req.query.healthId;
      const preSignedUrlParams = {
        Bucket: config.aws_bucket_object,
        Key: urlKey,
      };
      const preSignedUrlPromise = s3.getSignedUrlPromise(
        "putObject",
        preSignedUrlParams
      );
      preSignedUrlPromise.then(
        function (url) {
          appLogger.info("The PreSignedURL is");
          appLogger.info(url);
          res.send({ preFetchURL: url });
        },
        (err) => {
          appLogger.error("Error in fetching pre-signed", err);
          next(ApiError.partialContent(err.message, err.path));
        }
      );
    } catch (err) {
      next(ApiError.partialContent(err.message, err.path));
      return;
    }
  },

  async imageUrl(req, res, next) {
    try {
      appLogger.info("Get Image URL");
      assertNotNull(req.query.healthId, "params.healthId");
      const urlKey = "images/" + req.query.healthId;
      const expiration = 60 * 60;
      const preSignedUrlParams = {
        Bucket: config.aws_bucket_object,
        Key: urlKey,
        Expires: expiration,
      };
      const preSignedUrlPromise = s3.getSignedUrlPromise(
        "getObject",
        preSignedUrlParams
      );
      preSignedUrlPromise.then(
        function (url) {
          appLogger.info("The PreSignedURL is");
          appLogger.info(url);
          res.send({ preFetchURL: url });
        },
        (err) => {
          appLogger.error("Error in fetching pre-signed", err);
          next(ApiError.partialContent(err.message, err.path));
        }
      );
    } catch (err) {
      next(ApiError.partialContent(err.message, err.path));
      return;
    }
  },

  async abdmAuth(req, res, next) {
    const data = {
      clientId: `${process.env.ABDM_CLIENT_ID}`,
      clientSecret: `${process.env.ABDM_CLIENT_SECRET}`,
    };
    const response = await fetch(`${process.env.ABDM_AUTH_URL}`, {
      method: "POST", // *GET, POST, PUT, DELETE, etc.
      cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data), // body data type must match "Content-Type" header
    });
    if (response.status !== 200) {
      return res.send(ApiError.badRequest("Error in genterating the Auth"));
    } else {
      return res.send(await response.json());
    }
  },
};
