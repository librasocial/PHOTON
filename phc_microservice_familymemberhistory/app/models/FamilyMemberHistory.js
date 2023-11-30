const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("../models/Audit");

const FamilyMemberHistorySchema = new mongoose.Schema({
	patientId: String, //       "string, id of the patient",
	UHId: String,
	encounterId: String, //     "string, the encounter for which this vital sign is captured",
	encounterDate: Date, //     "isodatetime",
	doctor: String, //          "string, staff Id of the medical officer",
	type: {
		type: String,
		enum: ["Illness", "Medical", "Surgical", "Family", "Social"],
	},
	conditions: [
		{
			member: String, //  "string, family member name",
			problem: String, // "string, the medical condition",
			onsetPeriod: String, // "string iso8601 duration",
		},
	],
	audit: { type: Audit, required: true },
});

const FamilyMemberHistoryModel = mongoose.model(
	Config.collection_names.FamilyMemberHistory,
	FamilyMemberHistorySchema
);

module.exports = FamilyMemberHistoryModel;
