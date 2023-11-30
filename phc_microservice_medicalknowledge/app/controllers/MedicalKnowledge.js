const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const MedicalKnowledgeModel = require("../models/MedicalKnowledge");

module.exports = {
	async create(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const medicalKnowledge = new MedicalKnowledgeModel(req.body);

			medicalKnowledge.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};

			const savedMedicalKnowledge = await medicalKnowledge.save();

			res.status(201).send(savedMedicalKnowledge);
		} catch (err) {
			next(err);
		}
	},
	async fetch(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const { testId } = req.params;

			if (!testId) {
				return next(ApiError.partialContent("testId is required", "testId"));
			} else {
				const data = await MedicalKnowledgeModel.findById(testId);

				if (!data) {
					return next(
						ApiError.notFound(
							`MedicalKnowledge not found for ${testId}`,
							"testId"
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
			const { testId } = req.params;

			if (!testId) {
				return next(ApiError.partialContent("testId is required", "testId"));
			} else {
				const updatedData = await MedicalKnowledgeModel.findByIdAndUpdate(
					testId,
					req.body,
					{ new: true }
				);

				if (!updatedData) {
					return next(
						ApiError.notFound(
							`MedicalKnowledge not found for ${testId}`,
							"testId"
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
			const { encounterDate, UHId, doctor, encounterId, page, size } =
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
			if (doctor) filter.doctor = doctor;
			if (encounterId) filter.encounterId = encounterId;

			const dets = await MedicalKnowledgeModel.find(filter)
				.limit(+size)
				.skip(skip);

			const count = await MedicalKnowledgeModel.countDocuments(filter);

			if (dets !== null) {
				const meta = {
					totalPages: +page ? Math.ceil(count / size) : 1,
					totalElements: count,
					number: +page,
					size: +size,
				};

				respo.meta = meta;
				respo.data = dets;

				res.status(200).json(respo);
			} else {
				next(ApiError.notFound("No Medical Knowledge found"));
			}
		} catch (error) {
			next(error);
		}
	},
};
