const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("./Audit");

const PrescriptionSchema = new mongoose.Schema({
  patientId: String,
  abhaAddress: String,
  careContext: String,
  UHId: String,
  encounterId: String,
  encounterDate: Date,
  prescribedBy: String, // the staff id of the doctor",
  prescribedOn: Date,
  partOfPrescription: String, // id of prescription chain
  products: [
    {
      productId: String,
      name: String, // the name of the medicine
      code: String, // the snomed code of the medicine
      strength: String, // Quantity of ingredient present. e.g. 500mg
      route: String, // oral, intravenous, etc.
      routeCode: String, // the snomed code of route
      form: String,
      frequency: [String, String, String, String], //["morning", "afternoon", "evening", "night"],
      duration: Number, // iso duration format
      timing: String, //["after food", "before food", "during food"],
      startDate: Date,
      instruction: String, //  any other instructions
    },
  ],
  medicalTests: [String, String, String], // ["medical test name", "medical test name", "medical test name"],
  reviewAfter: String, // iso duration format
  audit: {
    type: Audit,
    required: true,
  },
});

const PrescriptionModel = mongoose.model(
  Config.collection_names.prescription,
  PrescriptionSchema
);

module.exports = PrescriptionModel;
