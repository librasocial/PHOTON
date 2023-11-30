const mongoose = require('mongoose');
const Config = require('../../config/config');
const AuditModel = require('./audit');
const QuestionOptionsModel = require('./question-options-model');

const SurveyMasterSchema = new mongoose.Schema({
  surveyName: { type: String, required: true },
  surveyType: { type: String, required: true },
  version: { type: String, required: true },
  quesOptions: [{ type: QuestionOptionsModel, required: true }],
  audit: {
    type: {
      dateCreated: { type: Date, required: true },
      createdBy: { type: String, required: true }
    },
    required: true
  }
});

const SurveyMasterModel = mongoose.model(Config.collection_names.survey_masters, SurveyMasterSchema);

module.exports = SurveyMasterModel
