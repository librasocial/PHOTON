package com.ssf.homevisit.repository;

import android.util.Log;
import android.view.WindowManager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.microsoft.appcenter.analytics.Analytics;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.HouseHoldInVillageResponse;
import com.ssf.homevisit.models.ResidentInHouseHoldResponse;
import com.ssf.homevisit.models.SearchHouseholdBody;
import com.ssf.homevisit.models.SearchHouseholdBodyProperty;
import com.ssf.homevisit.models.SearchHouseholdResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseHoldLevelMappingRepository {
    private static final String TAG = HouseHoldLevelMappingRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    public static MutableLiveData<HouseHoldInVillageResponse> householdData = new MutableLiveData<>();
    public static MutableLiveData<SearchHouseholdResponse> householdSearchData = new MutableLiveData<>();
    public static MutableLiveData<ResidentInHouseHoldResponse> residentsData = new MutableLiveData<>();

    public HouseHoldLevelMappingRepository() {
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
        householdData = new MutableLiveData<>();
        householdSearchData = new MutableLiveData<>();
        residentsData = new MutableLiveData<>();
    }

    public LiveData<HouseHoldInVillageResponse> getHouseholdInVillageData(String villageId, int pageNo, int size) {
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (villageId == null || villageId.isEmpty()) {
            householdData.setValue(null);
            return householdData;
        }
        apiInterface.getHousesInVillageResponse("Village",villageId, "CONTAINEDINPLACE", "HouseHold", pageNo, size, Util.getHeader()).enqueue(new Callback<HouseHoldInVillageResponse>() {
            @Override
            public void onResponse(Call<HouseHoldInVillageResponse> call, Response<HouseHoldInVillageResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        householdData.setValue(response.body());
                        HouseHoldInVillageResponse allPlacesResponse = response.body();
                    }
                } else {
                    householdData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<HouseHoldInVillageResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                householdData.setValue(null);
            }
        });
        return householdData;
    }

    public LiveData<SearchHouseholdResponse> getSearchHouseholdData(String houseHoldId, int pageNo, int size, String lgdCode) {
        householdSearchData = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        SearchHouseholdBody body = new SearchHouseholdBody();
        body.setType("HouseHold");
        body.setPage(pageNo);
        body.setSize(size);
        SearchHouseholdBodyProperty property = new SearchHouseholdBodyProperty();
        property.setVillageId(lgdCode);
        body.setProperties(property);
        apiInterface.searchByHouseId(houseHoldId, body, Util.getHeader()).enqueue(new Callback<SearchHouseholdResponse>() {
            @Override
            public void onResponse(Call<SearchHouseholdResponse> call, Response<SearchHouseholdResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        householdSearchData.setValue(response.body());
                    } else {
                        householdSearchData.setValue(response.body());
                    }
                } else {
                    householdSearchData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SearchHouseholdResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                householdSearchData.setValue(null);
            }
        });
        return householdSearchData;
    }

    public LiveData<ResidentInHouseHoldResponse> getResidentsInHousehold(String householdId, int pageNo, int size) {
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getResidentsInHousehold("HouseHold", householdId, "RESIDESIN", "Citizen", pageNo, size, Util.getHeader()).enqueue(new Callback<ResidentInHouseHoldResponse>() {
            @Override
            public void onResponse(Call<ResidentInHouseHoldResponse> call, Response<ResidentInHouseHoldResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        residentsData.setValue(response.body());
                        ResidentInHouseHoldResponse allPlacesResponse = response.body();
                        List<ResidentInHouseHoldResponse.Content> contentList = allPlacesResponse.getContent();
                    }
                } else {
                    residentsData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResidentInHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        return residentsData;
    }

    public void clear() {
        householdData = new MutableLiveData<>();
        householdSearchData = new MutableLiveData<>();
        residentsData = new MutableLiveData<>();
    }
}
