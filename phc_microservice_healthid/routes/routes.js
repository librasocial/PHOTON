const express = require("express");
const { api_version } = require("../config/config");
const HealthIds = require('../app/controllers/health-ids')
const { authenticateToken } = require("../app/middlewares/authorization-handler");
const router = express.Router()

router.get('/api_ver', async (req, res) => {
  res.send(`here is response for you Api version ${api_version}`);
})

router.post('/', authenticateToken, HealthIds.store )

router.patch('/:health_id',  authenticateToken, HealthIds.patch)

router.post('/filter',authenticateToken, HealthIds.filter)

router.get('/auth',authenticateToken, HealthIds.abdmAuth)

router.get('/presignedUrl',authenticateToken, HealthIds.presignedUrl)

router.get('/imageUrl',authenticateToken, HealthIds.imageUrl)

router.get('/:health_id',  authenticateToken, HealthIds.fetch)
module.exports = router
