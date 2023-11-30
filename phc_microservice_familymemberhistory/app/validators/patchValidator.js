let { check } = require("express-validator");

const patchValidator = [
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("encounterDate").optional().isISO8601(),
	check("doctor").optional().isString(),
	check("type").optional().isString(),
	check("conditions").optional().isArray(),
	check("conditions.*.member").optional().isString(),
	check("conditions.*.problem").optional().isString(),
	check("conditions.*.onsetPeriod").optional().isString(),
];

module.exports = patchValidator;
