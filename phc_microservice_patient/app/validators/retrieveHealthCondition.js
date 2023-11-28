const { check } = require("express-validator");

let retrieveHealthConditionValidator = [
	check("id", "String").notEmpty().trim(),
	check("patientId", "String").notEmpty().trim(),
];

module.exports = retrieveHealthConditionValidator;
