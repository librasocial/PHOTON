const express = require("express");
const SurveyController = require("../app/controllers/survey-controller");
const SurveyResponseController = require("../app/controllers/survey-response-controller");
const { authenticateToken } = require("../app/middlewares/authorization-handler");
const { api_version } = require("../config/config");
const router = express.Router()
const config = require('../config/config');
const NCDResponseController = require("../app/controllers/ncd-response-controller");

router.get('/api_ver', async (req, res) => {
  res.send(`here is response for you Api version ${api_version}`);
})

router.post('/:surveyId/responses',authenticateToken,SurveyResponseController.store)
router.get('/:surveyId/responses',authenticateToken,SurveyResponseController.fetch)

router.post('/ncd/:surveyId/responses',authenticateToken,NCDResponseController.store)
router.get('/ncd/:surveyId/responses',authenticateToken,NCDResponseController.fetch)
router.get('/ncd/filter', authenticateToken, NCDResponseController.getSurveyRecord)

router.get('/filter',authenticateToken,SurveyController.filter)
router.get('/filter/:contextId', authenticateToken, SurveyResponseController.getSurveyRecord)
router.get('/:surveyId',authenticateToken, SurveyController.fetch)
module.exports = router
