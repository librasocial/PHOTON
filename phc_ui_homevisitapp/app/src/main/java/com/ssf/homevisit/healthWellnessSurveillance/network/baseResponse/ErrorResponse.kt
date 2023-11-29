package com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class ErrorResponse(

    @SerializedName("code")
    var code: Int,

    @SerializedName("is_error")
    var isError: Boolean,

    @SerializedName("message")
    var message: String = ""
)