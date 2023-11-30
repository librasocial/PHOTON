const { check } = require("express-validator");

let fetchFamilyMemberHistory = [check("historyId", "String").notEmpty().trim()];

module.exports = fetchFamilyMemberHistory;
