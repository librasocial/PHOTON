const { check } = require("express-validator");

let filterEncounterValidator = [
	check("patientId", "String").notEmpty().trim(),
	check("audit", "Object").notEmpty().trim(),
];

module.exports = filterEncounterValidator;
