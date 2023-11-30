let { check } = require("express-validator");

const patchPrescription = [
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("prescribedBy").optional().isString(),
	check("prescribedOn").optional().isISO8601(),
	check("partOfPrescription").optional().isString(),
	check("drugs").optional().isArray(),
	check("medicalTests").optional().isArray(),
	check("instruction").optional().isString(),
	check("reviewAfter").optional().isString(),
];

module.exports = patchPrescription;
