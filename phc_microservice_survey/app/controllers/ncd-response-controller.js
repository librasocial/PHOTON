const ApiError = require("../errors/handler");
const kafka = require("../middlewares/kafka");
const NCDResponseModel = require("../models/ncd-response");

module.exports = {
  async store(req, res, next) {
    try {
      let authHeader = req.headers["authorization"];
      let idToken = req.headers["idToken"];
      let body = req.body;
      body.audit = {
        dateCreated: Date.now(),
        createdBy: req.authorization.username,
        dateModified: Date.now(),
        modifiedBy: req.authorization.username,
      };
      body.conductedBy = req.authorization.username;
      const dbResponse = await new NCDResponseModel(body).save();
      res.send(dbResponse);
      kafka.produce({
        authHeader: authHeader,
        domainObject: dbResponse,
        idToken: idToken,
        topic:"NCD"
      });
      res.send(dbResponse);
    } catch (err) {
      next(ApiError.partialContent(err));
      return;
    }
  },

  async fetch(req, res, next) {
    const surveyId = req.params.surveyId;
    const { page, size: limit } = req.query;
    const skip = (page - 1) * limit;
    try {
      const response = {};
      const data = await NCDResponseModel.find({ surveyId: surveyId })
        .limit(+limit)
        .skip(skip);
      // .sort({ name: 'asc' })
      const count = await NCDResponseModel.where({
        surveyId: surveyId,
      }).count();
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
      next(ApiError.partialContent(err));
      return;
    }
  },

  async getSurveyRecord(req, res, next) {
    const villageId = req.query.villageId;
    const houseHoldId = req.query.houseHoldId;
    const citizenId = req.query.citizenId;

    let filters = {};
    if (villageId) {
      filters["villageId"] = villageId;
    }
    if (houseHoldId) {
      filters["houseHoldId"] = houseHoldId;
    }
    if (citizenId) {
      filters["citizenId"] = citizenId;
    }
    try {
      const response = await NCDResponseModel.find(
        filters,
        {},
        { sort: { "audit.dateModified": -1 } }
      );
      if (response === null) {
        next(
          ApiError.notFound(
            `No NCD has been responded`
          )
        );
        return;
      }
      res.send(response);
    } catch (err) {
      console.log(err);
      next(ApiError.partialContent(err));
      return;
    }
  },
};
