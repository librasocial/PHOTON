const mongoose = require("mongoose");

const Config = {
	api_version: "0.0.51",
	collection_names: {
		Patient: "Patient",
		PatientHealthCondition: "PatientHealthCondition",
	},
};

module.exports = Config;
