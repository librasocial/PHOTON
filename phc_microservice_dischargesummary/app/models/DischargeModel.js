const mongoose = require('mongoose')
const Config = require('../../config/config')
const Audit = require('./Audit')

const DischargeSchema = new mongoose.Schema({
  patientId: String, // "string, id of the patient",
  UHId: String, // "string",
  encounterId: String, // "string, the encounter for which this vital sign is captured",
  encounterDate: Date, // "isodatetime",
  doctor: String, // "string, staff Id of the medical officer",
  deathDateTime: Date,
  type: {
    type: String,
    enum: ['Sent Home', 'Expired', 'Medical Advice', 'Refer To', 'Cured', 'Dropouts']
  },
  remarks: String, // "string, the summary information",
  referTo: String, // "string, a doctor's name",
  referType: String,
  nextVisit: String, // "string iso8601 duration",
  referredHospital: String, // "string, the hospital name"
  audit: { type: Audit, required: true }
})

const DischargeModel = mongoose.model(
  Config.collection_names.DischargeSummary,
  DischargeSchema
)

module.exports = DischargeModel
