const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");
const MedicalKnowledge = require("../app/controllers/MedicalKnowledge");
const postValidator = require("../app/validators/postValidator");
const fetchValidator = require("../app/validators/fetchValidator");
const patchValidator = require("../app/validators/patchValidator");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", postValidator, authenticateToken, MedicalKnowledge.create);

router.get("/filter", authenticateToken, MedicalKnowledge.filter);

router.get(
	"/:testId",
	fetchValidator,
	authenticateToken,
	MedicalKnowledge.fetch
);

router.patch(
	"/:testId",
	patchValidator,
	authenticateToken,
	MedicalKnowledge.update
);

module.exports = router;
