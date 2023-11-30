const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const Observation = require("../app/controllers/Observation");
const storeObservation = require("../app/validators/storeObservation");
const fetchObservation = require("../app/validators/fetchObservation");
const patchObservation = require("../app/validators/patchObservation");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", storeObservation, authenticateToken, Observation.store);

router.get("/filter", authenticateToken, Observation.filter);

router.get(
	"/:observationId",
	fetchObservation,
	authenticateToken,
	Observation.fetch
);

router.patch(
	"/:observationId",
	patchObservation,
	authenticateToken,
	Observation.update
);

module.exports = router;
