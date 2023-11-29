const { check } = require("express-validator");

let fetchMedicalTest = [check("testId", "String").notEmpty().trim()];

module.exports = fetchMedicalTest;
