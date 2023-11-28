const { check } = require("express-validator");

let registerValidator = [
	check("citizen.photoUrl", "String").notEmpty().trim(),
	check("citizen.salutation", "String").notEmpty().trim(),
	check("citizen.firstName", "String").notEmpty().trim(),
	check("citizen.dateOfBirth", "ISO8601format").isISO8601(
		"yyyy-mm-dd hh:mm:ss"
	),
	check("citizen.age", "Number").notEmpty().trim(),
];

module.exports = registerValidator;
