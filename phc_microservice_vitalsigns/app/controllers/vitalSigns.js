const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const VitalSignsModel = require("../models/vitalSigns");

module.exports = {
	async store(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const newVitals = new VitalSignsModel(req.body);

			newVitals.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};

			const dbResponse = await newVitals.save();

			res.send(dbResponse);
		} catch (error) {
			next(error);
		}
	},

	async filter(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const { UHId, encounterDate, encounterId, page } = req.query;

			if (!encounterDate && !encounterId && !UHId) {
				return next(
					ApiError.partialContent(
						"No or invalid value found",
						"encounterDate or encounterId or UHId"
					)
				);
			} else {
				let dbResponse;
				let count = 0;
				let respo = {};
				let givenDate = new Date(encounterDate);

				givenDate.setDate(givenDate.getDate() + 1);

				let filter = {}
				if (!page)
					page = 1
				const skip = (+page - 1) * 25;

				if (encounterId) {
					filter = { encounterId }
				}
				if (encounterDate) {
					filter.encounterDate = {
						$gte: new Date(encounterDate).toISOString(),
						$lt: givenDate.toISOString(),
					}
				}

				if (UHId) {
					filter.UHId = UHId
				}

				dbResponse = await VitalSignsModel.find(filter)
					.limit(25)
					.skip(skip);
				count = await VitalSignsModel.find(filter)
					.count();

				const meta = {
					totalPages: Math.ceil(count / 25),
					totalElements: count,
					number: 1,
					size: count,
				};

				respo.meta = meta;
				respo.data = dbResponse;

				res.send(respo);
			}
		} catch (error) {
			next(error);
		}
	},

	async fetch(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const { vitalSignId } = req.params;

			if (!vitalSignId) {
				return next(
					ApiError.partialContent("No or invalid value found", "vitalSignId")
				);
			} else {
				const dbResponse = await VitalSignsModel.findById(vitalSignId);

				if (!dbResponse) {
					return next(
						ApiError.notFound(
							`Vital Sign not found for id: ${vitalSignId}`,
							"vitalSignId"
						)
					);
				}

				res.send(dbResponse);
			}
		} catch (error) {
			next(error);
		}
	},

	async update(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const { vitalSignId } = req.params;

			if (!vitalSignId) {
				return next(
					ApiError.badRequest("vitalSignId not found", "vitalSignId")
				);
			}

			const dbResponse = await VitalSignsModel.findByIdAndUpdate(
				vitalSignId,
				req.body,
				{
					new: true,
				}
			);

			if (!dbResponse) {
				return next(
					ApiError.notFound(
						`VitalSign not found for _id ${vitalSignId}`,
						"vitalSignId"
					)
				);
			}

			res.send(dbResponse);
		} catch (error) {
			next(error);
		}
	},
};
