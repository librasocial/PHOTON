const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("../models/Audit");

const MedicalConditionSchema = new mongoose.Schema({
	encounterId: String,                     // encounter in which this chief complaint is recorded,
	encounterDate: Date,					 // date of encounter
	patientId: String,                       // the patient who has the complaint
	UHId: String,                            // the UHID of the patient
	recordedBy: String,                      // staff Id of the nurse of ANM who is recording it
	doctor: String,                          // staff Id of the medical officer",
	effectiveDate: Date,                     // ISO 8601 date time,
	effectivePeriod: String,                 //"ISO 8601 duration",
	complaintText: String,
	audit: { type: Audit, required: true },
});

const MedicalConditionModel = mongoose.model(
	Config.collection_names.MedicalCondition,
	MedicalConditionSchema
);

module.exports = MedicalConditionModel;
