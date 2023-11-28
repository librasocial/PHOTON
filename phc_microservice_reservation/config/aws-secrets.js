const AWS = require("aws-sdk");
const appLogger = require("./logger.js");

const loadSecrets = (region, SecretId) =>
	new Promise((resolve, reject) => {
        AWS.config.update({
          credentials: new AWS.Credentials({
            accessKeyId: "AKIAR2KY2Z47FONLTPZX",
            secretAccessKey: "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",
            region: process.env.region
          })
        });
		const secretsManager = new AWS.SecretsManager({ region });

		secretsManager.getSecretValue({ SecretId }, (err, data) => {
			const rejectOn = [
				"DecryptionFailureException",
				"InternalServiceErrorException",
				"InvalidParameterException",
				"InvalidRequestException",
				"ResourceNotFoundException",
			];

			if (err && rejectOn.indexOf(err.code)) {
				reject(err);
				appLogger.error(" Secrets Error ", err.code);
			}
			if (data !== null) resolve(data.SecretString);
		});
	});

const loadAWSJSONSecretsIntoENV = (region, secretId, logFn = () => {}) =>
	loadSecrets(region, secretId).then((secrets) => {
		var secrets = JSON.parse(secrets);
		for (const key in secrets) {
			if (!process.env[key]) {
				let val = secrets[key].trim();
				if (val === "false") val = false;
				if (val === "true") val = true;
				process.env[key] = val;
				logFn(`env ${key} loaded from AWS Secrets Manager`);
			} else {
				logFn(
					`env ${key} loaded from AWS but not used because it is already set`
				);
			}
		}
	});

module.exports = loadAWSJSONSecretsIntoENV;
