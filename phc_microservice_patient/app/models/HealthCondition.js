const mongoose = require("mongoose");
const ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = {
	conditionId: ObjectId,
	associatedAnatomy: String,
	signOrSymptom: String,
	possibleComplication: String,
	instructedByDr: { type: mongoose.Schema.Types.ObjectId },
	recordedBy: { type: mongoose.Schema.Types.ObjectId },
	recordedOn: Date,
};
