class ApiError {
  constructor(code, message) {
    this.code = code
    this.message = message
  }

  static badRequest(msg){
    return new ApiError(400, msg)
  }

  static unauthorized(msg){
    return new ApiError(401, msg)
  }

  static notFound(msg){
    return new ApiError(404, msg)
  }

  static partialContent(msg){
    return new ApiError(206, msg)
  }
}

module.exports = ApiError
