const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const {
  authenticateToken,
} = require("../app/middlewares/authorization-handler");
const FormUtility = require("../app/controllers/FormUtility");
const fetchDocument = require("../app/validators/fetchValidator");

router.get("/api_ver", async (req, res) => {
  res.send(`here is response for you Api version ${api_version}`);
});

router.get("/:docCode", fetchDocument, authenticateToken, FormUtility.fetch);

module.exports = router;
