let { check } = require("express-validator");

let storeMedicalTest = [
	check("patientId").optional().isString(),
	check("UHId").optional().isString(),
	check("encounterId").optional().isString(),
	check("effectiveDate").optional().isISO8601(),
	check("doctor").optional().isString(),
	check("tests").isArray().isLength({ min: 1 }),
];

module.exports = storeMedicalTest;