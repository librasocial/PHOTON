const { check } = require("express-validator");

let patchVitalValidator = [
	check("vitalSignId", "String").notEmpty().isString(),
];

module.exports = patchVitalValidator;
