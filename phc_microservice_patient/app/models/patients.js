const mongoose = require("mongoose");
const Config = require("../../config/config");
const AuditModel = require("./audit");

const PatientSchema = new mongoose.Schema({
  UHId: String,
  lgdCode: String,
  citizen: {
    healthId: String,
    abhaAddress: String,
    memberId: String,
    salutation: { type: String, required: true },
    firstName: { type: String, required: true, index: true },
    middleName: String,
    lastName: { type: String, index: true },
    photoUrl: String,
    mobile: { type: String, required: true, index: true },
    gender: { type: String, required: true },
    dateOfBirth: { type: Date, index: true },
    age: Number,
    maritalStatus: { type: String },
    religion: { type: String },
    residentType: { type: String, required: true },
    caste: { type: String },
    occupation: { type: String },
    fatherName: { type: String },
    spouseName: { type: String },
    address: {
      present: {
        addressLine: { type: String },
        area: String,
        village: String,
        state: { type: String },
        country: { type: String },
        district: { type: String },
        taluk: { type: String },
        pinCode: { type: String },
      },
      permanentSameAsPresent: Boolean,
      permanent: {
        addressLine: String,
        area: String,
        village: String,
        state: String,
        country: String,
        district: String,
        taluk: String,
        pinCode: String,
      },
    },
  },
  citizenMoreInfo: {
    birthPlace: String,
    birthIdentifiers: [String, String, String],
    annualIncome: Number,
    education: String,
    primaryLanguage: String,
    speakEnglish: Boolean,
    phone: String,
    emailId: String,
  },
  registeredOn: Date,
  audit: { type: AuditModel, required: true },
});

const PatientModel = mongoose.model(
  Config.collection_names.Patient,
  PatientSchema
);

module.exports = PatientModel;
