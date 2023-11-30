const ApiError = require("../errors/handler");
const SurveyMasterModel = require("../models/survey-master");

module.exports = {
  async filter(req, res, next) {
    const { surveyType, surveyName, version, limit = 5, page = 1 } = req.query
    const skip = (page - 1) * limit

    const filter = {}
    if (surveyType) {
      filter.surveyType = surveyType
    }
    if (surveyName) {
      filter.surveyName = surveyName
    }
    if (version) {
      filter.version = version
    }
    try {
      const response = {}
      const data = await SurveyMasterModel
        .find(filter)
        .limit(+limit)
        .skip(skip)

      const count = await SurveyMasterModel.find(filter).count()
      const meta = {
        totalPages: Math.ceil(count / limit),
        totalElements: count,
        number: +page,
        size: +limit
      }
      response.meta = meta
      response.data = data
      res.send(response)
    } catch (err) {
      next(ApiError.partialContent(err))
      return
    }
  },

  async fetch(req, res, next) {
    const surveyId = req.params.surveyId
    try {
      const data = await SurveyMasterModel
        .findOne({surveyType: surveyId })
      if(data)
        res.send(data)
      else next(ApiError.notFound(`No survey found for surveyType - ${surveyId}`));
    } catch (err) {
      next(ApiError.partialContent(err))
      return
    }
  }
}
