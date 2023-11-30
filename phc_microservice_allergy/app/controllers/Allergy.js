const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const AllergyModel = require("../models/AllergyModel");

module.exports = {
	async create(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

        try {
                                  		console.log("Entering into try black")
                                  			const receivedData = new AllergyModel(req.body);

                                            			receivedData.audit = {
                                            				dateCreated: Date.now(),
                                            				createdBy: req.authorization.username,
                                            				dateModified: Date.now(),
                                            				modifiedBy: req.authorization.username,
                                            			};
                                            console.log(`receivedData : ${receivedData}`)
                                            console.log("Going to save the data")
                                  			const newData = await receivedData.save();
                                  			console.log("Saved the data")
                                            console.log(`Saved the data into DB : ${newData}`)
                                  			res.send(newData);
                                  		} catch (error) {
                                  		console.log(`Error : ${error}`)
                                  			next(error);
                                  		}
	},
	async fetch(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const { allergyId } = req.params;

			if (!allergyId) {
				return next(
					ApiError.partialContent("allergyId is required", "allergyId")
				);
			} else {
				const data = await AllergyModel.findById(allergyId);

				if (!data) {
					return next(
						ApiError.notFound(`Allergy not found for ${allergyId}`, "allergyId")
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
			const { allergyId } = req.params;

			if (!allergyId) {
				return next(
					ApiError.partialContent("allergyId is required", "allergyId")
				);
			} else {
				const updatedData = await AllergyModel.findByIdAndUpdate(
					allergyId,
					req.body,
					{ new: true }
				);

				if (!updatedData) {
					return next(
						ApiError.notFound(`Allergy not found for ${allergyId}`, "allergyId")
					);
				} else {
					res.status(200).json(updatedData);
				}
			}
		} catch (error) {
			console.log(error);
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

			const allergy = await AllergyModel.find(filter)
				.limit(+size)
				.skip(skip);

			const count = await AllergyModel.countDocuments(filter);

			if (allergy !== null) {
				const meta = {
					totalPages: +page ? Math.ceil(count / size) : 1,
					totalElements: count,
					number: +page,
					size: +size,
				};

				respo.meta = meta;
				respo.data = allergy;

				res.status(200).json(respo);
			} else {
				next(ApiError.notFound("No Allergy found"));
			}
		} catch (error) {
			next(error);
		}
	},
};
