const express = require("express");
const router = express.Router();
const { api_version } = require("../config/config");
const reservation = require("../app/controllers/generation");
const registerValidator = require("../app/validators/reservationValidator");
const filterValidator = require("../app/validators/filterValidator");
const filterIdValidator = require("../app/validators/filterIdValidator");
const patchReservationValidator = require("../app/validators/patchValidator");
const {
	authenticateToken,
} = require("../app/middlewares/authorization-handler");

router.get("/api_ver", async (req, res) => {
	res.send(`here is response for you Api version ${api_version}`);
});

router.post(
	"/",
	registerValidator,
	authenticateToken,
	reservation.create
);

router.get(
	"/filter",
	filterValidator,
	authenticateToken,
	reservation.fetch
);

router.get(
	"/:reservationId",
	filterIdValidator,
	authenticateToken,
	reservation.filter
);

router.patch(
	"/:reservationId",
	patchReservationValidator,
	authenticateToken,
	reservation.patch
);

module.exports = router;
