package com.ssf.homevisit.repository

import com.ssf.homevisit.datasource.LocalDataSourceImpl
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class MainRepository @Inject constructor(private val dataSourceImpl: LocalDataSourceImpl) {

    fun clearLoginAuth() {
        dataSourceImpl.clearAuthToken()
    }
}