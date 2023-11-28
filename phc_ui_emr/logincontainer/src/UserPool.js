import { CognitoUserPool } from "amazon-cognito-identity-js";

const poolData = {
  // UserPoolId: "us-east-1_qbDuE37RH",
  // ClientId: "5rvkpmb6hj4l3e2igpuqe61bv0"
  UserPoolId: "ap-south-1_iicZFXp9O",
  ClientId: "3v9smbj1tdsgoukfjf4ltnj07c",
};

export default new CognitoUserPool(poolData);
