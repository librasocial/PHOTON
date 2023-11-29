const mongoose = require("mongoose");

module.exports = {
	dateCreated: { type: Date, required: true },
	createdBy: { type: String, required: true },
	dateModified: { type: Date },
	modifiedBy: { type: String },
};
