const { check } = require("express-validator");

let storeObservation = [
	check("citizenId").optional().isString(),
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("effectiveDate").optional().isISO8601(),
	check("doctor").optional().isString(),
	check("type").optional().isString(),
	check("assessments").isArray().isLength({ min: 1 }),
];

module.exports = storeObservation;
