package com.ssf.homevisit.viewmodel;

import android.app.Application;

import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.ssf.homevisit.fragment.HouseholdDataFragment;
import com.ssf.homevisit.interfaces.OnHouseholdCreated;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.repository.HouseHoldLevelMappingRepository;
import com.ssf.homevisit.repository.HouseHoldSearchRepository;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.AnalyticsEvents;
import com.ssf.homevisit.utils.Util;
import okhttp3.Request;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HouseHoldLevelMappingViewModel extends AndroidViewModel {

    private LiveData<SearchHouseholdResponse> searchHouseHoldRes;
    private HouseHoldSearchRepository houseHoldSearchRepository;
    public MutableLiveData<String> phcName = new MutableLiveData<>();
    public MutableLiveData<String> subCenterName = new MutableLiveData<>();
    public MutableLiveData<String> villageName = new MutableLiveData<>();
    private final ApiInterface apiInterface;
    private final HouseHoldLevelMappingRepository householdMappingRepository;
    private LiveData<HouseHoldInVillageResponse> householdInVillageResponseLiveData;
    private LiveData<SearchHouseholdResponse> houseHoldInSearchResponseLiveData;
    private String villageId;
    private String houseId;


    public HouseHoldLevelMappingViewModel(@NonNull Application application, VillageProperties village, String _SubCenterName, String _PhcName, String houseId) {
        super(application);
        this.houseId = houseId;
        villageName.setValue(village.getName());
        phcName.setValue(_PhcName);
        subCenterName.setValue(_SubCenterName);
        householdMappingRepository = new HouseHoldLevelMappingRepository();
        this.villageId = village.getUuid();
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public LiveData<HouseHoldInVillageResponse> getHouseHoldInVillageData(int pageNo, int limit) {
        householdInVillageResponseLiveData = new MutableLiveData<>();
        householdInVillageResponseLiveData = householdMappingRepository.getHouseholdInVillageData(/*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/villageId, pageNo, limit);
        householdInVillageResponseLiveData = HouseHoldLevelMappingRepository.householdData;
        return householdInVillageResponseLiveData;
    }

    public String houseId1;

    public LiveData<SearchHouseholdResponse> getHouseHoldSearchData(String housesId, int pageNo, int limit, String villageLgdCode) {
        try {
            houseHoldInSearchResponseLiveData = new MutableLiveData<>();
            houseHoldInSearchResponseLiveData = householdMappingRepository.getSearchHouseholdData(housesId, pageNo, limit, villageLgdCode);
            houseHoldInSearchResponseLiveData = HouseHoldLevelMappingRepository.householdSearchData;
            return houseHoldInSearchResponseLiveData;
        } catch (Exception e) {
            Toast toast = Toast.makeText(this.getApplication(),
                    "This is a message displayed in a Toast",
                    Toast.LENGTH_SHORT);

            toast.show();
            return houseHoldInSearchResponseLiveData;
        }
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

        try {
            String json = new ObjectMapper().writeValueAsString(createHouseholdBody);
            Log.d("json of createHouseHold", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

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

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d("oncleared", "cleared");
        phcName = new MutableLiveData<>();
        subCenterName = new MutableLiveData<>();
        villageName = new MutableLiveData<>();
        householdInVillageResponseLiveData = null;
        houseHoldInSearchResponseLiveData = null;
        villageId = null;
        houseId = null;
        householdMappingRepository.clear();
    }

    public void clear() {
        Log.d("tag", "cleared");
        phcName = new MutableLiveData<>();
        subCenterName = new MutableLiveData<>();
        villageName = new MutableLiveData<>();
        householdInVillageResponseLiveData = null;
        houseHoldInSearchResponseLiveData = null;
        villageId = null;
        houseId = null;
        householdMappingRepository.clear();
    }

    ;

    public void patchHousehold(double latitude, double longitude, HouseHoldProperties houseHoldProperties, OnHouseholdCreated onHouseholdCreated) {
        if (houseHoldProperties == null) {
            houseHoldProperties = new HouseHoldProperties();
        }
        houseHoldProperties.setLongitude(longitude);
        houseHoldProperties.setLatitude(latitude);
        CreateHouseholdBody createHouseholdBody = new CreateHouseholdBody();
        createHouseholdBody.setType("HouseHold");
        houseHoldProperties.setVillage(villageId/*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/);
        createHouseholdBody.setProperties(houseHoldProperties);

        try {
            String json = new ObjectMapper().writeValueAsString(createHouseholdBody);
            Log.d("json of createHouseHold", json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        HouseHoldProperties finalHouseHoldProperties = houseHoldProperties;
        apiInterface.patchHousehold(houseHoldProperties.getUuid(), createHouseholdBody, Util.getIdToken(), "application/json", Util.getHeader()).enqueue(new Callback<CreateHouseholdResponse>() {
            @Override
            public void onResponse(Call<CreateHouseholdResponse> call, Response<CreateHouseholdResponse> response) {
                Log.d("here", "response code = " + response.code());
                try {
                    final Request copy = response.raw().request();
                    final Buffer buffer = new Buffer();
                    copy.body().writeTo(buffer);
                    String a = buffer.readUtf8();
                    Log.d("resquest body = ", a);
                } catch (final IOException e) {

                }
                String a = response.raw().message();
                Log.d("resquest body = ", a);
                if(response.code()!=200){
                    Map<String, String> properties = new HashMap<>();
                    properties.put("id", finalHouseHoldProperties.getUuid());
                    properties.put("code", response.code()+"");
                    properties.put("message", response.message());
                    Analytics.trackEvent(AnalyticsEvents.HOUSEHOLD_UPDATION_FAILED);
                }
                onHouseholdCreated.onHouseholdCreated(response.body());
            }

            @Override
            public void onFailure(Call<CreateHouseholdResponse> call, Throwable t) {
                onHouseholdCreated.onHouseholdCreationFailed();
                Crashes.trackError(t);
                Log.d("here", "ah fail = ");
            }
        });
    }

}
