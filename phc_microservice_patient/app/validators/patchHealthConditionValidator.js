const { check } = require("express-validator");

let patchHealthConditionValidator = [
	check("healthId", "String").notEmpty().trim(),
	check("patientId", "String").notEmpty().trim(),
];

module.exports = patchHealthConditionValidator;
