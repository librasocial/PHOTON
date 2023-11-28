const { check } = require("express-validator");

let retrievePatientValidator = [
	check("id", "String").notEmpty().trim(),
];

module.exports = retrievePatientValidator;
