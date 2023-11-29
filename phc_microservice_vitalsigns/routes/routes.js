const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");

const VitalSigns = require("../app/controllers/vitalSigns");
const createVitalsValidator = require("../app/validators/createVitalsValidator");
const filterVitalsValidator = require("../app/validators/filterVitalsValidator");
const fetchVitalsValidator = require("../app/validators/fetchVitalValidator");

const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", createVitalsValidator, authenticateToken, VitalSigns.store);

router.get(
	"/filter",
	filterVitalsValidator,
	authenticateToken,
	VitalSigns.filter
);

router.get(
	"/:vitalSignId",
	fetchVitalsValidator,
	authenticateToken,
	VitalSigns.fetch
);

router.patch("/:vitalSignId", authenticateToken, VitalSigns.update);

module.exports = router;
