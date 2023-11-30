const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const MedicalConditionModel = require("../models/MedicalCondition");

module.exports = {
	async store(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const medicalCondition = new MedicalConditionModel(req.body);

			medicalCondition.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};

			const updatedMedicalCondition = await medicalCondition.save();

			res.status(200).json(updatedMedicalCondition);
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
			const { conditionId } = req.params;

			if (!conditionId) {
				return next(
					ApiError.partialContent("conditionId is required", "conditionId")
				);
			} else {
				const medicalCondition = await MedicalConditionModel.findOne({
					$or: [{ _id: conditionId }, { UHId: conditionId }]
				});

				if (!medicalCondition) {
					return next(
						ApiError.notFound(
							`Chief Complaint not found for ${conditionId}`,
							"conditionId"
						)
					);
				} else {
					res.status(200).json(medicalCondition);
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
			const { conditionId } = req.params;

			if (!conditionId) {
				return next(
					ApiError.partialContent("conditionId is required", "conditionId")
				);
			}
			const newData = await MedicalConditionModel.findOneAndUpdate(
				{ $or: [{ _id: conditionId }, { UHId: conditionId }] },
				req.body,
				{ new: true }
			);

			if (!newData) {
				next(
					ApiError.notFound(
						`Medical condition not found for _id ${conditionId}`,
						"_id"
					)
				);
				return;
			}

			res.status(200).json(newData);
		} catch (error) {
			next(error);
		}
	},

	async filter(req, res, next) {
		const { encounterDate, UHId, doctor, encounterId, page, size } = req.query;

		try {
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
			if (doctor) filter.doctor = doctor;
			if (encounterId) filter.encounterId = encounterId;

			const medicalConditions = await MedicalConditionModel.find(filter)
				.limit(+size)
				.skip(skip);

			const count = await MedicalConditionModel.countDocuments(filter);

			if (medicalConditions !== null) {
				const meta = {
					totalPages: +page ? Math.ceil(count / size) : 1,
					totalElements: count,
					number: +page,
					size: +size,
				};

				respo.meta = meta;
				respo.data = medicalConditions;

				res.status(200).json(respo);
			} else {
				next(ApiError.notFound("No medical conditions found"));
			}
		} catch (error) {
			next(error);
		}
	},
};
