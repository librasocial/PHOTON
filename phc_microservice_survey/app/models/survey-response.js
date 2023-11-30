const mongoose = require('mongoose');
const Config = require('../../config/config');
const AuditModel = require('./audit');
const QuestionResponseModel = require('./question-response-model');
const ObjectId = mongoose.Schema.Types.ObjectId;

const SurveyResponseSchema = new mongoose.Schema({
  surveyId: { type: ObjectId, ref: Config.collection_names.survey_masters },
  surveyName:{ type: String , required: true },
  surveyYear:{ type: String , required: false },
  context: {
		hId: String,
		villageId: String,
		memberId: String
	},
  quesResponse: [{ type: QuestionResponseModel, required: true }],
  conductedBy: { type: String, ref: 'volunteers', required: true },
  respondedBy: { type: String, ref: 'users' },
  audit: { type: AuditModel, required: true }
});

const SurveyResponseModel = mongoose.model(Config.collection_names.survey_responses, SurveyResponseSchema);

module.exports = SurveyResponseModel
