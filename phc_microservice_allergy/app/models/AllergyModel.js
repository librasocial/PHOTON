const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("../models/Audit");

const AllergySchema = new mongoose.Schema({
  patientId: String, //       "string, id of the patient",
  UHId: String,
  encounterId: String, //     "string, the encounter for which this vital sign is captured",
  encounterDate: Date, //     "isodatetime",
  recordedBy: String, // 		"string, staff Id of the nurse of ANM who is recording it",
  doctor: String, //          "string, staff Id of the medical officer",
  category: {
    type: String,
    enum: ["Food", "Drug", "Environment", "Unknown"],
  },
  allergen: String, // "string, the name of the allergen",
  reactionType: {
    type: String,
    enum: ["Adverse reaction", "Intolerance", "Side effect", "Toxicity","Allergy","Idiosyncrasy","Unknown"],
  },
  reaction: String, // "string, the details of the reaction",
  confirmationType: {
    type: String,
    enum: ["Sure", "Suspected"],
  },
  approximately: {
    type: Boolean,
  },
  dateSince: String,
  day: { type: Number },
  month: { type: Number },
  year: { type: Number },
  onsetDate: Date,
  status: {
    type: String,
    enum: ["Active", "Resolved"],
  },
  severity: {
    type: String,
    enum: ["Mild", "Moderate", "Severe"],
  },
  infoSource: String,
  reactionSite: String, // "string, the body part site",
  relievingFactor: String,
  closureDate: Date,
  remarks: {
    type: String,
  },
  audit: {
    type: Audit,
    required: true,
  },
});

const AllergyModel = mongoose.model(
  Config.collection_names.Allergy,
  AllergySchema
);

module.exports = AllergyModel;
