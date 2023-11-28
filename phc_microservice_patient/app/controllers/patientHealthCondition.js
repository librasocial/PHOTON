const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const patientHealthConditionModel = require("../models/patient_health_condition");

module.exports = {
	async store(req, res, next) {
		const errors = validationResult(req);
		if (!errors.isEmpty()) {
			return next(ApiError.expressValidationFailed(errors.errors));
		}

		const id = req.params.patientId;

		if (!id) {
			return next(ApiError.badRequest("PatientId not found", "patientId"));
		}
		try {
			const ans = new patientHealthConditionModel(req.body);
			ans.audit = {
				dateCreated: Date.now(),
				createdBy: req.authorization.username,
				dateModified: Date.now(),
				modifiedBy: req.authorization.username,
			};
			const dbResponse = await ans.save();
			dbResponse.healthConditions[0].conditionId = dbResponse._id;
			const final = await dbResponse.save();
			// console.log(final);
			res.send(final);
		} catch (error) {
			// res.send(error);
			next(error);
		}
	},

	async fetch(req, res, next) {
		const patientId = req.params.patientId;
		if (!patientId) {
			return next(ApiError.badRequest("PatiendId not found", "patientId"));
		}
		try {
			const patient = await patientHealthConditionModel.findOne({
				patientId: patientId,
			});
			if (patient !== null) {
				const data = [];
				const respo = {};
				const meta = {
					totalPages: 1,
					totalElements: 1,
					number: 1,
					size: 1,
				};

				data.push(patient);

				respo.meta = meta;
				respo.data = data;

				res.send(respo);
			} else {
				next(
					ApiError.notFound(`Patient not found for _id ${patientId}`, "_id")
				);
				return;
			}
		} catch (err) {
			// res.send(err);
			next(err);
		}
	},

	async patch(req, res, next) {
		const patientId = req.params.patientId;
		const conditionId = req.params.conditionId;

		if (!patientId) {
			return next(ApiError.badRequest("PatiendId not found", "patientId"));
		}
		if (!conditionId) {
			return next(ApiError.badRequest("PatiendId not found", "conditionId"));
		}
		try {
			const updated = await patientHealthConditionModel.findOneAndUpdate(
				{ patientId: patientId },
				req.body,
				{
					new: true,
				}
			);
			if (!updated) {
				next(
					ApiError.notFound(`Patient not found for _id ${patientId}`, "_id")
				);
				return;
			}

			res.send(updated);
		} catch (err) {
			if (err.name === "CastError") {
				next(ApiError.badRequest(err.message, err.path));
			} else if (err.name === "ValidationError") {
				next(ApiError.mongooseValidationError(err));
			} else if (err.path === "_id") {
				next(ApiError.badRequest(err.message, err.path));
			} else {
				// console.log(err);
				next(err);
			}
		}
	},
};
