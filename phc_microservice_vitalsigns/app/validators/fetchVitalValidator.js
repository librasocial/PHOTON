const { check } = require("express-validator");

let fetchVitalsValidator = [
	check("vitalSignId", "String").notEmpty().isString(),
];

module.exports = fetchVitalsValidator;
