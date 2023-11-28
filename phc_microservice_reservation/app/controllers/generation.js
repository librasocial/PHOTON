const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const reservationModel = require("../models/reservation");

module.exports = {
	async create(req, res, next) {
		const errors = validationResult(req);
		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}
		try {
			var newData = new reservationModel(req.body);
			newData.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};
			const ans = await newData.save();
			res.send(ans);
		} catch (err) {
			// res.send(err);
			next(err);
		}
	},

	async fetch(req, res, next) {
		const { providerId, status, date, page } = req.query;

		if (!date) {
			next(ApiError.partialContent("No or invalid value found", "date"));
			return;
		}

		try {
			let skip = 0;
			let limit = 0;
			if (page) {
				skip = (page - 1) * 25;
				limit = 25;
			}
			const respo = {};
			const filter = {};
			let count = 0;
			let reservation = {};

			// for filtering by status array
			let temp = "";
			if (status) {
				temp = status.substring(1, status.length - 1).split(",");
			}
			//end of cond for filter by status array

			let givenDate = new Date(date);
			
			givenDate.setDate(givenDate.getDate() + 1);

			if (date && providerId && status) {
				filter["Provider.memberId"] = providerId;
				reservation = await reservationModel
					.find({
						$and: [
							{
								reservationTime: {
									$gte: new Date(date).toISOString(),
									$lt: givenDate.toISOString(),
								},
							},
							filter,
							{ status: { $in: temp } },
						],
					})
					.limit(limit)
					.skip(skip);

				count = await reservationModel
					.find({
						$and: [
							{
								reservationTime: {
									$gte: new Date(date).toISOString(),
									$lt: givenDate.toISOString(),
								},
							},
							filter,
							{ status: { $in: temp } },
						],
					})
					.count();
			}

			if (date && providerId && !status) {
				filter["Provider.memberId"] = providerId;
				reservation = await reservationModel
					.find({
						$and: [
							{
								reservationTime: {
									$gte: new Date(date).toISOString(),
									$lt: givenDate.toISOString(),
								},
							},
							filter,
						],
					})
					.limit(limit)
					.skip(skip);

				count = await reservationModel
					.find({
						$and: [
							{
								reservationTime: {
									$gte: new Date(date).toISOString(),
									$lt: givenDate.toISOString(),
								},
							},
							filter,
						],
					})
					.count();
			}

			if (date && status && !providerId) {
				reservation = await reservationModel
					.find({
						$and: [
							{
								reservationTime: {
									$gte: new Date(date).toISOString(),
									$lt: givenDate.toISOString(),
								},
							},
							{ status: { $in: temp } },
						],
					})
					.limit(limit)
					.skip(skip);

				count = await reservationModel
					.find({
						$and: [
							{
								reservationTime: {
									$gte: new Date(date).toISOString(),
									$lt: givenDate.toISOString(),
								},
							},
							{ status: { $in: temp } },
						],
					})
					.count();
			}

			if (date && !providerId && !status) {
				reservation = await reservationModel
					.find({
						reservationTime: {
							$gte: new Date(date).toISOString(),
							$lt: givenDate.toISOString(),
						},
					})
					.limit(limit)
					.skip(skip);

				count = await reservationModel
					.find({
						reservationTime: {
							$gte: new Date(date).toISOString(),
							$lt: givenDate.toISOString(),
						},
					})
					.count();
			}

			if (reservation !== null) {
				const meta = {
					totalPages: +page ? Math.ceil(count / 25) : 1,
					totalElements: count,
					number: +page ? +page : 1,
					size: +page ? reservation.length : count,
				};

				respo.meta = meta;
				respo.data = reservation;

				res.send(respo);
				return;
			} else {
				next(
					ApiError.notFound(
						"Reservation not found for given parameters",
						"Invalid Inputs"
					)
				);
				return;
			}
		} catch (err) {
			// res.send(err);
			next(err);
		}
	},

	async filter(req, res, next) {
		const reservationId = req.params.reservationId;
		if (!reservationId) {
			return next(
				ApiError.badRequest("ReservationId not found", "ReservationId")
			);
		}
		try {
			const reservation = await reservationModel.findOne({
				_id: reservationId,
			});
			if (reservation !== null) {
				res.send(reservation);
			} else {
				next(
					ApiError.notFound(
						`Reservation not found for _id ${reservationId}`,
						"_id"
					)
				);
				return;
			}
		} catch (err) {
			// res.send(err);
			next(err);
		}
	},

	async patch(req, res, next) {
		const reservationId = req.params.reservationId;
		if (!reservationId) {
			return next(
				ApiError.badRequest("ReservationId not found", "reservationId")
			);
		}

		try {
			const fieldsToBeUpdatedArray = [
				"reservationFor",
				"reservationTime",
				"encounterId",
				"visitType",
				"status",
				"tokenNumber",
				"label",
			];
			let update = {};
			const body = req.body;

			fieldsToBeUpdatedArray.forEach((field) => {
				if (body[field] !== null) {
					update[field] = body[field];
				}
			});

			if (body.Patient) {
				Object.entries(body.Patient).forEach(([key, value]) => {
					update[`Patient.${key}`] = value;
				});
			}

			if (body.Provider) {
				Object.entries(body.Provider).forEach(([key, value]) => {
					update[`Provider.${key}`] = value;
				});
			}

			const updated = await reservationModel.findByIdAndUpdate(
				reservationId,
				update,
				{ new: true }
			);
			if (!updated) {
				next(
					ApiError.notFound(
						`Reservation not found for _id ${reservationId}`,
						"_id"
					)
				);
				return;
			}
			res.send(updated);
			return;
		} catch (err) {
			if (err.name === "CastError") {
				next(ApiError.badRequest(err.message, err.path));
			} else if (err.name === "ValidationError") {
				next(ApiError.mongooseValidationError(err));
			} else if (err.path === "_id") {
				next(ApiError.badRequest(err.message, err.path));
			} else {
				next(err);
			}
		}
	},
};
