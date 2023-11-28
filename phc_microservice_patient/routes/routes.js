const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const patients = require("../app/controllers/patients");
const patientHealthCondition = require("../app/controllers/patientHealthCondition");
const registerValidator = require("../app/validators/patientValidator");
const retrievePatientValidator = require("../app/validators/retrievePatientValidator");
const searchPatientValidator = require("../app/validators/searchPatientValidator");
const patchPatientValidator = require("../app/validators/patchPatientValidator");
const createHealthConditionValidator = require("../app/validators/healthConditionValidator");
const retrieveHealthConditionValidator = require("../app/validators/retrieveHealthCondition");
const patchHealthConditionValidator = require("../app/validators/patchHealthConditionValidator");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", registerValidator, authenticateToken, patients.store);

router.post(
	"/:patientId/healthConditions",
	createHealthConditionValidator,
	authenticateToken,
	patientHealthCondition.store
);

router.get("/filter", authenticateToken, patients.filter);

router.get(
	"/search",
	searchPatientValidator,
	authenticateToken,
	patients.search
);

router.get(
	"/:patientId",
	retrievePatientValidator,
	authenticateToken,
	patients.fetch
);

router.get(
	"/:patientId/healthConditions",
	retrieveHealthConditionValidator,
	authenticateToken,
	patientHealthCondition.fetch
);

router.patch(
	"/:patientId",
	patchPatientValidator,
	authenticateToken,
	patients.patch
);

router.patch(
	"/:patientId/healthConditions/:conditionId",
	patchHealthConditionValidator,
	authenticateToken,
	patientHealthCondition.patch
);

module.exports = router;
