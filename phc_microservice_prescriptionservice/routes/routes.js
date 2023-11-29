const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");
const Prescription = require("../app/controllers/Prescription");
const createPrescription = require("../app/validators/createPrescription");
const fetchPrescription = require("../app/validators/fetchPrescription");
const patchPrescription = require("../app/validators/patchPrescription");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post("/", createPrescription, authenticateToken, Prescription.create);

router.get("/filter", authenticateToken, Prescription.filter);

router.get(
	"/:prescriptionId",
	fetchPrescription,
	authenticateToken,
	Prescription.fetch
);

router.patch(
	"/:prescriptionId",
	patchPrescription,
	authenticateToken,
	Prescription.update
);

module.exports = router;
