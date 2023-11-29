const ApiError = require('../errors/handler')

class Middleware {
  /** Authenticate Token */
  static authenticateToken (req, res, next) {
    if (process.env.NODE_ENV === 'test') {
      req.authorization = { username: 'test' }
      next()
      return
    }

    const authHeader = req.headers.authorization

    try {
      const token = authHeader.split(' ')[1]
      if (token === null) {
        next(ApiError.unauthorized('No authorization'))
        return
      }
      req.authorization = JSON.parse(
        Buffer.from(token.split('.')[1], 'base64').toString('binary')
      )
      // check if exp is after now

      if (req.authorization.exp < new Date().getSeconds()) {
        next(ApiError.unauthorized('Token Expired'))
        return
      }

      if (!req.authorization.username) {
        next(ApiError.unauthorized('cannot find username in auth token'))
        return
      }
    } catch (err) {
      next(ApiError.unauthorized('cannot validate auth token'))
      return
    }
    next()
  }
}

module.exports = Middleware
