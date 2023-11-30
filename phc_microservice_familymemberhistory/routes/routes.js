const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");
const FamilyMemberHistoryController = require("../app/controllers/FamilyMemberHistoryController");
const postValidator = require("../app/validators/postValidator");
const fetchFamilyMemberHistory = require("../app/validators/fetchValidator");
const patchValidator = require("../app/validators/patchValidator");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post(
	"/",
	postValidator,
	authenticateToken,
	FamilyMemberHistoryController.create
);

router.get("/filter", authenticateToken, FamilyMemberHistoryController.filter);

router.get(
	"/:historyId",
	fetchFamilyMemberHistory,
	authenticateToken,
	FamilyMemberHistoryController.fetch
);

router.patch(
	"/:historyId",
	patchValidator,
	authenticateToken,
	FamilyMemberHistoryController.update
);

module.exports = router;
