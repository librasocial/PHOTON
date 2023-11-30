package com.ssf.homevisit.datasource

import com.ssf.homevisit.models.AuthenticationResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalDataSourceImpl
@Inject constructor(
        private val sharedPreferences: SecureSharedPrefManager)
    : LocalDataSource {

    override fun getAuthToken(): AuthenticationResult? {
        return sharedPreferences.getAuthToken()
    }

    override fun saveAuthToken(authenticationResult: AuthenticationResult) {
        sharedPreferences.saveAuthToken(authenticationResult)
    }

    override fun clearAuthToken() {
        sharedPreferences.clearAuthToken()

    }
}