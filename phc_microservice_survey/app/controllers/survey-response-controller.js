const ApiError = require("../errors/handler");
const SurveyResponseModel = require("../models/survey-response");
const kafka = require("../middlewares/kafka");

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
      const dbResponse = await new SurveyResponseModel(body).save();
      res.send(dbResponse);
      kafka.produce({
        authHeader: authHeader,
        domainObject: dbResponse,
        idToken: idToken,
        topic: "Survey",
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
      const data = await SurveyResponseModel.find({ surveyId: surveyId })
        .limit(+limit)
        .skip(skip);
      // .sort({ name: 'asc' })
      const count = await SurveyResponseModel.where({
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
    const contextId = req.params.contextId;
    const surveyType = req.query.surveyType;
    let key;
    if (surveyType === "HouseHold") {
      key = "context.hId";
    } else if (surveyType === "Village") {
      key = "context.villageId";
    } else {
      key = "context.memberId";
    }
    try {
      const response = await SurveyResponseModel.findOne(
        { [key]: contextId },
        {},
        { sort: { "audit.dateModified": -1 } }
      );
      if (response === null) {
        next(
          ApiError.notFound(
            `No Survey has been responded for ${key}:${contextId}`
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
