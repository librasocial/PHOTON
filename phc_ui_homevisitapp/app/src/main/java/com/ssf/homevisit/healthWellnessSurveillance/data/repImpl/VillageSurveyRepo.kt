package com.ssf.homevisit.healthWellnessSurveillance.data.repImpl

import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.data.Target
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IVillageSurvey
import com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse.BaseResponse
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.network.safeApiRequest.SafeApiRequest
import com.ssf.homevisit.requestmanager.ApiService
import com.ssf.homevisit.utils.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class VillageSurveyRepo @Inject constructor(
        private val apiService: ApiService, private val safeApiRequest: SafeApiRequest
) : IVillageSurvey {

    override suspend fun saveSurveyResponse(saveSurvey: SaveSurvey): Flow<DataState<BaseResponse<SaveSurvey>>> = flow {
        emit(DataState.Loading)
        emit(
                safeApiRequest.apiRequest {
                    val data = apiService.saveSurvey(
                            authorization = "Bearer " + Util.getHeader(),
                            surveyId = saveSurvey.surveyId,
                            saveSurvey = saveSurvey
                    )
                    BaseResponse(isError = false, data = data)
                }
        )
    }

    override suspend fun villageSurveyFormResponse(surveyName: String): Flow<DataState<BaseResponse<SurveyFormResponse?>>> =
            flow {
                emit(DataState.Loading)
                emit(safeApiRequest.apiRequest {
                    val data = apiService.surveyFormResponse(
                            surveyType = surveyName,
                            surveyName = "Village",
                            authorization = "Bearer " + Util.getHeader()
                    )
                    if (data.data.isNotEmpty()) {
                        BaseResponse(false, data = data.data[0])
                    } else {
                        BaseResponse(false, data = null)
                    }
                })
            }

    override suspend fun fetchStateByCountry(): Flow<DataState<BaseResponse<List<Target>>>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            val data = apiService.fetchStateByCountry(
                    authorization = "Bearer " + Util.getHeader(),
                    srcType = "Country",
                    targetType = "State",
                    srcNodeId = "df2f5468-55ca-4fcd-a2d3-f47639ea6373",
                    rel = "CONTAINEDINPLACE"
            ).contents.map { it.target }
            BaseResponse(false, data = data)
        })
    }

    override suspend fun fetchDistrictByState(srcId: String): Flow<DataState<BaseResponse<List<Target>>>> =flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            val data = apiService.fetchDistrictByState(
                    authorization = "Bearer " + Util.getHeader(),
                    srcType = "State",
                    targetType = "District",
                    srcNodeId = srcId,
                    rel = "CONTAINEDINPLACE",
                    size = 250
            ).contents.map { it.target }
            BaseResponse(false, data = data)
        })
    }

    override suspend fun fetchTalukByDistrict(srcId:String): Flow<DataState<BaseResponse<List<Target>>>> =flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            val data = apiService.fetchTalukBYDistrict(
                    authorization = "Bearer " + Util.getHeader(),
                    srcType = "District",
                    targetType = "Taluk",
                    srcNodeId = srcId,
                    rel = "CONTAINEDINPLACE"
            ).contents.map { it.target }
            BaseResponse(false, data = data)
        })
    }

    override suspend fun fetchGramPanchayatByTaluk(srcId: String): Flow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            val data = apiService.fetchGramPanchayatByTaluk(
                    authorization = "Bearer " + Util.getHeader(),
                    srcType = "Taluk",
                    targetType = "GramPanchayat",
                    srcNodeId = srcId,
                    rel = "ADMINISTEREDBY"
            ).contents.map { it.target }
            BaseResponse(false, data = data)
        })
    }
}
