class ApiError {
	constructor(code, message, field, description) {
		this.description = description;
		this.errors = [];
		if (message !== null) {
			this.errors.push({
				errorCode: code,
				field: field,
				message: message,
			});
		}
	}

	pushError(code, field, message) {
		this.errors.push({
			errorCode: code,
			field: field,
			message: message,
		});
	}

	static badRequest(msg, field) {
		return new ApiError(400, msg, field, "Bad Request");
	}

	static unauthorized(msg) {
		return new ApiError(
			401,
			msg,
			"Authorization",
			"Authorization token is required in headers"
		);
	}

	static notFound(msg, field) {
		return new ApiError(404, msg, field, "Cannot find the requested data");
	}

	static partialContent(msg, field) {
		return new ApiError(206, msg, field, "Partial Content was found");
	}

	static expressValidationFailed(errors) {
		const apiError = new ApiError(400, null, null, "Validation failed");
		errors.forEach((err) => {
			apiError.pushError(400, err.param, err.msg);
		});

		return apiError;
	}

	static mongooseValidationError(error) {
		const apiError = new ApiError(null, null, null, "Validation Failed");
		for (const entry of Object.entries(error.errors)) {
			apiError.pushError(206, entry[0], entry[1].properties.message);
		}
		return apiError;
	}

	static databaseRequestTimeout(error) {
		return new ApiError(500, error.message, null, "Database Connection Error");
	}

	buildMessage() {
		return {
			description: this.description,
			errors: this.errors,
		};
	}
}

module.exports = ApiError;
