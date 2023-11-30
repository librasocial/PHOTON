const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const encounterModel = require("../models/encounter");
const appLogger = require("../../config/logger.js");

module.exports = {
	async create(req, res, next) {
		if (req.body.UHID) {
			return ApiError.badRequest("UHID is not a valid key")
		}

			const errors = validationResult(req);
			if (!errors.isEmpty()) {
				return next(ApiError.expressValidationFailed(errors.errors));
			}
			try {
				var newData = new encounterModel(req.body);
				newData.audit = {
					dateCreated: Date.now(),
					createdBy: req.authorization.username,
					dateModified: Date.now(),
					modifiedBy: req.authorization.username,
				};
				const ans = await newData.save();
				res.send(ans);
			} catch (err) {
				res.status(500).json(err);
			}
	},

	async filter(req, res, next) {
		const { purpose, patientId, UHId, encounterDate } = req.body;

		try {
			let foundEncounter;
			let filter = {};
			if (encounterDate) {
				// filter.encounterDate = encounterDate;
			}
			if (purpose) {
				filter.purpose = purpose;
			}

			if (patientId) {
				filter.patientId = patientId
			}

			if (UHId) {
				filter.UHId = UHId
			}

			foundEncounter = await encounterModel
				.find(filter)
				.sort({ "audit.dateCreated": -1 });

			res.send(foundEncounter);
		} catch (error) {
			appLogger.error("filtering error", error);
			next(error);
		}

	},

	async fetch(req, res, next) {
		const encounterId = req.params.encounterId;

		if (!encounterId) {
			return next(ApiError.badRequest("Encounter not found", "encounterId"));
		}

		try {
			const encounter = await encounterModel.findOne({ _id: encounterId });
			if (encounter !== null) {
				res.send(encounter);
			} else {
				next(
					ApiError.notFound(`Encounter not found for _id ${encounterId}`, "_id")
				);
				return;
			}
		} catch (err) {
			// res.send(err);
			next(err);
		}
	},

	async patch(req, res, next) {
		const encounterId = req.params.encounterId;

		if (!encounterId) {
			return next(ApiError.badRequest("Encounter not found", "encounterId"));
		}

			const errors = validationResult(req);
			if (!errors.isEmpty()) {
				return next(ApiError.expressValidationFailed(errors.errors));
			}
			try {
				const fieldsToBeUpdatedArray = [
					"UHId",
					"citizenId",
					"patientId",
					"purpose",
					"visitType",
					"attendantName",
					"attendantPhone",
					"reservationFlag",
				];

				let update = {};
				const body = req.body;

				fieldsToBeUpdatedArray.forEach((field) => {
					if (body[field] !== null) {
						update[field] = body[field];
					}
				});

				if (body.assignedTo) {
					Object.entries(body.assignedTo).forEach(([key, value]) => {
						update[`assignedTo.${key}`] = value;
					});
				}

				if (body.referredByAshaWorker) {
					Object.entries(body.referredByAshaWorker).forEach(([key, value]) => {
						update[`referredByAshaWorker.${key}`] = value;
					});
				}

				if (body.referredByDoctor) {
					Object.entries(body.referredByDoctor).forEach(([key, value]) => {
						update[`referredByDoctor.${key}`] = value;
					});
				}

				var newData = await encounterModel.findOneAndUpdate(
					{ _id: encounterId },
					update,
					{
						new: true,
					}
				);
				if (!newData) {
					next(
						ApiError.notFound(
							`Encounter not found for _id ${encounterId}`,
							"_id"
						)
					);
					return;
				}
				res.send(newData);
			} catch (err) {
				next(err);
			}
	},
};
