package com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("is_error")
    var isError: Boolean,

    @SerializedName("message")
    var message: String = "",

    @SerializedName("data")
    var data: T
)