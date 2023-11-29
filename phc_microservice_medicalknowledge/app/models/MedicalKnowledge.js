const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("../models/Audit");

const MedicalKnowledgeSchema = new mongoose.Schema({
	patientId: String, //           "string, id of the patient",
	UHId: String, //                "string",
	encounterId: String, //         "string, the encounter for which this vital sign is captured",
	encounterDate: Date, //         "isodatetime",
	doctor: String, //              "string, staff Id of the medical officer",
	clarification: String, //       "string",
	audit: { type: Audit, required: true },
});

const MedicalKnowledgeModel = mongoose.model(
	Config.collection_names.MedicalKnowledge,
	MedicalKnowledgeSchema
);

module.exports = MedicalKnowledgeModel;
