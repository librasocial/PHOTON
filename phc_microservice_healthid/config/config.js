const Config = {
  api_version: '0.0.50',
  collection_names : {
    health_id:'health_ids'
  },
  STEP_FUNCTION_ENDPOINT: process.env.STEP_FUNCTION_ENDPOINT,
  BASE_URL: process.env.BASE_URL || "https://dev-api-phcdt.sampoornaswaraj.org",
  patch_members:"member-svc/members",
  aws_bucket_object: "ssf-dev-assets",
  aws_region: "ap-south-1"
}
module.exports = Config
