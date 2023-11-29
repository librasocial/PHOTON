package com.ssf.homevisit.healthWellnessSurveillance.di

import com.ssf.homevisit.healthWellnessSurveillance.data.repImpl.GetSurveillanceRepo
import com.ssf.homevisit.healthWellnessSurveillance.data.repImpl.VillageSurveyRepo
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IVillageSurvey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoProvider {

    @Binds
    abstract fun provideGetLarvaSurveillance(repo: GetSurveillanceRepo): IGetSurveillance

    @Binds
    abstract fun provideSurveyResponse(repo: VillageSurveyRepo): IVillageSurvey
}