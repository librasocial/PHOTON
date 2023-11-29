package com.ssf.homevisit.repository;

import android.view.WindowManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.AddAdCareServiceRequest;
import com.ssf.homevisit.models.AdolescentCareRegRequest;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.CCHouseHoldRequest;
import com.ssf.homevisit.models.CCHouseHoldResponse;
import com.ssf.homevisit.models.CcChildListRequest;
import com.ssf.homevisit.models.CcChildListResponse;
import com.ssf.homevisit.models.CcChildMotherDetailRequest;
import com.ssf.homevisit.models.ChildInHouseHoldResponse;
import com.ssf.homevisit.models.ChildInHouseholdRequest;
import com.ssf.homevisit.models.ChildMotherDetailRequest;
import com.ssf.homevisit.models.ChildMotherDetailResponse;
import com.ssf.homevisit.models.CreateChildRegRequest;
import com.ssf.homevisit.models.CreateChildRegResponse;
import com.ssf.homevisit.models.CreateImmunization;
import com.ssf.homevisit.models.GetPlaceCountInVillageBody;
import com.ssf.homevisit.models.PlacesCountResponse;
import com.ssf.homevisit.models.PlacesInVillageResponse;
import com.ssf.homevisit.models.PncRegistrationData;
import com.ssf.homevisit.models.PncServiceData;
import com.ssf.homevisit.models.RegisterAdolescentCareResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.childCare.immunization.Immunization;
import com.ssf.homevisit.utils.AnalyticsEvents;
import com.ssf.homevisit.utils.Util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VillageLevelMappingRepository {
    private static final String TAG = VillageLevelMappingRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    public static MutableLiveData<PlacesInVillageResponse> placesData = new MutableLiveData<>();
    public static MutableLiveData<PlacesCountResponse> placesCountData = new MutableLiveData<>();
    public static MutableLiveData<CCHouseHoldResponse> childCareHouseHoldInVillage = new MutableLiveData<>();
    public static MutableLiveData<CcChildListResponse> childCareChildrenInVillage = new MutableLiveData<>();
    public static MutableLiveData<ChildInHouseHoldResponse> childInHousehold = new MutableLiveData<>();
    public static MutableLiveData<ChildMotherDetailResponse> childMotherDetail = new MutableLiveData<>();
    public static MutableLiveData<ChildMotherDetailResponse> childMotherDetailsList = new MutableLiveData<>();
    public static MutableLiveData<ChildMotherDetailResponse> coupleDetail = new MutableLiveData<>();
    public static MutableLiveData<List<AshaWorkerResponse.Content>> aashaWorkersInSHC = new MutableLiveData<>();
    public static MutableLiveData<CreateChildRegResponse> createChildResponse = new MutableLiveData<>();
    public static MutableLiveData<PncServiceData> pncServiceData = new MutableLiveData<>();
    public static MutableLiveData<PncRegistrationData> pncRegistrationData = new MutableLiveData<>();

    public static MutableLiveData<CreateImmunization> createImmunization = new MutableLiveData<>();
    public static MutableLiveData<LinkedHashMap<String,List<Immunization>>> childHistory = new MutableLiveData<>();

    public static MutableLiveData<RegisterAdolescentCareResponse> registerAdolescent = new MutableLiveData<>();
    public static MutableLiveData<AddAdCareServiceRequest> createAdCareService = new MutableLiveData<>();

    public VillageLevelMappingRepository() {
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public LiveData<PlacesInVillageResponse> getPlacesInVillageData(String villageId, int pageNo, int size) {
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // TODO uncomment villageId
        apiInterface.getPlacesInVillageResponse("Village", /*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/villageId, "CONTAINEDINPLACE", "Place", pageNo, size, Util.getHeader()).enqueue(new Callback<PlacesInVillageResponse>() {
            @Override
            public void onResponse(Call<PlacesInVillageResponse> call, Response<PlacesInVillageResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        placesData.setValue(response.body());
                        PlacesInVillageResponse allPlacesResponse = response.body();
                    }
                } else {
                    placesData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PlacesInVillageResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                placesData.setValue(null);
            }
        });
        return placesData;
    }

    public LiveData<PlacesCountResponse> getPlacesCountData(GetPlaceCountInVillageBody body) {
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        // TODO uncomment villageId
        apiInterface.getPlacesInVillageCount(body, Util.getHeader()).enqueue(new Callback<PlacesCountResponse>() {
            @Override
            public void onResponse(Call<PlacesCountResponse> call, Response<PlacesCountResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        placesCountData.setValue(response.body());
                    }
                } else {
                    Map<String, String> properties = new HashMap<>();
                    properties.put("response code", response.code()+"");
                    properties.put("response message", response.message());
                    Analytics.trackEvent(AnalyticsEvents.UNACCEPTABLE_RESPONSE_PLACES_COUNT, properties);
                    placesCountData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PlacesCountResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Crashes.trackError(t);
                placesCountData.setValue(null);
            }
        });
        return placesCountData;
    }

    public LiveData<CCHouseHoldResponse> getChildCareHouseHoldInVillage(CCHouseHoldRequest body) {
        AppController.getInstance().getCurrentActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getChildCareHouseHolds(body,20, Util.getHeader()).enqueue(new Callback<CCHouseHoldResponse>() {
            @Override
            public void onResponse(Call<CCHouseHoldResponse> call, Response<CCHouseHoldResponse> response) {
                AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        childCareHouseHoldInVillage.setValue(response.body());
                    }
                } else {
                    Map<String, String> properties = new HashMap<>();
                    properties.put("response code", response.code()+"");
                    properties.put("response message", response.message());
                    properties.put("request", body.toString());
                    Analytics.trackEvent(AnalyticsEvents.CC_HOUSEHOLD_IN_VILLAGE, properties);
                    childCareHouseHoldInVillage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CCHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Crashes.trackError(t);
                childCareHouseHoldInVillage.setValue(null);
            }
        });
        return childCareHouseHoldInVillage;
    }

    public LiveData<CcChildListResponse> getChildCareChildrenInVillage(CcChildListRequest body) {
        AppController.getInstance().getCurrentActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getChildCareChildInVillage(body,0,200, Util.getHeader()).enqueue(new Callback<CcChildListResponse>() {
            @Override
            public void onResponse(Call<CcChildListResponse> call, Response<CcChildListResponse> response) {
                AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        childCareChildrenInVillage.setValue(response.body());
                    }
                } else {
                    Map<String, String> properties = new HashMap<>();
                    properties.put("response code", response.code()+"");
                    properties.put("response message", response.message());
                    properties.put("request", body.toString());
                    Analytics.trackEvent(AnalyticsEvents.CC_HOUSEHOLD_IN_VILLAGE, properties);
                    childCareChildrenInVillage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CcChildListResponse> call, Throwable t) {
                AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Crashes.trackError(t);
                childCareChildrenInVillage.setValue(null);
            }
        });
        return childCareChildrenInVillage;
    }

    public LiveData<ChildInHouseHoldResponse> getChildCareChildrenInVillage(ChildInHouseholdRequest body) {
        AppController.getInstance().getCurrentActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getCCChildInHousehold(body, Util.getHeader()).enqueue(new Callback<ChildInHouseHoldResponse>() {
            @Override
            public void onResponse(Call<ChildInHouseHoldResponse> call, Response<ChildInHouseHoldResponse> response) {
                AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        childInHousehold.setValue(response.body());
                    }
                } else {
                    Map<String, String> properties = new HashMap<>();
                    properties.put("response code", response.code()+"");
                    properties.put("response message", response.message());
                    properties.put("request", body.toString());
                    Analytics.trackEvent(AnalyticsEvents.CC_HOUSEHOLD_IN_VILLAGE, properties);
                    childInHousehold.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChildInHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Crashes.trackError(t);
                childInHousehold.setValue(null);
            }
        });
        return childInHousehold;
    }

    public LiveData<ChildMotherDetailResponse> getChildMotherDetail(ChildMotherDetailRequest body) {
        apiInterface.getChildMotherDetail(body, Util.getHeader()).enqueue(new Callback<ChildMotherDetailResponse>() {
            @Override
            public void onResponse(Call<ChildMotherDetailResponse> call, Response<ChildMotherDetailResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        childMotherDetail.setValue(response.body());
                    }
                } else {
                    Map<String, String> properties = new HashMap<>();
                    properties.put("response code", response.code()+"");
                    properties.put("response message", response.message());
                    properties.put("request", body.toString());
                    Analytics.trackEvent(AnalyticsEvents.CC_HOUSEHOLD_IN_VILLAGE, properties);
                    childMotherDetail.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChildMotherDetailResponse> call, Throwable t) {
                Crashes.trackError(t);
                childMotherDetail.setValue(null);
            }
        });
        return childMotherDetail;
    }

    public LiveData<ChildMotherDetailResponse> getCoupleDetailByWifeID(ChildMotherDetailRequest body) {
        apiInterface.getCoupleDetailByWifeID(body, Util.getHeader()).enqueue(new Callback<ChildMotherDetailResponse>() {
            @Override
            public void onResponse(Call<ChildMotherDetailResponse> call, Response<ChildMotherDetailResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        coupleDetail.setValue(response.body());
                    }
                } else {
                    Map<String, String> properties = new HashMap<>();
                    properties.put("response code", response.code()+"");
                    properties.put("response message", response.message());
                    properties.put("request", body.toString());
                    Analytics.trackEvent(AnalyticsEvents.CC_HOUSEHOLD_IN_VILLAGE, properties);
                    coupleDetail.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChildMotherDetailResponse> call, Throwable t) {
                Crashes.trackError(t);
                coupleDetail.setValue(null);
            }
        });
        return coupleDetail;
    }

    public MutableLiveData<List<AshaWorkerResponse.Content>> getAshaWorkerLiveData(String uuid) {
        apiInterface.getAshaWorkerResponse(uuid, Util.getHeader()).enqueue(new Callback<AshaWorkerResponse>() {
            @Override
            public void onResponse(Call<AshaWorkerResponse> call, Response<AshaWorkerResponse> response) {
                if (response.code() == 200) {
                    AshaWorkerResponse responseData = response.body();
                    if (responseData != null) {
                        aashaWorkersInSHC.setValue(responseData.getContent());
                    } else {
                        aashaWorkersInSHC.setValue(null);
                    }
                } else {
                    aashaWorkersInSHC.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AshaWorkerResponse> call, Throwable t) {
                aashaWorkersInSHC.setValue(null);
            }
        });
        return aashaWorkersInSHC;
    }

    public MutableLiveData<CreateChildRegResponse> createChildReg(CreateChildRegRequest requestBody) {
        apiInterface.createChildReg(requestBody, Util.getHeader()).enqueue(new Callback<CreateChildRegResponse>() {
            @Override
            public void onResponse(Call<CreateChildRegResponse> call, Response<CreateChildRegResponse> response) {
                if (response.code() == 200) {
                    createChildResponse.setValue(response.body());
                } else {
                    aashaWorkersInSHC.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CreateChildRegResponse> call, Throwable t) {
                createChildResponse.setValue(null);
            }
        });
        return createChildResponse;
    }

    public MutableLiveData<CreateImmunization> createChildImmunization(CreateImmunization requestBody) {
        apiInterface.createChildImmunization(requestBody, Util.getHeader(),Util.getIdToken()).enqueue(new Callback<CreateImmunization>() {
            @Override
            public void onResponse(Call<CreateImmunization> call, Response<CreateImmunization> response) {
                if (response.isSuccessful()) {
                    createImmunization.setValue(response.body());
                } else {
                    createImmunization.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<CreateImmunization> call, Throwable t) {
                createImmunization.setValue(null);
            }
        });
        return createImmunization;
    }

    public MutableLiveData<RegisterAdolescentCareResponse> registerAdolescentCare(AdolescentCareRegRequest requestBody) {
        apiInterface.createAdolescentCare(requestBody, Util.getHeader()).enqueue(new Callback<RegisterAdolescentCareResponse>() {
            @Override
            public void onResponse(Call<RegisterAdolescentCareResponse> call, Response<RegisterAdolescentCareResponse> response) {
                if (response.code() == 200) {
                    registerAdolescent.setValue(response.body());
                } else {
                    registerAdolescent.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RegisterAdolescentCareResponse> call, Throwable t) {
                registerAdolescent.setValue(null);
            }
        });
        return registerAdolescent;
    }

    public MutableLiveData<AddAdCareServiceRequest> createAdCareService(AddAdCareServiceRequest requestBody) {
        apiInterface.createAdolescentService(requestBody, Util.getHeader()).enqueue(new Callback<AddAdCareServiceRequest>() {
            @Override
            public void onResponse(Call<AddAdCareServiceRequest> call, Response<AddAdCareServiceRequest> response) {
                if (response.code() == 200) {
                    createAdCareService.setValue(response.body());
                } else {
                    createAdCareService.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AddAdCareServiceRequest> call, Throwable t) {
                createAdCareService.setValue(null);
            }
        });
        return createAdCareService;
    }

    public MutableLiveData<LinkedHashMap<String,List<Immunization>>> getChildImmunizationHistory(String childId) {
        apiInterface.getChildHistory(childId, Util.getHeader()).enqueue(new Callback<LinkedHashMap<String,List<Immunization>>>() {
            @Override
            public void onResponse(Call<LinkedHashMap<String,List<Immunization>>> call, Response<LinkedHashMap<String,List<Immunization>>> response) {
                if (response.code() == 200) {
                    LinkedHashMap<String,List<Immunization>> responseData = response.body();
                    if (responseData != null) {
                        childHistory.setValue(responseData);
                    } else {
                        childHistory.setValue(null);
                    }
                } else {
                    childHistory.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<LinkedHashMap<String,List<Immunization>>> call, Throwable t) {
                childHistory.setValue(null);
            }
        });
        return childHistory;
    }

    public MutableLiveData<ChildMotherDetailResponse> getChildMotherDetailsByChildUUIDList(CcChildMotherDetailRequest body) {
        apiInterface.getChildMotherDetailsByUuidList(body, Util.getHeader()).enqueue(new Callback<ChildMotherDetailResponse>() {
            @Override
            public void onResponse(Call<ChildMotherDetailResponse> call, Response<ChildMotherDetailResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        childMotherDetailsList.setValue(response.body());
                    }
                } else {
                    childMotherDetailsList.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ChildMotherDetailResponse> call, Throwable t) {
                Crashes.trackError(t);
                childMotherDetailsList.setValue(null);
            }
        });
        return childMotherDetailsList;
    }

    public MutableLiveData<PncServiceData> getPncServiceData(String pncRegistrationId, String pncInfantRegistrationId) {
        apiInterface.getPncServiceData(pncRegistrationId, pncInfantRegistrationId, Util.getHeader()).enqueue(new Callback<PncServiceData>() {
            @Override
            public void onResponse(Call<PncServiceData> call, Response<PncServiceData> response) {
                if (response.code() == 200) {
                    pncServiceData.setValue(response.body());
                } else {
                    pncServiceData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PncServiceData> call, Throwable t) {
                pncServiceData.setValue(null);
            }
        });
        return pncServiceData;
    }

    public MutableLiveData<PncRegistrationData> getPncRegistrationData(String pncRegistrationId) {
        apiInterface.getPncRegistrationData(pncRegistrationId,  Util.getHeader()).enqueue(new Callback<PncRegistrationData>() {
            @Override
            public void onResponse(Call<PncRegistrationData> call, Response<PncRegistrationData> response) {
                if (response.code() == 200) {
                    pncRegistrationData.setValue(response.body());
                } else {
                    pncRegistrationData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PncRegistrationData> call, Throwable t) {
                pncRegistrationData.setValue(null);
            }
        });
        return pncRegistrationData;
    }

}
