const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const FamilyMemberHistoryModel = require("../models/FamilyMemberHistory");

module.exports = {
	async create(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const receivedData = new FamilyMemberHistoryModel(req.body);

			receivedData.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};

			const newData = await receivedData.save();

			res.status(201).json(newData);
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
			const { historyId } = req.params;

			if (!historyId) {
				return next(
					ApiError.partialContent("historyId is required", "historyId")
				);
			} else {
				const data = await FamilyMemberHistoryModel.findById(historyId);

				if (!data) {
					return next(
						ApiError.notFound(
							`FamilyMemberHistory not found for ${historyId}`,
							"historyId"
						)
					);
				} else {
					res.status(200).json(data);
				}
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
			const { historyId } = req.params;

			if (!historyId) {
				return next(
					ApiError.partialContent("historyId is required", "historyId")
				);
			} else {
				const fieldsToUpdateArray = [
					"patientId",
					"UHId",
					"encounterId",
					"encounterDate",
					"doctor",
				];

				let update = {};

				fieldsToUpdateArray.forEach((field) => {
					if (req.body[field]) {
						update[field] = req.body[field];
					}
				});

				if (req.body.conditions) {
					for (let i = 0; i < req.body.conditions.length; i++) {
						Object.entries(req.body.conditions[i]).forEach(([key, value]) => {
							update[`conditions.${i}.${key}`] = value;
						});
					}
				}

				const updatedData = await FamilyMemberHistoryModel.findByIdAndUpdate(
					historyId,
					update,
					{ new: true }
				);

				if (!updatedData) {
					return next(
						ApiError.notFound(
							`FamilyMemberHistory not found for ${historyId}`,
							"historyId"
						)
					);
				} else {
					res.status(200).json(updatedData);
				}
			}
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
			const { encounterDate, UHId, doctor, encounterId, type, page, size } =
				req.query;

			if (isNaN(+size)) {
				next(ApiError.partialContent("No or invalid value found", "size"));
				return;
			}
			if (isNaN(+page)) {
				next(ApiError.partialContent("No or invalid value found", "page"));
				return;
			}

			const skip = (page - 1) * size;

			const filter = {};
			const respo = {};

			if (encounterDate) {
				let givenDate = new Date(encounterDate);
				givenDate.setDate(givenDate.getDate() + 1);
				filter.encounterDate = {
					$gte: new Date(encounterDate).toISOString(),
					$lt: givenDate.toISOString(),
				};
			}
			if (UHId) filter.UHId = UHId;
			if (doctor) filter.prescribedBy = doctor;
			if (encounterId) filter.encounterId = encounterId;
			if (type) filter.type = type;

			const familyMemberHistory = await FamilyMemberHistoryModel.find(filter)
				.limit(+size)
				.skip(skip);

			const count = await FamilyMemberHistoryModel.countDocuments(filter);

			if (familyMemberHistory !== null) {
				const meta = {
					totalPages: +page ? Math.ceil(count / size) : 1,
					totalElements: count,
					number: +page,
					size: +size,
				};

				respo.meta = meta;
				respo.data = familyMemberHistory;

				res.status(200).json(respo);
			} else {
				next(ApiError.notFound("No Prescription found"));
			}
		} catch (error) {
			next(error);
		}
	},
};
