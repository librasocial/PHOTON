const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("../models/Audit");

const ObservationSchema = new mongoose.Schema({
	citizenId: String, //uuid of the citizen or null for external
	patientId: String, //id of the patient
	UHId: String,
	encounterId: String, // the encounter for which this vital sign is captured
	encounterDate: Date, // the encounter date
	effectiveDate: Date, // the date when the observation is captured ISO 8601 date time
	doctor: String, //staff Id of the medical officer
	type: {
		type: String,
		enum: ["Physical Examination", "Provisional Diagnosis", "Final Diagnosis", "Clinical Diagnosis"],
	},
	assessments: [
		{
			assessmentTitle: String,
			observations: [
				{
					observationName: String, // the label of the vital sign
					observationValues: [String, String, String],
				},
			],
		},
	],
	audit: {
		type: Audit,
		required: true,
	},
});

const ObservationModel = mongoose.model(
	Config.collection_names.Observation,
	ObservationSchema
);

module.exports = ObservationModel;
