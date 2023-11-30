const { check } = require("express-validator");

let fetchPrescriptionValidator = [
	check("prescriptionId", "String").notEmpty().trim(),
];

module.exports = fetchPrescriptionValidator;
