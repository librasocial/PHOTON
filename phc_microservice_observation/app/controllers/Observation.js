const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const ObservationModel = require("../models/Observation");

module.exports = {
	async store(req, res, next) {
		const errors = validationResult(req);

		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		try {
			const Observation = new ObservationModel(req.body);

			Observation.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};

			const updatedObservation = await Observation.save();

			res.status(200).json(updatedObservation);
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
			const { observationId } = req.params;

			if (!observationId) {
				return next(
					ApiError.partialContent("observationId is required", "observationId")
				);
			} else {
				const observation = await ObservationModel.findOne({
					_id: observationId,
				});

				if (!observation) {
					return next(
						ApiError.notFound(
							`Physical Examination not found for ${observationId}`,
							"observationId"
						)
					);
				} else {
					res.status(200).json(observation);
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
			const { observationId } = req.params;

			if (!observationId) {
				return next(
					ApiError.partialContent("observationId is required", "observationId")
				);
			}

			const fieldsToUpdateArray = [
				"citizenId",
				"patientId",
				"UHId",
				"encounterId",
				"doctor",
			];

			let update = {};

			fieldsToUpdateArray.forEach((field) => {
				if (req.body[field]) {
					update[field] = req.body[field];
				}
			});

			if (req.body.assessments) {
				update.assessments = [];
				for (let i = 0; i < req.body.assessments.length; i++) {
					if(req.body.assessments[i].assessmentTitle != "") {
						for(let k=0; k<req.body.assessments[i].observations.length; k++) {
							if(req.body.assessments[i].observations[k].observationName=="") {
								req.body.assessments[i].observations.splice(k, 1);
							}
						}
						update.assessments.push(req.body.assessments[i])
					}				
				}
			}

			const observation = await ObservationModel.findByIdAndUpdate(
				observationId,
				update,
				{ new: true }
			);

			if (!observation) {
				return next(
					ApiError.notFound(
						`Physical Examination not found for ${observationId}`,
						"observationId"
					)
				);
			}

			res.status(200).json(observation);
		} catch (error) {
			console.log(error);
			next(error);
		}
	},
	async filter(req, res, next) {
		const { encounterDate, UHId, doctor, encounterId, type, page, size } =
			req.query;

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
			if (type) filter.type = type;

			const observation = await ObservationModel.find(filter)
				.limit(+size)
				.skip(skip);

			const count = await ObservationModel.countDocuments(filter);

			if (observation !== null) {
				const meta = {
					totalPages: +page ? Math.ceil(count / size) : 1,
					totalElements: count,
					number: +page,
					size: +size,
				};

				respo.meta = meta;
				respo.data = observation;

				res.status(200).json(respo);
			} else {
				next(ApiError.notFound("No observations found"));
			}
		} catch (error) {
			next(error);
		}
	},
};
