/*
|--------------------------------------------------------------------------
| Validating Environment Variables
|--------------------------------------------------------------------------
|
| In this file we define the rules for validating environment variables.
| By performing validation we ensure that your application is running in
| a stable environment with correct configuration values.
|
| This file is read automatically by the framework during the boot lifecycle
| and hence do not rename or move this file to a different location.
|
*/

const dotenv = require("dotenv");
dotenv.config();
module.exports = {
  AWSClientId: process.env.AWSClientId,
  AWSPoolId: process.env.AWSPoolId,
  AWSRegion: process.env.AWSRegion,
  AWSIdentityPoolId: process.env.IdentityPoolId,
  AWSIdentityId: process.env.IdentityId,
  AWSUserPoolId: process.env.AWSUserPoolId
}