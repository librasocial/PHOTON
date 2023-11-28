const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const { validate, db } = require("../models/patients");
const patientModel = require("../models/patients");
const appLogger = require("../../config/logger");

module.exports = {
  async store(req, res, next) {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }
    try {
      if (req.body.citizen.address) {
        const { permanentSameAsPresent, permanent } = req.body.citizen.address;
        if (permanentSameAsPresent && permanent) {
          return next(
            ApiError.badRequest(
              "Permanent Address Not Required",
              "Permanent Address"
            )
          );
        }
        if (!permanentSameAsPresent) {
          if (!permanent) {
            return next(
              ApiError.partialContent(
                "No or Invalid data found",
                "Permanent Address"
              )
            );
          }
        }
      }

      var newData = new patientModel(req.body);

      const randomNumber = Math.floor(Math.random() * 90000 + 10000);
      const lgdVillageCode = newData.lgdCode;

      newData.UHId = lgdVillageCode + "_" + randomNumber;

      newData.audit = {
        dateCreated: Date.now(),
        createdBy: req.authorization.username,
        dateModified: Date.now(),
        modifiedBy: req.authorization.username,
      };
      const dbResponse = await newData.save();
      res.send(dbResponse);
    } catch (err) {
      res.status(500).json(err);
      appLogger.error("error posting patient", err);
      next(err);
    }
  },
  async fetch(req, res, next) {
    const patientId = req.params.patientId;
    if (!patientId) {
      return next(ApiError.badRequest("PatiendId not found", "patientId"));
    }
    try {
      const patient = await patientModel.findOne({ _id: patientId });
      if (patient !== null) {
        res.send(patient);
      } else {
        next(
          ApiError.notFound(`Patient not found for _id ${patientId}`, "_id")
        );
        return;
      }
    } catch (err) {
      // res.send(err);
      next(err);
    }
  },
  async patch(req, res, next) {
    const patientId = req.params.patientId;
    if (!patientId) {
      return next(ApiError.badRequest("PatiendId not found", "patientId"));
    }

    try {
      const fieldsToBeUpdatedArray = ["UHId", "lgdCode", "registeredOn"];
      let update = {};
      const body = req.body;

      fieldsToBeUpdatedArray.forEach((field) => {
        if (body[field] !== null) {
          update[field] = body[field];
        }
      });

      if (body.citizen) {
        Object.entries(body.citizen).forEach(([key, value]) => {
          update[`citizen.${key}`] = value;
        });
      }

      if (body.citizenMoreInfo) {
        Object.entries(body.citizenMoreInfo).forEach(([key, value]) => {
          update[`citizenMoreInfo.${key}`] = value;
        });
      }

      const dbResponse = await patientModel.findByIdAndUpdate(
        patientId,
        update,
        { new: true }
      );

      if (!dbResponse) {
        next(
          ApiError.notFound(`Patient not found for _id ${patientId}`, "_id")
        );
        return;
      }

      res.send(dbResponse);
    } catch (err) {
      if (err.name === "CastError") {
        next(ApiError.badRequest(err.message, err.path));
      } else if (err.name === "ValidationError") {
        next(ApiError.mongooseValidationError(err));
      } else if (err.path === "_id") {
        next(ApiError.badRequest(err.message, err.path));
      } else {
        next(err);
      }
    }
  },
  async filter(req, res, next) {
    const {
      memberId,
      village,
      citizenId,
      healthId,
      UHId,
      regDate,
      page,
      size,
    } = req.query;

    try {
      if (isNaN(+size)) {
        next(ApiError.partialContent("No or invalid value found", "size"));
        return;
      }
      if (isNaN(+page)) {
        next(ApiError.partialContent("No or invalid value found", "page"));
        return;
      }

      const skip = (page - 1) * size;
      let filter = {};

      if (village) {
        filter = filter
          ? Object.assign(filter, {
              "citizen.address.present.village": village,
            })
          : { "citizen.address.present.village": village };
      }

      if (citizenId) filter._id = citizenId;

      if (healthId) {
        filter = filter
          ? Object.assign(filter, { "citizen.healthId": healthId })
          : { "citizen.healthId": healthId };
      }

      if (memberId) {
        filter = filter
          ? Object.assign(filter, { "citizen.memberId": memberId })
          : { "citizen.memberId": memberId };
      }

      if (regDate) filter.registeredOn = regDate;
      if (UHId) filter.UHId = UHId;

      const respo = {};

      const data = await patientModel.find(filter).limit(+size).skip(skip);

      const count = await patientModel.find(filter).count();

      const meta = {
        totalPages: Math.ceil(count / size),
        totalElements: count,
        number: +page,
        size: +size,
      };

      respo.meta = meta;
      respo.data = data;

      res.send(respo);
      return;
    } catch (err) {
      console.log(err);
      if (err.name === "CastError") {
        next(ApiError.badRequest(err.message, err.path));
      } else {
        next(err);
      }
      return;
    }
  },
  async search(req, res, next) {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }

    try {
      const { fieldName, value, page } = req.query;

      if (!fieldName) {
        next(
          ApiError.partialContent("No or partial content found", "fieldName")
        );
        return;
      }
      if (!value) {
        next(ApiError.partialContent("No or partial content found", "value"));
        return;
      }
      if (isNaN(+page)) {
        next(ApiError.partialContent("No or invalid value found", "page"));
        return;
      }

      let data;
      let count;
      const skip = (page - 1) * 25;
      const respo = {};

      if (fieldName === "UHID") {
        data = await patientModel.find({ UHId: value });
        count = 1;
      } else if (fieldName === "HealthID") {
        data = await patientModel
          .find({ "citizen.healthId": { $regex: value, $options: "i" } })
          .limit(25)
          .skip(skip);
        count = await patientModel
          .find({ "citizen.healthId": { $regex: value, $options: "i" } })
          .count();
      } else if (fieldName === "Name") {
        data = await patientModel
          .find({
            $or: [
              { "citizen.firstName": { $regex: value, $options: "i" } },
              { "citizen.lastName": { $regex: value, $options: "i" } },
            ],
          })
          .limit(25)
          .skip(skip);
        count = await patientModel
          .find({
            $or: [
              { "citizen.firstName": { $regex: value, $options: "i" } },
              { "citizen.lastName": { $regex: value, $options: "i" } },
            ],
          })
          .count();
      } else if (fieldName === "DateOfBirth") {
        data = await patientModel
          .find({ "citizen.dateOfBirth": value })
          .limit(25)
          .skip(skip);
        count = await patientModel
          .find({ "citizen.dateOfBirth": value })
          .count();
      } else if (fieldName === "MobileNumber") {
        data = await patientModel
          .find({ "citizen.mobile": value })
          .limit(25)
          .skip(skip);
        count = await patientModel.find({ "citizen.mobile": value }).count();
      } else {
        res.send("Invalid Inputs");
      }

      const meta = {
        totalPages: 1,
        totalElements: count,
        number: 1,
        size: count,
      };

      respo.meta = meta;
      respo.data = data;

      res.send(respo);
      return;
    } catch (err) {
      if (err.name === "CastError") {
        next(ApiError.badRequest(err.message, err.path));
      } else {
        // res.send(err);
        next(err);
      }
      return;
    }
  },
};
