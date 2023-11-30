const { check } = require("express-validator");

let fetchMedicalCondition = [
    check("conditionId", "String").notEmpty().trim(),
];

module.exports = fetchMedicalCondition;
