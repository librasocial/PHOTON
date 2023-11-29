const QuestionOptionsModel = {
  question: { type: String, required: true },
  choices: [{ type: Object }],
  propertyName: { type: String, required: false },
  displayName: { type: String, required: false },
};

module.exports = QuestionOptionsModel;
