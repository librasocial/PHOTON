const { check } = require("express-validator");

let filterValidator = [
	check("data", "Array").notEmpty().trim(),
	check("meta", "Object").notEmpty().trim(),
];

module.exports = filterValidator;
