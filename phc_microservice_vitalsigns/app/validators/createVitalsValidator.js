const { check } = require("express-validator");

let createVitalsValidator = [
	check("UHId", "String").isString(),
	check("patientId", "String").isString(),
	check("encounterId", "String").isString(),
	check("medicalSigns", "Array").isArray(),
];

module.exports = createVitalsValidator;
