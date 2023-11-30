const AWS = require("aws-sdk");
const config = require("../config/config")
const regionConst = 'ap-south-1';
// Set the region
AWS.config.update({ region: 'ap-south-1' });
AWS.config.update({ signatureVersion: 'v4' });

// Create S3 service object
const s3 = new AWS.S3();
module.exports = s3;
