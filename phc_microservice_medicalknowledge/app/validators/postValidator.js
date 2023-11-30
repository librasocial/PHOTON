let { check } = require("express-validator");

const postValidator = [
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("encounterDate").optional().isISO8601(),
	check("doctor").optional().isString(),
	check("clarification").optional().isString(),
];

module.exports = postValidator;
