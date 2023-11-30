const { check } = require("express-validator");

const patchMedicalCondition = [
	check("encounterId").optional().isString(),
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("recordedBy").optional().isString(),
	check("doctor").optional().isString(),
	check("effectiveDate").optional().isISO8601(),
	check("effectivePeriod").optional().isString(),
	check("complaintText").optional().isString(),
];

module.exports = patchMedicalCondition;
