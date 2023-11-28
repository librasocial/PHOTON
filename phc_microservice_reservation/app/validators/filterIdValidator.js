const { check } = require("express-validator");

let filterIdValidator = [
	check("id", "String").notEmpty().trim(),
	check("Patient", "Object").notEmpty().trim(),
	check("Provider", "Object").notEmpty().trim(),
	check("audit", "Object").notEmpty().trim(),
];

module.exports = filterIdValidator;
