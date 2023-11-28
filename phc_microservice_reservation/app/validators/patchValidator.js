const { check } = require("express-validator");

let patchReservationValidator = [
	check("Patient.patientId", "String").optional().isString(),
	check("Patient.name", "String").optional().isString(),
	check("Provider.memberId", "String").optional().isString(),
	check("tokenNumber", "String").optional().isString(),
];

module.exports = patchReservationValidator;
