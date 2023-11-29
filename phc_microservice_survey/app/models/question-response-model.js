const QuestionResponseModel = {
  question: { type: String, required: true },
  response: [{type: Object, required: false}],
  propertyName: {type: String, required: false}
};

module.exports = QuestionResponseModel
