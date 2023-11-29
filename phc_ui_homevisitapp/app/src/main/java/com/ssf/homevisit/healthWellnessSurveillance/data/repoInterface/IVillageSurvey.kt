package com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface

import com.ssf.homevisit.healthWellnessSurveillance.data.SaveSurvey
import com.ssf.homevisit.healthWellnessSurveillance.data.Source
import com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse.BaseResponse
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import kotlinx.coroutines.flow.Flow

interface IVillageSurvey {

    suspend fun saveSurveyResponse(saveSurvey: SaveSurvey): Flow<DataState<BaseResponse<SaveSurvey>>>
    suspend fun villageSurveyFormResponse(surveyName: String): Flow<DataState<BaseResponse<com.ssf.homevisit.healthWellnessSurveillance.data.SurveyFormResponse?>>>

    suspend fun fetchStateByCountry():Flow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>
    suspend fun fetchDistrictByState(srcId: String):Flow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>

    suspend fun fetchTalukByDistrict(srcId:String):Flow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>

    suspend fun fetchGramPanchayatByTaluk(srcId: String):Flow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>
}
