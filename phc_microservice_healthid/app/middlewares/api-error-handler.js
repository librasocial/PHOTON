const ApiError = require('../errors/handler')

function apiErrorHandler(err, req, res, next) {

  if (err instanceof ApiError) {
    res.status(err.errors[0].errorCode).json(err.buildMessage());
    return;
  }

  res.status(500).json('Something went wrong')
}

module.exports = apiErrorHandler
