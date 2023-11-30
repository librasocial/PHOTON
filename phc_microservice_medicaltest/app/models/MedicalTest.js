const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("./Audit");

const MedicalTestSchema = new mongoose.Schema({
	patientId: String, //id of the patient
	UHId: String,
	encounterId: String, // the encounter for which this vital sign is captured
	encounterDate: Date, // the encounter date
	effectiveDate: Date, // the date when the observation is captured ISO 8601 date time
	doctor: String, //staff Id of the medical officer
	tests: [String, String, String],
	audit: {
		type: Audit,
		required: true,
	},
});

const MedicalTestModel = mongoose.model(
	Config.collection_names.MedicalTest,
	MedicalTestSchema
);

module.exports = MedicalTestModel;
