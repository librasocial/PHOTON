package com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface

import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse.BaseResponse
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.*
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface IGetSurveillance {

    suspend fun getPlace(villageUuid: String,assetType:String,placeRequest: PlaceRequest): Flow<DataState<BaseResponse<List<Place>>>>
    suspend fun checkLarvaCreated(checkIfSurveillanceIsCreated: CheckIfSurveillanceIsCreated): Flow<DataState<IsSurveillanceCreated>>
    suspend fun getLarvaSurveillanceVisitsByFilter(surveillanceId: String): Flow<DataState<LarvaVisitModelResponse>>
    suspend fun getLarvaByFilter(
        villageId: String?=null,
        placeOrgId: String?=null,
        householdId: String?=null,
        page: Int,
        size: Int,
        placeType: String?=null
    ): Flow<DataState<IsSurveillanceCreated>>

    suspend fun saveSurveillanceVisit(
        saveSurveillance: SaveSurveillance
    ): Flow<DataState<IsSurveillanceCreated>>

    suspend fun getImageUrl(bucketKey: String): Flow<DataState<PresignedUrl>>

    suspend fun getPlaceImageUrl(bucketKey: String): Flow<DataState<PresignedUrl>>

    suspend fun createWaterSampleSurveillance(waterSampleData: WaterSampleContent,userId: String): Flow<DataState<WaterSampleResponse>>

    suspend fun updateWaterSampleSurveillance(waterSampleData: WaterSampleContent,userId: String): Flow<DataState<WaterSampleResponse>>

    suspend fun getWaterSampleByFilter(
        dateOfSurvey: String?,
        villageId: String?,
        placeOrgId: String?, page: Int, size: Int, placeType: String?
    ): Flow<DataState<WaterSampleResponse>>

    suspend fun getWaterSampleById(
        waterSampleId: String,
        userId: String
    ): Flow<DataState<WaterSampleResponse>>

    suspend fun createIodineSurveillance(
        iodineData: CheckIfSurveillanceIsCreated,
        userId: String
    ): Flow<DataState<CreateSurveillanceResponse>>

    suspend fun getIodineSurveillanceByFilter(
        checkIfSurveillanceIsCreated: CheckIfSurveillanceIsCreated,
        userId: String
    ): Flow<DataState<CreateSurveillanceResponse>>

    suspend fun createIodineSample(
        iodineSampleData: CreateIodineSample, userId: String
    ): Flow<DataState<IodineSampleResponse>>

    suspend fun updateIodineSample(
        iodineSampleData: CreateIodineSample, userId: String,iodineSampleId:String
    ): Flow<DataState<IodineSampleResponse>>

    suspend fun getIodineSampleByFilter(
        userId: String, iodineSurveillanceId: String
    ): Flow<DataState<IodineSampleResponse>>

    suspend fun getHouseholdNearVillage(villageId: String): Flow<DataState<HouseHoldResponse>>

    suspend fun getHouseHoldByName(
        villageLgdCode: String, name: String
    ): Flow<DataState<HouseHoldResponseByName>>

    suspend fun getHouseHoldNearMe(
        villageId: String, latitude: Double, longitude: Double
    ): Flow<DataState<NearByHouseHoldResponse>>

    suspend fun getChildrenInVillage(villageId: String,requestData: ChildrenRequestData): Flow<DataState<ChildrenResponse>>

    suspend fun getPreSignedUrl(bucketKey: String): Flow<DataState<PresignedUrl>>

    suspend fun putImageToS3Bucket(
        preSignedUrl: String, contentType: String, body: RequestBody
    ): Flow<DataState<Unit>>

    suspend fun saveCovidData(requestData: CovidRequestData): Flow<DataState<SaveIndividualDataResponse>>

    suspend fun saveLeprosyData(requestData: LeprosyRequestData): Flow<DataState<SaveIndividualDataResponse>>

    suspend fun saveUrineData(requestData: UrineRequestData): Flow<DataState<SaveIndividualDataResponse>>

    suspend fun saveAcfData(requestData: AcfRequestData): Flow<DataState<SaveIndividualDataResponse>>

    suspend fun saveBloodSmearData(requestData: BloodSmearRequestData): Flow<DataState<SaveIndividualDataResponse>>

    suspend fun saveIdspData(requestData: IdspData): Flow<DataState<SaveIndividualDataResponse>>

    suspend fun getIndividualSurveillanceData(flowType: String): Flow<DataState<Response>>

    suspend fun getCitizensInHouse(houseUuid: String): Flow<DataState<IndividualResponse>>

    suspend fun updateAcfData(requestData: AcfRequestData, surveillanceId: String): Flow<DataState<AcfRequestData>>

    suspend fun updateLeprosyData(
        requestData: LeprosyRequestData, surveillanceId: String
    ): Flow<DataState<LeprosyRequestData>>

    suspend fun updateUrineData(
        requestData: UrineRequestData, surveillanceId: String
    ): Flow<DataState<UrineRequestData>>

    suspend fun updateBloodSmearData(
        requestData: BloodSmearUpdateRequestData, surveillanceId: String
    ): Flow<DataState<BloodSmearUpdateRequestData>>

    suspend fun getUrineData(citizenId: String): Flow<DataState<GetUrineFilterData>>

    suspend fun getLeprosyData(citizenId: String): Flow<DataState<GetLeprosyFilterData>>

    suspend fun getBloodSmearData(citizenId: String): Flow<DataState<GetBloodFilterData>>

    suspend fun getAcfData(citizenId: String): Flow<DataState<GetAcfFilterData>>

    suspend fun getLabResultStatus(url:String, citizenIdList: String):Flow<DataState<List<LabResultStatusResponse>>>

//    suspend fun getAcfLabResultStatus(citizenIdList:List<String>):Flow<DataState<LabResultStatusResponse>>
//
//    suspend fun getUrineLabResultStatus(citizenIdList:List<String>):Flow<DataState<LabResultStatusResponse>>
//
//    suspend fun getBloodSmearResultStatus(citizenIdList:List<String>):Flow<DataState<LabResultStatusResponse>>
}