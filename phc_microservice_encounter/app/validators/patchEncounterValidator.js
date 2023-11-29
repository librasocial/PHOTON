const { check } = require("express-validator");

let patchEncounterValidator = [
	check("patientId", "String").optional().isString(),
	check("purpose", "String").optional().isString(),
	check("attendantName", "String").optional().isString(),
];

module.exports = patchEncounterValidator;
