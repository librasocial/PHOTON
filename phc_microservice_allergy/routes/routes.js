const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");
const Allergy = require("../app/controllers/Allergy");
const postValidator = require("../app/validators/postValidator");
const fetchAllergy = require("../app/validators/fetchValidator");
const patchValidator = require("../app/validators/patchValidator");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", postValidator, authenticateToken, Allergy.create);

router.get("/filter", authenticateToken, Allergy.filter);

router.get("/:allergyId", fetchAllergy, authenticateToken, Allergy.fetch);

router.patch("/:allergyId", patchValidator, authenticateToken, Allergy.update);

module.exports = router;
