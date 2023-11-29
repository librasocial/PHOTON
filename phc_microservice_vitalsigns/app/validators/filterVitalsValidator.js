const { check } = require("express-validator");

let filterVitalsValidator = [
	check("encounterId", "String").optional().isString(),
	check("encounterDate", "Date").optional().isDate(),
];

module.exports = filterVitalsValidator;
