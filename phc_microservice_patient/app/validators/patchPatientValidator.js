const { check } = require("express-validator");

let patchPatientValidator = [
	check("citizen.salutation", "String").isString(),
	check("citizen.firstName", "String").isString(),
	check("citizen.dateOfBirth", "ISO8601format").isISO8601(
		"yyyy-mm-dd hh:mm:ss"
	),
];

module.exports = patchPatientValidator;
