const { check } = require("express-validator");

let fetchDocument = [check("docCode", "String").notEmpty().trim()];

module.exports = fetchDocument;
