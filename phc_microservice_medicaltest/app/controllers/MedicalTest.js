const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const MedicalTestModel = require("../models/MedicalTest");

module.exports = {
	async store(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const MedicalTest = new MedicalTestModel(req.body);

			MedicalTest.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};

			const updatedMedicalTest = await MedicalTest.save();

			res.status(200).json(updatedMedicalTest);
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
			const { testId } = req.params;

			if (!testId) {
				return next(ApiError.partialContent("testId is required", "testId"));
			} else {
				const MedicalTest = await MedicalTestModel.findOne({
					_id: testId,
				});

				if (!MedicalTest) {
					return next(
						ApiError.notFound(`Investigation not found for ${testId}`, "testId")
					);
				} else {
					res.status(200).json(MedicalTest);
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
			}
			const newData = await MedicalTestModel.findOneAndUpdate(
				{ _id: testId },
				req.body,
				{ new: true }
			);

			if (!newData) {
				next(
					ApiError.notFound(`MedicalTest not found for _id ${testId}`, "_id")
				);
				return;
			}

			res.status(200).json(newData);
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
			const { UHId, encounterId, encounterDate, doctor, page, size } =
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

			if (UHId) filter.UHId = UHId;
			if (encounterId) filter.encounterId = encounterId;
			if (doctor) filter.doctor = doctor;
			if (encounterDate) {
				let givenDate = new Date(encounterDate);
				givenDate.setDate(givenDate.getDate() + 1);
				filter.encounterDate = {
					$gte: new Date(encounterDate).toISOString(),
					$lt: givenDate.toISOString(),
				};
			}

			const MedicalTest = await MedicalTestModel.find(filter)
				.limit(+size)
				.skip(skip);

			const count = await MedicalTestModel.countDocuments(filter);

			if (!MedicalTest) {
				return next(ApiError.notFound("MedicalTests not found", "MedicalTest"));
			} else {
				const meta = {
					totalPages: +page ? Math.ceil(count / size) : 1,
					totalElements: count,
					number: +page,
					size: +size,
				};

				respo.meta = meta;
				respo.data = MedicalTest;

				res.status(200).json(respo);
			}
		} catch (error) {
			next(error);
		}
	},
};
