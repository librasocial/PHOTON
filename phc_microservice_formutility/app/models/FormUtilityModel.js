const mongoose = require("mongoose");
const Config = require("../../config/config");
const Audit = require("./Audit");

const DocumentSchema = new mongoose.Schema({
  docCode: String,
  name: String,
  docFile: String,
  fileLocation: String,
  language: String,
  parentDocId: String,
  hasSubDocs: Boolean,
  formItems: [
    {
      groupName: String,
      groupType: String,
      elements: [
        {
          title: String,
          sequence: Number,
        },
      ],
    },
  ],
  audit: {
    type: Audit,
    required: true,
  },
});

const DocumentModel = mongoose.model(
  Config.collection_names.FormUtility,
  DocumentSchema
);

module.exports = DocumentModel;
