let { check } = require("express-validator");

const postValidator = [
  check("patientId").optional().isString(),
  check("UHId").optional().isString(),
  check("encounterId").optional().isString(),
  check("encounterDate").optional().isISO8601(),
  check("recordedBy").optional().isString(),
  check("doctor").optional().isString(),
  check("category").optional().isString(),
  check("allergen").optional().isString(),
  check("reactionType").optional().isString(),
  check("reaction").optional().isString(),
  check("confirmationType").optional().isString(),
  check("dateSince").optional().isString(),
  check("onsetDate").optional().isISO8601(),
  check("status").optional().isString(),
  check("severity").optional().isString(),
  check("infoSource").optional().isString(),
  check("reactionSite").optional().isString(),
  check("relievingFactor").optional().isString(),
  check("closureDate").optional().isISO8601(),
];

module.exports = postValidator;
