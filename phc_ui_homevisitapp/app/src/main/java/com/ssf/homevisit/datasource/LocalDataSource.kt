package com.ssf.homevisit.datasource

import com.ssf.homevisit.models.AuthenticationResult

interface LocalDataSource {

    fun getAuthToken(): AuthenticationResult?

    fun saveAuthToken(authenticationResult: AuthenticationResult)

    fun clearAuthToken()
}