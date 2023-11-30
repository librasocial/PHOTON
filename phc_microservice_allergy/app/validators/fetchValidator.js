const { check } = require("express-validator");

let fetchAllergy = [
    check("allergyId", "String").notEmpty().trim(),
];

module.exports = fetchAllergy;
