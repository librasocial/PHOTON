package com.ssf.homevisit.healthWellnessSurveillance.network.safeApiRequest

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse.ErrorResponse
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.network.exception.NoInternetException
import com.ssf.homevisit.healthWellnessSurveillance.network.networkHelper.NetworkHelper
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

enum class ErrorCodes(val code: Int) {
    SocketTimeOut(-1)
}

open class SafeApiRequest
@Inject constructor(
    private val networkHelper: NetworkHelper
) {
    suspend fun <T : Any> apiRequest(dataRequest: suspend () -> T): DataState<T> {
        return try {
            if (networkHelper.isNetworkConnected()) {
                DataState.Success(dataRequest.invoke())
            } else {
                val throwable = NoInternetException("Please check your Internet Connection")
                DataState.Error(throwable, throwable.message.toString())
            }

        } catch (throwable: Throwable) {
            when (throwable) {
                is HttpException -> {
                    val errorResponse = convertErrorBody(throwable)
                    DataState.Error(
                        throwable,
                        errorMessage = errorResponse?.message.toString()
                    )
                }
                is SocketTimeoutException -> DataState.Error(
                    throwable,
                    getErrorMessage(ErrorCodes.SocketTimeOut.code)
                )

                else -> {
                    DataState.Error(throwable as Exception, throwable.message.toString())
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): ErrorResponse? {
    return try {
        throwable.response()?.errorBody()?.let {

            val gson = Gson()
            val type = object : TypeToken<ErrorResponse>() {}.type
            val errorResponse: ErrorResponse = gson.fromJson(it.string(), type)
            errorResponse
        }
    } catch (exception: Exception) {
        ErrorResponse(throwable.code(), true, getErrorMessage(throwable.code()))
    }
}

private fun getErrorMessage(code: Int): String {
    return when (code) {
        ErrorCodes.SocketTimeOut.code -> "Timeout"
        400 -> "Bad Request"
        401 -> "Unauthorised"
        403 -> "Forbidden"
        404 -> "Not found"
        409 -> "Conflict"
        500 -> "Internal Server Error"
        else -> "Something went wrong"
    }
}