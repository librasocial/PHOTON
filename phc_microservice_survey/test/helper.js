const fs = require('fs').promises;
const SurveyMasterModel = require("../app/models/survey-master");

async function loadData(filePath) {
  const data = await fs.readFile(filePath);
  await SurveyMasterModel(JSON.parse(data.toString())).save()
}

module.exports = { loadData }
