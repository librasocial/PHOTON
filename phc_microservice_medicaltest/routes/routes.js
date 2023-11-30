const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const MedicalTest = require("../app/controllers/MedicalTest.js");
const storeMedicalTest = require("../app/validators/storeMedicalTest.js");
const fetchMedicalTest = require("../app/validators/fetchMedicalTest.js");
const patchMedicalTest = require("../app/validators/patchMedicalTest.js");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", storeMedicalTest, authenticateToken, MedicalTest.store);

router.get("/filter", authenticateToken, MedicalTest.filter);

router.get("/:testId", fetchMedicalTest, authenticateToken, MedicalTest.fetch);

router.patch(
	"/:testId",
	patchMedicalTest,
	authenticateToken,
	MedicalTest.update
);

module.exports = router;
