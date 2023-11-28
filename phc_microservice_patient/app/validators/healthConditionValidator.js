const { check } = require("express-validator");

let createHealthConditionValidator = [
	check("patientId", "String").notEmpty().trim(),
];

module.exports = createHealthConditionValidator;
