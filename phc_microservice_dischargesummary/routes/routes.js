const express = require('express')
const router = express.Router()
const { apiVersion } = require('../config/config')
const {
  authenticateToken
} = require('../app/middlewares/authorization-handler')
const DischargeSummary = require('../app/controllers/DischargeController')
const postValidator = require('../app/validators/postValidator')
const fetchValidator = require('../app/validators/fetchValidator')
const patchValidator = require('../app/validators/patchValidator')

router.get('/api_ver', async (req, res) => {
  res.send(`here is response for your Api version ${apiVersion}`)
})

router.post('/', postValidator, authenticateToken, DischargeSummary.create)

router.get('/filter', authenticateToken, DischargeSummary.filter)

router.get(
  '/:historyId',
  fetchValidator,
  authenticateToken,
  DischargeSummary.fetch
)

router.patch(
  '/:historyId',
  patchValidator,
  authenticateToken,
  DischargeSummary.update
)

module.exports = router
