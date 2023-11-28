const { check } = require("express-validator");

let searchPatientValidator = [
	check("fieldName")
		.isIn(["Name", "DateOfBirth", "MobileNumber", "UHID", "HealthID"])
		.notEmpty()
		.trim(),
	check("value", "String").notEmpty().trim(),
];

module.exports = searchPatientValidator;
