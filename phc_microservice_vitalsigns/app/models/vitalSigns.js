const mongooose = require("mongoose");
const Config = require("../../config/config");
const AuditModel = require("./audit");

const VitalSignSchema = new mongooose.Schema({
	citizenId: String,
	patientId: String,
	UHId: String,
	encounterId: String,
	encounterDate: Date,
	medicalSigns: [
		{
			signName: String,
			signValue: String,
		},
	],
	audit: { type: AuditModel, required: true },
});

const VitalSignsModel = mongooose.model(
	Config.collection_names.VitalSigns,
	VitalSignSchema
);

module.exports = VitalSignsModel;