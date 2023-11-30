package com.ssf.homevisit.di

import androidx.lifecycle.MutableLiveData
import com.ssf.homevisit.features.hnwvillage.data.HnWRepository
import com.ssf.homevisit.features.hnwvillage.data.IHnWRepository
import com.ssf.homevisit.features.hnwvillage.data.SurveillanceDataType
import com.ssf.homevisit.requestmanager.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module()
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideHnWRepo(iHnWRepository: HnWRepository, apiInterface: ApiInterface): IHnWRepository {
        return HnWRepository(apiInterface)
    }

    @Provides
    fun provideLiveData(): MutableLiveData<List<SurveillanceDataType>> {
        return MutableLiveData()
    }

}