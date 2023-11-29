const mongoose = require("mongoose");
const Config = require("../../config/config");
const AuditModel = require("./audit");

const HealthIdSchema = new mongoose.Schema({
  citizenId: { type: String, required: true, index: true },
  staffId: { type: String, required: true, index: true },
  faceId: { type: String },
  abhaAddress: { type: String },
  initialize: {
    type: {
      initiatedTimestamp: { type: Date, default: Date.now() },
      success: {
        healthId: String,
        firstName: { type: String, index: "text" }, // TODO index these
        middleName: { type: String, index: "text" }, // TODO index these
        lastName: { type: String, index: "text" }, // TODO index these
        gender: { type: String, enum: ["MALE, FEMALE", "OTHER"], index: true },
        dob: { type: Date, index: true },
        phone: { type: String, index: true },
        generatedTimeStamp: Date,
      },
      failure: {
        errorInfo: String,
      },
    },
    required: true,
  },
  healthId: { type: String, index: true },
  generatedTimeStamp: Date,
  cardPrintedTimestamp: Date,
  handoverTimestamp: Date,
  isEvidenced: Boolean,
  status: {
    type: String,
    enum: [
      "INITIALIZED",
      "GENERATED",
      "FAILURE",
      "HANDEDOVER",
      "PRINTED",
      "EVIDENCED",
    ],
    required: true,
    index: true,
  },
  audit: {
    type: AuditModel,
  },
});

const HealthIdModel = mongoose.model(
  Config.collection_names.health_id,
  HealthIdSchema
);

module.exports = HealthIdModel;
