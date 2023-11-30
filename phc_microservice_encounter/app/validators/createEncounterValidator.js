const { check } = require("express-validator");

let createEncounterValidator = [
	check("patientId", "String").notEmpty().trim(),
	// check("audit", "Object").notEmpty().trim(),
];

module.exports = createEncounterValidator;
