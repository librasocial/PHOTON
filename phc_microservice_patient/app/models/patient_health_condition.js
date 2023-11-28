const mongoose = require("mongoose");
const Config = require("../../config/config");
const AuditModel = require("./audit");
const healthConditionModel = require("./HealthCondition");

const PatientHealthConditionSchema = mongoose.Schema({
	healthId: String,
	patientId: { type: mongoose.Schema.Types.ObjectId, required: true },
	healthConditions: [{ type: healthConditionModel, required: true }],
	audit: { type: AuditModel, required: true },
});

const PatientHealthConditionModel = mongoose.model(
	Config.collection_names.PatientHealthCondition,
	PatientHealthConditionSchema
);

module.exports = PatientHealthConditionModel;
