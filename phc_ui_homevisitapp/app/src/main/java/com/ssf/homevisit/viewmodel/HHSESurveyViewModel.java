package com.ssf.homevisit.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ssf.homevisit.interfaces.OnHouseholdCreated;
import com.ssf.homevisit.models.CreateHouseholdBody;
import com.ssf.homevisit.models.CreateHouseholdResponse;
import com.ssf.homevisit.models.HouseHoldInVillageResponse;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.NearbyHouseholdRequestBody;
import com.ssf.homevisit.models.ResponseNearByHouseholds;
import com.ssf.homevisit.models.SearchHouseholdResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.repository.HouseHoldLevelMappingRepository;
import com.ssf.homevisit.repository.HouseHoldSearchRepository;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import java.io.IOException;

import okhttp3.Request;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HHSESurveyViewModel extends AndroidViewModel {
    private LiveData<SearchHouseholdResponse> searchHouseHoldRes;
    private HouseHoldSearchRepository houseHoldSearchRepository;
    public final MutableLiveData<String> phcName = new MutableLiveData<>();
    public final MutableLiveData<String> subCenterName = new MutableLiveData<>();
    public final MutableLiveData<String> villageName = new MutableLiveData<>();
    private final ApiInterface apiInterface;
    private final HouseHoldLevelMappingRepository householdMappingRepository;
    private LiveData<HouseHoldInVillageResponse> householdInVillageResponseLiveData;
    private LiveData<SearchHouseholdResponse> houseHoldInSearchResponseLiveData;
    private MutableLiveData<ResponseNearByHouseholds> responseNearByHouseholdsLiveData;
    private String villageId;
    private String houseId;


    public HHSESurveyViewModel(@NonNull Application application, VillageProperties village, String _SubCenterName, String _PhcName, String houseId) {

        super(application);
        this.houseId = houseId;
        villageName.setValue(village.getName());
        phcName.setValue(_PhcName);
        subCenterName.setValue(_SubCenterName);
        householdMappingRepository = new HouseHoldLevelMappingRepository();
        this.villageId = village.getUuid();
        householdInVillageResponseLiveData = householdMappingRepository.getHouseholdInVillageData(villageId, 0, 1000);

        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public LiveData<HouseHoldInVillageResponse> getHouseHoldInVillageData() {
        householdInVillageResponseLiveData = new MutableLiveData<>();
        householdInVillageResponseLiveData = householdMappingRepository.getHouseholdInVillageData(villageId, 0, 500);
        householdInVillageResponseLiveData = HouseHoldLevelMappingRepository.householdData;
        return householdInVillageResponseLiveData;
    }

    public LiveData<HouseHoldInVillageResponse> getHouseHoldInVillageData(String villageId) {
        householdInVillageResponseLiveData = new MutableLiveData<>();
        householdInVillageResponseLiveData = householdMappingRepository.getHouseholdInVillageData(villageId, 0, 500);
        householdInVillageResponseLiveData = HouseHoldLevelMappingRepository.householdData;
        return householdInVillageResponseLiveData;
    }

    public String houseId1;

    public LiveData<SearchHouseholdResponse> getHouseHoldSearchData(String housesId, String villageLgdCode) {
            houseHoldInSearchResponseLiveData = new MutableLiveData<>();
        houseHoldInSearchResponseLiveData = householdMappingRepository.getSearchHouseholdData(housesId, 0, 5000, villageLgdCode);
        houseHoldInSearchResponseLiveData = HouseHoldLevelMappingRepository.householdSearchData;
            return houseHoldInSearchResponseLiveData;
    }

    public void createHousehold(double latitude, double longitude, HouseHoldProperties houseHoldProperties, OnHouseholdCreated onHouseholdCreated) {
        if (houseHoldProperties == null) {
            houseHoldProperties = new HouseHoldProperties();
        }
        houseHoldProperties.setLongitude(longitude);
        houseHoldProperties.setLatitude(latitude);
        CreateHouseholdBody createHouseholdBody = new CreateHouseholdBody();
        createHouseholdBody.setType("HouseHold");
        houseHoldProperties.setVillage(villageId);
        createHouseholdBody.setProperties(houseHoldProperties);

        apiInterface.createNewHousehold(createHouseholdBody, Util.getIdToken(), "application/json", Util.getHeader()).enqueue(new Callback<CreateHouseholdResponse>() {
            @Override
            public void onResponse(Call<CreateHouseholdResponse> call, Response<CreateHouseholdResponse> response) {
                onHouseholdCreated.onHouseholdCreated(response.body());
            }

            @Override
            public void onFailure(Call<CreateHouseholdResponse> call, Throwable t) {
                onHouseholdCreated.onHouseholdCreationFailed();
            }
        });
    }

    public LiveData<ResponseNearByHouseholds> getNearByHousehold(int size, int page, double latitude, double longitude, double distance, String villageId) {
        if (responseNearByHouseholdsLiveData == null)
            responseNearByHouseholdsLiveData = new MutableLiveData<>();
        NearbyHouseholdRequestBody nearbyHouseholdRequestBody = new NearbyHouseholdRequestBody();
        nearbyHouseholdRequestBody.setType("HouseHold");
        nearbyHouseholdRequestBody.setPage(page);
        nearbyHouseholdRequestBody.setSize(size);
        NearbyHouseholdRequestBody.Properties properties = new NearbyHouseholdRequestBody.Properties();
        properties.setVillageId(villageId);
        properties.setLatitude(latitude);
        properties.setLongitude(longitude);
        properties.setDistance(distance);
        nearbyHouseholdRequestBody.setProperties(properties);

        apiInterface.getNearbyHouseholds(nearbyHouseholdRequestBody, Util.getIdToken(), Util.getHeader(), "application/json").enqueue(new Callback<ResponseNearByHouseholds>() {
            @Override
            public void onResponse(Call<ResponseNearByHouseholds> call, Response<ResponseNearByHouseholds> response) {
                if (response.body() != null)
                    responseNearByHouseholdsLiveData.setValue(response.body());
                else responseNearByHouseholdsLiveData.setValue(null);
            }

            @Override
            public void onFailure(Call<ResponseNearByHouseholds> call, Throwable t) {
                responseNearByHouseholdsLiveData.setValue(null);
            }
        });
        return responseNearByHouseholdsLiveData;
    }

    public void removeObservers(LifecycleOwner context) {
        if (householdInVillageResponseLiveData != null)
            householdInVillageResponseLiveData.removeObservers(context);
        if (houseHoldInSearchResponseLiveData != null)
            houseHoldInSearchResponseLiveData.removeObservers(context);
        if (responseNearByHouseholdsLiveData != null)
            responseNearByHouseholdsLiveData.removeObservers(context);
    }
}
