const mongoose = require("mongoose");
const Config = require("../../config/config");
const AuditModel = require("./audit");

const ReservationSchema = new mongoose.Schema({
  Patient: {
    UHId: String,
    patientId: { type: String, required: true },
    abhaAddress: String,
    memberId: String,
    healthId: String,
    name: String,
    gender: String,
    dob: Date,
    phone: String,
  },
  Provider: {
    memberId: String,
    name: String,
  },
  reservationFor: { type: String, required: true },
  reservationTime: Date,
  encounterId: String,
  visitType: String,
  status: {
    type: String,
    enum: [
      "Cancelled",
      "Confirmed",
      "OnHold",
      "Pending",
      "VitalsCaptured",
      "CheckedIn",
      "startedConsultation",
    ],
  },
  tokenNumber: { type: String, default: null },
  label: String,
  audit: { type: AuditModel, required: true },
});

const ReservationModel = mongoose.model(
  Config.collection_names.Reservation,
  ReservationSchema
);

module.exports = ReservationModel;
