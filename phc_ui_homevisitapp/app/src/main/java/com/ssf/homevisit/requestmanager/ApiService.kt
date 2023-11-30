package com.ssf.homevisit.requestmanager

import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.*
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("/organization-svc/organizations/relationships/filter")
    suspend fun getPlace(
            @Body placeRequest: PlaceRequest,
            @Header("Authorization") token: String
    ): PlaceResponse

    @GET("/surveillance-svc/surveillance/larvas/{surveillanceId}/visits/filter")
    suspend fun getLarvaSurveillanceVisitsByFilter(
            @Path("surveillanceId") surveillanceId: String,
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Header("Authorization") authorization: String
    ): LarvaVisitModelResponse

    @POST("/surveillance-svc/surveillance/larvas")
    suspend fun checkLarvaCreatedByFilter(
            @Header("Authorization") authorization: String,
            @Header("accept") accept: String = "*/*",
            @Header("Content-Type") ContentType: String = "application/json",
            @Body checkIfSurveillanceIsCreated: CheckIfSurveillanceIsCreated,
    ): IsSurveillanceCreated

    @GET("surveillance-svc/surveillance/larvas/filter")
    suspend fun getLarvaByFilter(
            @Query("villageId") villageId: String?,
            @Query("placeOrgId") placeOrgId: String?,
            @Query("householdId") householdId: String?,
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Query("placeType") placeType: String?,
            @Header("Authorization") authorization: String,
            @Header("accept") accept: String = "*/*",
            @Header("x-user-id") userId: String = "",
            @Header("Content-Type") ContentType: String = "application/json",
    ): IsSurveillanceCreated

    @GET("/organization-svc/organizations/relationships/filter")
    suspend fun fetchDivisionList(
            @Header("Authorization") authorization: String,
            @Query("srcType") srcType: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Query("srcNodeId") srcNodeId: String?
    ): PlaceResponse

    @GET("/surveys/filter")
    suspend fun surveyFormResponse(
            @Query("surveyType") surveyType: String,
            @Query("surveyName") surveyName: String,
            @Header("Authorization") authorization: String
    ): SurveyFormData

    @POST("surveys/{surveyId}/responses")
    suspend fun saveSurvey(
            @Header("Authorization") authorization: String,
            @Path("surveyId") surveyId: String,
            @Body saveSurvey: SaveSurvey
    ): SaveSurvey

    @POST("/surveillance-svc/surveillance/larvas/{surveillanceId}/visits")
    suspend fun saveSurveillanceVisit(
            @Header("Authorization") authorization: String,
            @Header("accept") accept: String = "*/*",
            @Path("surveillanceId") surveillanceId: String,
            @Header("Content-Type") ContentType: String = "application/json",
            @Body saveSurveillance: SaveSurveillance
    ): IsSurveillanceCreated

    // water sample
    @POST("/surveillance-svc/surveillance/watersamples")
    suspend fun createWaterSampleSurveillance(
            @Header("Authorization") authorization: String, @Header("x-user-id") userId: String,
            @Body waterRequest: WaterSampleContent
    ): WaterSampleResponse

    @PATCH("/surveillance-svc/surveillance/watersamples/{id}")
    suspend fun updateWaterSampleSurveillance(
            @Header("Authorization") authorization: String, @Header("x-user-id") userId: String,
            @Path("id") surveillanceId: String,
            @Body waterRequest: WaterSampleContent
    ): WaterSampleResponse

    @GET("/surveillance-svc/surveillance/watersamples/{id}")
    suspend fun getWaterSampleById(
            @Header("Authorization") authorization: String,
            @Header("x-user-id") userId: String,
            @Path("id") id: String
    ): WaterSampleResponse

    @GET("/surveillance-svc/surveillance/watersamples/filter")
    suspend fun getWaterSampleByFilter(
            @Query("dateOfSurvey") dateOfSurvey: String?,
            @Query("villageId") villageId: String?,
            @Query("placeOrgId") placeOrgId: String?,
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Query("placeType") placeType: String?,
            @Header("Authorization") token: String
    ): WaterSampleResponse

    // iodine flow

    @POST("/surveillance-svc/surveillance/iodines")
    suspend fun createIodineSurveillance(
            @Body data: CheckIfSurveillanceIsCreated,
            @Header("x-user-id") userId: String,
            @Header("Authorization") token: String
    ): CreateSurveillanceResponse

    @GET("surveillance-svc/surveillance/iodines/filter")
    suspend fun getIodineSurveillanceByFilter(
            @Query("villageId") villageId: String?,
            @Query("placeOrgId") placeOrgId: String?,
            @Query("householdId") householdId: String?,
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Query("placeType") placeType: String?,
            @Header("Authorization") token: String,
            @Header("x-user-id") userId: String
    ): CreateSurveillanceResponse


    // iodineSample
    @POST("/surveillance-svc/surveillance/iodines/{id}/samples")
    suspend fun createIodineSample(
            @Body CreateIodineSample: CreateIodineSample,
            @Path("id") iodineSurveillanceId: String,
            @Header("x-user-id") userId: String,
            @Header("Authorization") token: String
    ): IodineSampleResponse

    @PATCH("/surveillance-svc/surveillance/iodines/{id}/samples/{sampleId}")
    suspend fun updateIodineSample(
            @Body CreateIodineSample: CreateIodineSample,
            @Path("id") iodineSurveillanceId: String,
            @Path("sampleId") iodineSampleId: String,
            @Header("x-user-id") userId: String,
            @Header("Authorization") token: String
    ): IodineSampleResponse

    @GET("/surveillance-svc/surveillance/iodines/{id}/samples/filter")
    suspend fun getIodineSampleByFilter(
            @Path("id") id: String,
            @Query("page") page: Int,
            @Header("x-user-id") userId: String,
            @Query("size") size: Int,
            @Header("Authorization") token: String
    ): IodineSampleResponse

    @GET("/member-svc/members/getimageurl")
    suspend fun getImageUrl(
            @Query("bucketKey") bucketKey: String,
            @Header("Authorization") authorization: String,
    ): PresignedUrl

    @GET("/organization-svc/organizations/getimageurl")
    suspend fun getPlaceImageUrl(
            @Query("bucketKey") bucketKey: String,
            @Header("Authorization") authorization: String,
    ): PresignedUrl


    @GET("/organization-svc/organizations/relationships/filter")
    suspend fun getHouseHoldNearVillage(
            @Query("srcType") srcType: String,
            @Query("srcNodeId") srcNodeId: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Header("Authorization") authorization: String,

            ): HouseHoldResponse


    @POST("/organization-svc/organizations/search")
    suspend fun getHouseHoldByName(
            @Header("Authorization") authorization: String,
            @Body body: HouseHoldByNameRequest,
            @Query("query") name: String,
            @Query("type") type: String
    ): HouseHoldResponseByName

    @POST("/organization-svc/organizations/nearme")
    suspend fun getHouseHoldNearMe(
            @Header("Authorization") authorization: String, @Body body: HouseHoldByNameRequest
    ): NearByHouseHoldResponse

    @POST("/member-svc/members/relationships/filter")
    suspend fun getChildrenInVillage(
            @Header("Authorization") authorization: String,
            @Body body: ChildrenRequestData
    ): ChildrenResponse

    @GET("/organization-svc/organizations/getprefetchedurl")
    suspend fun getPreSignedUrl(
            @Query("bucketKey") bucketKey: String, @Header("Authorization") token: String
    ): PresignedUrl


    @PUT
    suspend fun putImageToS3(
            @Url uploadUrl: String, @Header("Content-Type") contentType: String, @Body body: RequestBody
    ): Unit

    /// individual
    @POST("/covidsurveillance-svc/covidsurveillances")
    suspend fun saveCovidData(
            @Body requestData: CovidRequestData,
            @Header("Authorization") token: String
    ): SaveIndividualDataResponse

    @POST("/leprosysurveillance-svc/leprosySurveillances")
    suspend fun saveLeprosyData(
            @Body requestData: LeprosyRequestData,
            @Header("Authorization") token: String
    ): SaveIndividualDataResponse

    @POST("/urinesurveillance-svc/urinesamplesurveillances")
    suspend fun saveUrineData(
            @Body requestData: UrineRequestData,
            @Header("Authorization") token: String
    ): SaveIndividualDataResponse

    @POST("/tbsurveillance-svc/tbsurveillance")
    suspend fun saveAcfData(
            @Body requestData: AcfRequestData,
            @Header("Authorization") token: String
    ): SaveIndividualDataResponse


    @POST("/bssurveillance-svc/bloodsmearsurveillances")
    suspend fun saveBloodSmearData(
            @Body requestData: BloodSmearRequestData,
            @Header("Authorization") token: String
    ): SaveIndividualDataResponse

    @POST("/idsp-surveillance-svc/idspsurveillances")
    suspend fun saveIdspData(
            @Body requestData: IdspData,
            @Header("Authorization") token: String
    ): SaveIndividualDataResponse

    @GET("/formutility/{flowType}")
    suspend fun getIndividualData(
            @Path("flowType") flowType: String,
            @Header("Authorization") authorization: String,
    ): Response

    @GET("/member-svc/members/relationships/filter")
    suspend fun getCitizensInHouseHold(
            @Query("srcType") srcType: String,
            @Query("srcNodeId") srcNodeId: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Query("page") page: Int,
            @Query("size") size: Int,
            @Header("Authorization") authorization: String,
    ): IndividualResponse

    @PATCH("/bssurveillance-svc/bloodsmearsurveillances/{surveillanceId}")
    suspend fun updateBloodSmear(
            @Path("surveillanceId") surveillanceId: String,
            @Body body: BloodSmearUpdateRequestData,
            @Header("Authorization") authorization: String,
    ): BloodSmearUpdateRequestData

    @PATCH("/leprosysurveillance-svc/leprosySurveillances/{surveillanceId}")
    suspend fun updateLeprosy(
            @Path("surveillanceId") surveillanceId: String,
            @Body body: LeprosyRequestData,
            @Header("Authorization") authorization: String,
    ): LeprosyRequestData

    @PATCH("urinesurveillance-svc/urinesamplesurveillances/{surveillanceId}")
    suspend fun updateUrine(
            @Path("surveillanceId") surveillanceId: String,
            @Body body: UrineRequestData,
            @Header("Authorization") authorization: String,
    ): UrineRequestData

    @PATCH("/tbsurveillance-svc/tbsurveillance/{surveillanceId}")
    suspend fun updateAcf(
            @Path("surveillanceId") surveillanceId: String,
            @Body body: AcfRequestData,
            @Header("Authorization") authorization: String,
    ): AcfRequestData


    @GET("/leprosysurveillance-svc/leprosySurveillances/filter")
    suspend fun getLeprosyData(
            @Query("citizenId") citizenId: String,
            @Header("Authorization") authorization: String,
    ): GetLeprosyFilterData

    @GET("/bssurveillance-svc/bloodsmearsurveillances/filter")
    suspend fun getBloodSmearData(
            @Query("citizenId") citizenId: String,
            @Header("Authorization") authorization: String,
    ): GetBloodFilterData

    @GET("/urinesurveillance-svc/urinesamplesurveillances/filter")
    suspend fun getUrineData(
            @Query("citizenId") citizenId: String,
            @Header("Authorization") authorization: String,
    ): GetUrineFilterData

    @GET("/tbsurveillance-svc/tbsurveillance/filter")
    suspend fun getAcfData(
            @Query("citizenId") citizenId: String,
            @Header("Authorization") authorization: String,
    ): GetAcfFilterData

    @GET
    suspend fun getLabResultStatus(
            @Url url: String,
            @Header("Authorization") authorization: String,
            @Query("citizenIds") citizenId: String
    ): List<LabResultStatusResponse>


    @GET("/organization-svc/organizations/relationships/filter")
    suspend fun fetchTalukBYDistrict(
            @Query("srcType") srcType: String,
            @Query("srcNodeId") srcNodeId: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Header("Authorization") authorization: String,

            ): PlaceResponse


    @GET("/organization-svc/organizations/relationships/filter")
    suspend fun fetchStateByCountry(
            @Query("srcType") srcType: String,
            @Query("srcNodeId") srcNodeId: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Header("Authorization") authorization: String,

            ): PlaceResponse

    @GET("/organization-svc/organizations/relationships/filter")
    suspend fun fetchDistrictByState(
            @Query("srcType") srcType: String,
            @Query("srcNodeId") srcNodeId: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Header("Authorization") authorization: String,
            @Query("size") size: Int
    ): PlaceResponse

    @GET("/organization-svc/organizations/relationships/filter")
    suspend fun fetchGramPanchayatByTaluk(
            @Query("srcType") srcType: String,
            @Query("srcNodeId") srcNodeId: String,
            @Query("rel") rel: String,
            @Query("targetType") targetType: String,
            @Header("Authorization") authorization: String,

            ): PlaceResponse

}
