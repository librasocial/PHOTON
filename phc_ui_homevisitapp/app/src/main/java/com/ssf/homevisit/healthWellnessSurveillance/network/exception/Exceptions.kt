package com.ssf.homevisit.healthWellnessSurveillance.network.exception

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)