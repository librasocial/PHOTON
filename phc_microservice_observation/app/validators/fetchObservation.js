const { check } = require("express-validator");

let fetchObservation = [check("observationId", "String").notEmpty().trim()];

module.exports = fetchObservation;
