const { check } = require('express-validator')

const patchValidator = [
  check('patientId').optional().isString(),
  check('UHId').optional().isString(),
  check('encounterId').optional().isString(),
  check('encounterDate').optional().isISO8601(),
  check('doctor').optional().isString(),
  check('type').optional().isString(),
  check('remarks').optional().isString(),
  check('referTo').optional().isString(),
  check('nextVisit').optional().isString()
]

module.exports = patchValidator
