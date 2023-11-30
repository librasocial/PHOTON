const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");
const createEncounterValidator = require("../app/validators/createEncounterValidator");
const filterEncounterValidator = require("../app/validators/filterEncounterValidator");
const fetchEncounterValidator = require("../app/validators/fetchEncounterValidator");
const patchEncounterValidator = require("../app/validators/patchEncounterValidator");
const encounter = require("../app/controllers/encounter");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("", createEncounterValidator, authenticateToken, encounter.create);

router.post(
	"/filter",
	filterEncounterValidator,
	authenticateToken,
	encounter.filter
);

router.get(
	"/:encounterId",
	fetchEncounterValidator,
	authenticateToken,
	encounter.fetch
);

router.patch(
	"/:encounterId",
	patchEncounterValidator,
	authenticateToken,
	encounter.patch
);

module.exports = router;
