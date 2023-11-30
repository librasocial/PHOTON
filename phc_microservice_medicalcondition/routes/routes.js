const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const MedicalCondition = require("../app/controllers/MedicalConditions");
const createMedicalCondition = require("../app/validators/createMedicalCondition");
const fetchMedicalCondition = require("../app/validators/fetchMedicalCondition");
const patchMedicalCondition = require("../app/validators/patchMedicalCondition");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post(
	"/",
	createMedicalCondition,
	authenticateToken,
	MedicalCondition.store
);

router.get("/filter", authenticateToken, MedicalCondition.filter);

router.get(
	"/:conditionId",
	fetchMedicalCondition,
	authenticateToken,
	MedicalCondition.fetch
);

router.patch(
	"/:conditionId",
	patchMedicalCondition,
	authenticateToken,
	MedicalCondition.update
);

module.exports = router;