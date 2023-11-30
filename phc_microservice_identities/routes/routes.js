const express = require("express");
const { api_version } = require("../config/config");
const { authenticateToken } = require("../app/middlewares/authorization-handler");
const UsersController = require("../app/controllers/users");
const router = express.Router()

router.get('/api_ver', async (req, res) => {
  res.send(`here is response for you Api version ${api_version}`);
})

router.post('/identities', UsersController.store)
router.get('/identities/:IdentityId', UsersController.fetch)
router.patch('/identities/:IdentityId', UsersController.patch)

module.exports = router
