const { check } = require('express-validator')

const fetchValidator = [
  check('historyId').notEmpty().trim().isMongoId()
]

module.exports = fetchValidator
