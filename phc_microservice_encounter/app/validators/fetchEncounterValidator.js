const { check } = require("express-validator");

let fetchEncounterValidator = [
	check("patientId", "String").notEmpty().trim(),
	check("audit", "Object").notEmpty().trim(),
];

module.exports = fetchEncounterValidator;
