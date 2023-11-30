const mongoose = require('mongoose');
const ObjectId = mongoose.Schema.Types.ObjectId;

module.exports = {
  dateCreated: { type: Date, required: true },
  createdBy: { type: String, required: true },
  dateModified: { type: Date },
  modifiedBy: { type: String }
}
