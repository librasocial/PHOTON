const mongoose = require("mongoose");
const Config = require("../../config/config");
const AuditModel = require("./audit");

const EncounterSchema = new mongoose.Schema({
	UHId: String,				//uhid of patient
	citizenId: String, 			//uuid of the citizen or null for external
	patientId: String,
	purpose: { type: String, enum: ["Doctor", "Lab", "Pharmacy", "Optometry"] },
	visitType: {
		type: String,
		enum: ["In Patient", "Out Patient", "Emergency"],
	},
	assignedTo: {
		staffId: String, 		//staff id of the doctor or pharmacist or lab technician
		staffName: String, 		//name of the staff member
		staffType: String, 		//e.g. head medical or pharmcist etc
	},
	referredByAshaWorker: {
		staffId: String, 		//staff id of the asha worker
		staffName: String,
		staffType: String,		//e.g. Asha Worker
	},
	referredByDoctor: {
		staffId: String,		//staff id of the doctor
		staffName: String,
		staffType: String,		//e.g. Asha Worker
	},
	attendantName: String,
	attendantPhone: String,
	reservationFlag: { type: Boolean, default: false },
	audit: { type: AuditModel, required: true },
});

const EncounterModel = mongoose.model(
	Config.collection_names.Encounter,
	EncounterSchema
);

module.exports = EncounterModel;
