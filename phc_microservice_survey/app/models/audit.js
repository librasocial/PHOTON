const mongoose = require('mongoose');

module.exports = {
  dateCreated: { type: Date, required: true },
  createdBy: {type: String, required : true},
  dateModified: {type:Date, required: true},
  modifiedBy: {type: String, required : true}
}
