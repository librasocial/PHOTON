let { check } = require("express-validator");

const createPrescription = [
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("prescribedBy").isString(),
	check("prescribedOn").isISO8601(),
	check("partOfPrescription").optional().isString(),
	check("drugs").optional().isArray(),
	check("medicalTests").optional().isArray(),
	check("instruction").optional().isString(),
	check("reviewAfter").optional().isString(),
];

module.exports = createPrescription;
