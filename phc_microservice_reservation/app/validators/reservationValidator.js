const { check } = require("express-validator");

let registerValidator = [
	check("Patient.patientId", "String").notEmpty().trim(),
	check("reservationFor", "String").notEmpty().trim(),
	// check("Patinet.dob", "ISO8601format").isISO8601("yyyy-mm-dd hh:mm:ss"),
];

module.exports = registerValidator;
