package com.ssf.homevisit.requestmanager;

import com.google.gson.JsonObject;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.rmncha.childCare.immunization.Immunization;
import com.ssf.homevisit.utils.Util;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

    Util utils = null;

    @POST
    Call<CognitoResponse> getLoginTokenResponse(@Body JsonObject jsonObject,
                                                @Header("Content-Type") String contentType,
                                                @Header("X-Amz-Target") String X_Amz_Target,
                                                @Url String url);

    //    @GET("/organization-svc/PHCs/")
//    Call<AllPhcResponse>getAllPhcResponse(@Query("page") int page,
//                                          @Query("size") int size,
//                                          @Header("Authorization") String token);
    @GET("/member-svc/members/{path}")
    Call<AllPhcResponse> getAllPhcResponse(@Path("path") String path,
                                           @Header("Authorization") String token
    );
    @GET("/member-svc/members/{path}")
    Call<AllPhcResponse> getUserData(@Path("path") String path,
                                     @Header("Authorization") String token
    );

    @POST("/organization-svc/organizations/search")
    Call<SearchHouseholdResponse> searchByHouseId(@Query("query") String query,
                                                  @Body SearchHouseholdBody searchHouseholdBody,
                                                  @Header("Authorization") String token);


    @GET("/organization-svc/PHCs")
    Call<PhcResponse> getPhc(@Query("page") String page,
                             @Query("size") String size,
                             @Header("Authorization") String token);


    @GET("/organization-svc/organizations/relationships/filter")
    Call<SubcentersFromPHCResponse> getSubcentersFromPhc(@Query("srcType") String srcType,
                                                         @Query("rel") String rel,
                                                         @Query("srcNodeId") String srcNodeId,
                                                         @Query("targetType") String targetType,
                                                         @Header("Authorization") String token);

    @GET("/organization-svc/organizations/relationships/filter")
    Call<SubCVillResponse> getVillagesFromSubCenter(@Query("srcType") String srcType,
                                                    @Query("rel") String rel,
                                                    @Query("srcNodeId") String srcNodeId,
                                                    @Query("targetType") String targetType,
                                                    @Header("Authorization") String token);

    @GET("/member-svc/members/relationships/filter")
    Call<PhcStaffSubCenter> getPHCStaffList(@Query("srcType") String srcType,
                                            @Query("rel") String rel,
                                            @Query("srcNodeId") String srcNodeId,
                                            @Query("targetType") String targetType,
                                            @Header("Authorization") String token);

    @GET("/organization-svc/organizations/relationships/filter")
    Call<SubCenterResponse> getAllSubcenterResponse(@Query("srcType") String srcType,
                                                    @Query("rel") String rel,
                                                    @Query("targetType") String targetType,
                                                    @Header("Authorization") String token);


    @GET("/organization-svc/organizations/relationships/filter")
    Call<SubCVillResponse> getAllSubcenterVillageResponse(@Query("srcType") String srcType,
                                                          @Query("rel") String rel,
                                                          @Query("targetType") String targetType,
                                                          @Header("Authorization") String token);

    @GET("/organization-svc/organizations/relationships/filter")
    Call<PlacesInVillageResponse> getPlacesInVillageResponse(@Query("srcType") String srcType,
                                                             @Query("srcNodeId") String srcNodeId,
                                                             @Query("rel") String rel,
                                                             @Query("targetType") String targetType,
                                                             @Query("page") int page,
                                                             @Query("size") int size,
                                                             @Header("Authorization") String token);

    @POST("/organization-svc/organizations/grouping")
    Call<PlacesCountResponse> getPlacesInVillageCount(@Body GetPlaceCountInVillageBody body,
                                                      @Header("Authorization") String token);

    @GET("/organization-svc/organizations/relationships/filter")
    Call<HouseHoldInVillageResponse> getHousesInVillageResponse(@Query("srcType") String srcType,
                                                                @Query("srcNodeId") String srcNodeId,
                                                                @Query("rel") String rel,
                                                                @Query("targetType") String targetType,
                                                                @Query("page") int page,
                                                                @Query("size") int size,
                                                                @Header("Authorization") String token);


    @GET("/organization-svc/organizations/relationships/filter?rel=CONTAINEDINPLACE&targetType=HouseHold&srcType=Village")
    Call<RMNCHAHouseHoldResponse> getRMNCHAHouseHoldResponse(@Query("srcNodeId") String srcNodeId,
                                                             @Query("page") int page,
                                                             @Query("size") int size,
                                                             @Header("Authorization") String token);

    @GET("/member-svc/members/relationships/filter?rel=MEMBEROF&targetType=AshaWorker&srcType=SubCenter")
    Call<AshaWorkerResponse> getAshaWorkerResponse(@Query("srcNodeId") String srcNodeId,
                                                   @Header("Authorization") String token);

    @GET
    Call<FormUtilityResponse> getEcUtilityData(
            @Url String url,
            @Header("Authorization") String token);

    @GET("/member-svc/members/relationships/filter?srcType=HouseHold&rel=RESIDESIN&targetType=Citizen")
    Call<RMNCHAMembersInHouseHoldResponse> getRMNCHAMembersInHouseHoldResponse(@Query("srcNodeId") String srcNodeId,
                                                                               @Header("Authorization") String token);

    @GET("/member-svc/members/{womenUUID}")
    Call<RMNCHAPNCMemberResponse> getRMNCHAPNCMembersResponse(@Path(value = "womenUUID", encoded = true) String womenUUID,
                                                              @Header("Authorization") String token);

    @GET("/member-svc/members/relationships/filter?srcType=Citizen&rel=MARRIEDTO&targetType=Citizen")
    Call<RMNCHACoupleResponse> getRMNCHACoupleResponse(@Query("srcNodeId") String srcNodeId,
                                                       @Header("Authorization") String token);

    @POST("/eligible-couple-svc/ecservices")
    Call<RMNCHACreateECServiceResponse> makeRMNCHACreateECServiceRequest(@Body RMNCHACreateECServiceRequest rmnchaCreateECServiceRequest,
                                                                         @Header("Authorization") String token);

    @POST("/organization-svc/organizations/relationships/filter")
    Call<RMNCHAANCHouseholdsResponse> getRMNCHAANCHouseHoldsRequest(@Body RMNCHAANCHouseHoldsRequest rmnchaancHouseHoldsRequest,
                                                                    @Query("page") int page,
                                                                    @Query("size") int size,
                                                                    @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<RMNCHAPNCHouseholdsResponse> getRMNCHAPNCHouseHoldsRequest(@Body RMNCHAPNCHouseHoldsRequest rmnchapncHouseHoldsRequest,
                                                                    @Header("size") Integer size,
                                                                    @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<RMNCHAANCPWsResponse> getRMNCHAANCPWsRequest(@Body RMNCHAANCPWsRequest rmnchaancpWsRequest,
                                                      @Query("page") int page,
                                                      @Query("size") int size,
                                                      @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<RMNCHAPNCWomenResponse> getRMNCHAPNCWomenInHHRequest(@Body RMNCHAPNCWomenInHHRequest RMNCHAPNCWomenInHHRequest,
                                                              @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<RMNCHAPNCWomenResponse> getRMNCHAPNCWomenRequest(@Body RMNCHAPNCWomenRequest rmnchapncWomenRequest,
                                                          @Header("Authorization") String token);


    @GET("/anc-service-svc/ancservices/visitlogs/filter")
    Call<RMNCHAANCServiceHistoryResponse> getRMNCHAANCServiceHistoryRequest(@Query("serviceId") String serviceId,
                                                                            @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<RMNCHACoupleDetailsResponse> getRMNCHACoupleDetailsRequest(@Body RMNCHACoupleDetailsRequest RMNCHACoupleDetailsRequest,
                                                                    @Header("Authorization") String token);

    @POST("/eligible-couple-svc/ecservices/{serviceId}/visitlogs")
    Call<RMNCHACreateVisitLogResponse> makeRMNCHACreateVisitLogRequest(@Path(value = "serviceId", encoded = true) String serviceId,
                                                                       @Body RMNCHACreateVisitLogRequest rmnchaCreateVisitLogRequest,
                                                                       @Header("Authorization") String token);

    @GET("/eligible-couple-svc/ecservices/visitlogs/filter?size=15")
    Call<RMNCHAVisitLogsResponse> getRMNCHAVisitLogsResponse(@Query("serviceId") String serviceId,
                                                             @Header("Authorization") String token);

    @POST("/organization-svc/organizations/nearme")
    Call<RMNCHAHouseHoldResponse> getRMNCHANearMEHouseHoldResponse(@Body RMNCHANearMEHouseHoldRequest rmnchaNearMEHouseHoldRequest,
                                                                   @Header("Content-Type") String contentType,
                                                                   @Header("Authorization") String token);

    @POST("/organization-svc/organizations/search")
    Call<RMNCHAHouseHoldSearchResponse> getRMNCHAHouseHoldWithHeadNameResponse(@Query("query") String name,
                                                                               @Body SearchHouseholdBody searchHouseholdBody,
                                                                               @Header("Authorization") String token);

    @POST("/ec-registration-svc/ecregistrations")
    Call<RMNCHAECRegistrationResponse> makeECRegistration(@Body RMNCHAECRegistrationRequest rmnchaECRegistrationRequest,
                                                          @Header("Authorization") String header,
                                                          @Header("IdToken") String idToken);

    @POST("/anc-registration-svc/ancregistration")
    Call<RMNCHAANCRegistrationResponse> makeANCRegistration(@Body RMNCHAANCRegistrationRequest rmnchaancRegistrationRequest,
                                                            @Header("Authorization") String header,
                                                            @Header("IdToken") String idToken);

    @POST("/pnc-registration-svc/pncregistration")
    Call<RMNCHAPNCDeliveryOutcomesResponse> makePNCDeliveryOutcomesRegistration(@Body RMNCHAPNCDeliveryOutcomesRequest rmnchapncDeliveryOutcomesRequest,
                                                                                @Header("Authorization") String header,
                                                                                @Header("IdToken") String idToken);

    @GET("/pnc-registration-svc/pncregistration/{pncRegistrationId}")
    Call<RMNCHAPNCDeliveryOutcomesResponse> getPNCDeliveryOutcomesRegistrationData(@Path("pncRegistrationId") String pncRegistrationId,
                                                                                   @Header("Authorization") String header,
                                                                                   @Header("IdToken") String idToken);

    @POST("/pnc-registration-svc/pncregistration/{pncRegistrationId}/infants")
    Call<RMNCHAPNCInfantResponse> makePNCInfantRegistration(@Path("pncRegistrationId") String pncRegistrationId,
                                                            @Body RMNCHAPNCInfantRequest request,
                                                            @Header("Authorization") String header,
                                                            @Header("IdToken") String idToken);

    @GET("/pnc-registration-svc/pncregistration/infants/filter")
    Call<RMNCHAPNCInfantResponse> getPNCInfantRegistrationData(@Query("registrationId") String pncInfantRegistrationId,
                                                               @Header("Authorization") String header,
                                                               @Header("IdToken") String idToken);

    @POST("/member-svc/members/relationships/filter")
    Call<RMNCHAMotherInfantDetailsResponse> getPNCMotherInfantDate(@Body RMNCHAMotherInfantDetailsRequest request,
                                                                   @Header("Authorization") String header,
                                                                   @Header("IdToken") String idToken);

    @GET("/pnc-service-svc/pncservices/visitlogs/filter")
    Call<RMNCHAPNCMotherServiceHistoryResponse> getRMNCHAPNCMotherServiceHistory(@Query("serviceId") String serviceId,
                                                                                 @Header("Authorization") String token);

    @GET("/anc-registration-svc/ancregistration/{ancRegistrationId}")
    Call<RMNCHAANCRegistrationResponse> getANCRegistration(@Path("ancRegistrationId") String ancRegistrationId,
                                                           @Header("Authorization") String header,
                                                           @Header("IdToken") String idToken);

    @POST("/anc-service-svc/ancservices")
    Call<RMNCHAANCServiceResponse> makeANCServiceRequest(@Body RMNCHAANCServiceRequest request,
                                                         @Header("Authorization") String header,
                                                         @Header("IdToken") String idToken);

    @POST("/anc-service-svc/ancservices/{serviceId}/visitlogs")
    Call<RMNCHAANCVisitLogResponse> makeANCVisitLogCall(@Body RMNCHAANCVisitLogRequest request,
                                                        @Path("serviceId") String serviceId,
                                                        @Header("Authorization") String header,
                                                        @Header("IdToken") String idToken);

    @POST("/pnc-service-svc/pncservices")
    Call<RMNCHAPNCMotherServiceResponse> makePNCMotherServiceRequest(@Body RMNCHAPNCMotherServiceRequest request,
                                                                     @Header("Authorization") String header,
                                                                     @Header("IdToken") String idToken);

    @POST("/pnc-service-svc/pncservices/{serviceId}/visitlogs")
    Call<RMNCHAPNCMotherVisitLogResponse> makePNCMotherVisitLogCall(@Body RMNCHAPNCMotherVisitLogRequest request,
                                                                    @Path("serviceId") String serviceId,
                                                                    @Header("Authorization") String header,
                                                                    @Header("IdToken") String idToken);
    @POST("/pnc-service-svc/pncservices/{serviceId}/infants")
    Call<RMNCHAPNCInfantServiceResponse> makePNCInfantServiceRequest(@Body RMNCHAPNCInfantServiceRequest request,
                                                                     @Path("serviceId") String serviceId,
                                                                     @Header("Authorization") String header,
                                                                     @Header("IdToken") String idToken);

    @POST("/pnc-service-svc/pncservices/{serviceId}/infants/visitlogs")
    Call<RMNCHAPNCInfantVisitLogResponse> makePNCInfantVisitLogCall(@Body RMNCHAPNCInfantVisitLogRequest request,
                                                                    @Path("serviceId") String serviceId,
                                                                    @Header("Authorization") String header,
                                                                    @Header("IdToken") String idToken);

    @GET("/pnc-service-svc/pncservices/infants/visitlogs/filter")
    Call<RMNCHAPNCInfantServiceHistoryResponse> getRMNCHAPNCInfantServiceHistory(@Query("childId") String childID,
                                                                                 @Header("Authorization") String token);


    @GET("/member-svc/members/relationships/filter")
    Call<ResidentInHouseHoldResponse> getResidentsInHousehold(@Query("srcType") String srcType,
                                                              @Query("srcNodeId") String srcNodeId,
                                                              @Query("rel") String rel,
                                                              @Query("targetType") String targetType,
                                                              @Query("page") int page,
                                                              @Query("size") int size,
                                                              @Header("Authorization") String token);

    /**
     * creating and editing of the villageAsset can both be done through this api
     * if uuid is not sent, new object will be created
     * else old object with provided uuid will be updated
     */
    @POST("/organization-svc/organizations")
    Call<CreatePlaceResponse> createNewVillageAsset(@Body CreateVillageAssetBody createVillageAssetBody,
                                                    @Header("IdToken") String idToken,
                                                    @Header("Content-Type") String contentType,
                                                    @Header("Authorization") String token);

    @POST("/organization-svc/organizations")
    Call<CreateHouseholdResponse> createNewHousehold(@Body CreateHouseholdBody createVillageAssetBody,
                                                     @Header("IdToken") String idToken,
                                                     @Header("Content-Type") String contentType,
                                                     @Header("Authorization") String token);

    @PATCH("/organization-svc/organizations/{houseUuid}")
    Call<CreateHouseholdResponse> patchHousehold(@Path("houseUuid") String houseUuid,
                                                 @Body CreateHouseholdBody createHouseholdBody,
                                                 @Header("IdToken") String idToken,
                                                 @Header("Content-Type") String contentType,
                                                 @Header("Authorization") String token);

    /**
     * creating and editing of the resident both can be done through this api
     * if uuid is not sent, new object will be created
     * else old object with provided uuid will be updated
     */
    @POST("/member-svc/members")
    Call<CreateResidentResponse> createNewResident(@Body CreateResidentBody createResidentBody,
                                                   @Header("IdToken") String idToken,
                                                   @Header("Content-Type") String contentType,
                                                   @Header("Authorization") String token);

    @PATCH("/member-svc/members/{citizenid}")
    Call<CreateResidentResponse> updateResident(@Body CreateResidentBody createResidentBody,
                                                @Path("citizenid") String citizenid,
                                                @Header("IdToken") String idToken,
                                                @Header("Content-Type") String contentType,
                                                @Header("Authorization") String token);

    @GET("/member-svc/members/getprefetchedurl")
    Call<PrefetchURLResponse> getMembersPresignedURL(@Query("bucketKey") String bucketKey,
                                                     @Header("Authorization") String token);

    @GET("/organization-svc/organizations/getprefetchedurl")
    Call<PrefetchURLResponse> getOrganizationPresignedURL(@Query("bucketKey") String bucketKey,
                                                          @Header("Authorization") String token);

    @GET("/member-svc/members/getimageurl")
    Call<GetImageUrlResponse> getMembershipImageUrl(@Query("bucketKey") String bucketKey,
                                                    @Header("Authorization") String token);

    @GET("/organization-svc/organizations/getimageurl")
    Call<GetImageUrlResponse> getOrgImageUrl(@Query("bucketKey") String bucketKey,
                                             @Header("Authorization") String token);

    @PUT
    Call<PutImageResponse> putImageToS3(@Url String uploadUrl,
                                        @Header("Content-Type") String contentType,
                                        @Body RequestBody body);


    @POST("/member-svc/members")
    Call<CreateCitizenResponse> createNewCitizen(@Body CreateCitizenBody createVillageAssetBody, @Header("Content-Type") String contentType, @Header("Authorization") String token);


    @POST("/organization-svc/organizations/nearme")
    Call<ResponseNearByHouseholds> getNearbyHouseholds(@Body NearbyHouseholdRequestBody nearbyHouseholdRequestBody,
                                                       @Header("IdToken") String idToken,
                                                       @Header("Authorization") String token,
                                                       @Header("Content-Type") String contentType);

    @GET("/surveys/filter")
    Call<SurveyFilterResponse> getSurveyFilterResponse(
            @Query("surveyType") String surveyType,
            @Query("limit") int limit,
            @Query("page") int page,
            @Header("Authorization") String token
    );

    @POST("/surveys/{surveyId}/responses")
    Call<SurveyAnswersResponse> postSurveyAnswers(
            @Body SurveyAnswersBody surveyAnswersBody,
            @Path("surveyId") String surveyId,
            @Header("Authorization") String token
    );

    @GET("/surveys/filter/{contextId}")
    Call<SurveyAnswersResponse> filterSurveyByContextId(
            @Path("contextId") String contextId,
            @Header("Authorization") String token,
            @Query("surveyType") String surveyType
    );

    @POST("/base-program-svc/baseprograms")
    Call<Meeting> postMeeting(
            @Body Meeting meeting,
            @Header("Authorization") String token
    );

    @PATCH("/base-program-svc/baseprograms/{meetingId}")
    Call<Meeting> updateMeeting(
            @Path("meetingId") String meetingId,
            @Body Object meeting,
            @Header("Authorization") String token
    );

    @GET("/base-program-svc/baseprograms/{meetingId}")
    Call<Meeting> getMeetingById(
            @Path("meetingId") String meetingId,
            @Header("Authorization") String token
    );

    @GET("/base-program-svc/baseprograms/filter")
    Call<MeetingListResponse> getMeetings(
            @Query("page") int page,
            @Query("size") int size,
            @Query("programType") String programType,
            @Header("Authorization") String token
    );

    @GET("/member-svc/members/relationships/filter?srcType=GramPanchayat&rel=MEMBEROF&targetType=GPMember")
    Call<GPMemberResponse> getGPMemberList(@Query("srcNodeId") String srcNodeId,
                                           @Header("Authorization") String token);

    @GET("/organization-svc/organizations/relationships/filter?srcType=Village&rel=GOVERNEDBY&targetType=GramPanchayat")
    Call<GramPanchayatForVillage> getGramPanchayatOfVillage(@Query("srcNodeId") String srcNodeId,
                                                            @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<CCHouseHoldResponse> getChildCareHouseHolds(@Body CCHouseHoldRequest cCHouseHoldRequest,
                                                     @Header("size") Integer size,
                                                     @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<CcChildListResponse> getChildCareChildInVillage(@Body CcChildListRequest ccChildListRequest,
                                                         @Query("page") int page,
                                                         @Query("size") int size,
                                                         @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<ChildInHouseHoldResponse> getCCChildInHousehold(@Body ChildInHouseholdRequest ccChildListRequest,
                                                         @Header("Authorization") String token);



    @POST("/member-svc/members/relationships/filter")
    Call<ChildMotherDetailResponse> getChildMotherDetail(@Body ChildMotherDetailRequest ccChildListRequest,
                                                         @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<ChildMotherDetailResponse> getChildMotherDetailsByUuidList(@Body CcChildMotherDetailRequest ccChildMotherDetailRequest,
                                                         @Header("Authorization") String token);

    @POST("/member-svc/members/relationships/filter")
    Call<ChildMotherDetailResponse> getCoupleDetailByWifeID(@Body ChildMotherDetailRequest ccChildListRequest,
                                                            @Header("Authorization") String token);

    @POST("/childcare-svc/childcare")
    Call<CreateChildRegResponse> createChildReg(@Body CreateChildRegRequest createChildRegRequest,
                                                @Header("Authorization") String token);

    @POST("/childcareimmunization-svc/immunizations")
    Call<CreateImmunization> createChildImmunization(@Body CreateImmunization createImmunization,
                                                     @Header("Authorization") String token,
                                                     @Header("X-USER_ID") String userId
    );

    @POST("/adolescentcare-svc/adolescentcare")
    Call<RegisterAdolescentCareResponse> createAdolescentCare(@Body AdolescentCareRegRequest adolescentCareRegRequest, @Header("Authorization") String token);

    @POST("/adolescentcareservice-svc/adolescentcareservice")
    Call<AddAdCareServiceRequest> createAdolescentService(@Body AddAdCareServiceRequest addAdCareServiceRequest, @Header("Authorization") String token);

    @GET("/childcareimmunization-svc/immunizations/groupby/{childId}")
    Call<LinkedHashMap<String, List<Immunization>>> getChildHistory(@Path("childId") String childId, @Header("Authorization") String token);

    @GET("/pnc-registration-svc/pncregistration/{pncRegistrationId}/infants/{pncInfantRegistrationId}")
    Call<PncServiceData> getPncServiceData(@Path("pncRegistrationId") String pncRegistrationId,
                                           @Path("pncInfantRegistrationId") String pncInfantRegistrationId,
                                           @Header("Authorization") String token);

    @GET("/pnc-registration-svc/pncregistration/{pncRegistrationId}")
    Call<PncRegistrationData> getPncRegistrationData(@Path("pncRegistrationId") String pncRegistrationId,
                                                     @Header("Authorization") String token);

}
