package com.ssf.homevisit.healthWellnessSurveillance.network.retrofitIntepceptor

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
) :
    Interceptor {
    @SuppressLint("HardwareIds")
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        run {
            request.header("Accept", "*/*")
        }

        return chain.proceed(request.build())
    }
}