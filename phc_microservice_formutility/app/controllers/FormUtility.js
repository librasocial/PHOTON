const { validationResult } = require("express-validator");
const ApiError = require("../errors/handler");
const FormUtility = require("../models/FormUtilityModel");

module.exports = {
  async fetch(req, res, next) {
    const errors = validationResult(req);

    if (!errors.isEmpty()) {
      return next(ApiError.expressValidationFailed(errors.errors));
    }

    try {
      const { docCode } = req.params;
      if (!docCode) {
        return next(ApiError.partialContent("docCode is required", "docCode"));
      } else {
        const data = await FormUtility.findOne({ docCode: docCode });
        if (!data) {
          return next(
            ApiError.notFound(`Document not found for ${docCode}`, "docCode")
          );
        } else {
          res.status(200).json(data);
        }
      }
    } catch (error) {
      console.log(error);
      next(error);
    }
  },
};
