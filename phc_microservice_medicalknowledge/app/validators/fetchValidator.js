const { check } = require("express-validator");

let fetchValidator = [check("testId", "String").notEmpty().trim()];

module.exports = fetchValidator;
