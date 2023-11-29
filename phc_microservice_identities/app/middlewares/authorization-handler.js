const ApiError = require("../errors/handler");

class Middleware {
  /** Authenticate Token */
  static authenticateToken(req, res, next) {
    const authHeader = req.headers["authorization"];

    try{
      const token = authHeader.split(" ")[1];
      if (token === null) {
        next(ApiError.unauthorized('No authorization'))
        return;
      }
      req.user = JSON.parse(Buffer.from(token.split('.')[1], 'base64').toString('binary'))
      if (req.user._id === undefined) {
        next(ApiError.unauthorized('cannot validate auth token'))
        return;
      }
    }catch(err){
      next(ApiError.unauthorized('cannot validate auth token'))
      return;
    }
    next()

  }
}

module.exports = Middleware
