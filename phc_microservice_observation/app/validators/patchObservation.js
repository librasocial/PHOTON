const { check } = require("express-validator");

const patchObservation = [
	check("citizenId").optional().isString(),
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("doctor").optional().isString(),
	check("type").optional().isString(),
	check("assessments").isArray().isLength({ min: 1 }),
];

module.exports = patchObservation;
