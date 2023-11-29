package com.ssf.homevisit.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.ssf.homevisit.interfaces.OnCitizenCreated;
import com.ssf.homevisit.interfaces.OnImageUrlFetched;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.repository.HouseHoldLevelMappingRepository;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class HouseholdAlertViewModel extends AndroidViewModel {
    private final ApiInterface apiInterface;
    HouseHoldProperties houseHoldProperties;

    private LiveData<ResidentInHouseHoldResponse> residentsInHouseholdResponseLiveData;
    private final HouseHoldLevelMappingRepository householdMappingRepository;
    public MutableLiveData<String> latlong = new MutableLiveData<>();
    public MutableLiveData<String> hid = new MutableLiveData<>();

    public HouseholdAlertViewModel(@NonNull Application application, HouseHoldProperties _houseHoldProperties) {
        super(application);
        houseHoldProperties = _houseHoldProperties;
        householdMappingRepository = new HouseHoldLevelMappingRepository();
        residentsInHouseholdResponseLiveData = householdMappingRepository.getResidentsInHousehold(/*"c7f517e5-66e2-4033-957f-0e36ffa0b76f"*/houseHoldProperties.getUuid(), 0, 1000);
        hid.setValue(houseHoldProperties.getHouseID());
        latlong.setValue(Util.DDtoDMS(houseHoldProperties.getLatitude(), "latitude") + " - " + Util.DDtoDMS(houseHoldProperties.getLongitude(), "longitude"));
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public LiveData<ResidentInHouseHoldResponse> getResidentsInHouseholdData() {
        residentsInHouseholdResponseLiveData = new MutableLiveData<>();
        residentsInHouseholdResponseLiveData = householdMappingRepository.getResidentsInHousehold(/*"c7f517e5-66e2-4033-957f-0e36ffa0b76f"*/houseHoldProperties.getUuid(), 0, 50);
        residentsInHouseholdResponseLiveData = HouseHoldLevelMappingRepository.residentsData;
        return residentsInHouseholdResponseLiveData;
    }

    public void createCitizen(ResidentProperties residentProperties, OnCitizenCreated onCitizenCreated) {
        CreateResidentBody createResidentBody = new CreateResidentBody();
        createResidentBody.setType("Citizen");
        residentProperties.setHouseHold(houseHoldProperties.getUuid());
        createResidentBody.setProperties(residentProperties);
        String idToken = Util.getIdToken();

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(createResidentBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Log.d("body", json);

        if (residentProperties.getUuid() != null) {
            apiInterface.updateResident(createResidentBody, residentProperties.getUuid(), idToken, "application/json", Util.getHeader()).enqueue(new Callback<CreateResidentResponse>() {
                @Override
                public void onResponse(Call<CreateResidentResponse> call, Response<CreateResidentResponse> response) {
                    // todo handle 500 response here
                    if (response.code() == 500) {
                        String body;
                        try {
                            body = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    onCitizenCreated.onCitizenCreated(response.body());
                }

                @Override
                public void onFailure(Call<CreateResidentResponse> call, Throwable t) {
                    t.getMessage();
                    Gson gson = new Gson();
                    String responseString = gson.toJson(call.request().body());
                    onCitizenCreated.onCitizenCreationFailed();
                }
            });

        } else {
            apiInterface.createNewResident(createResidentBody, idToken, "application/json", Util.getHeader()).enqueue(new Callback<CreateResidentResponse>() {
                @Override
                public void onResponse(Call<CreateResidentResponse> call, Response<CreateResidentResponse> response) {
                    // todo handle 500 response here
                    if (response.code() == 500) {
                        String body;
                        try {
                            body = response.errorBody().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    onCitizenCreated.onCitizenCreated(response.body());
                }

                @Override
                public void onFailure(Call<CreateResidentResponse> call, Throwable t) {
                    t.getMessage();
                    Gson gson = new Gson();
                    String responseString = gson.toJson(call.request().body());
                    onCitizenCreated.onCitizenCreationFailed();
                }
            });

        }


    }

    public void setHouseholdProperties(HouseHoldProperties houseHoldProperties) {
        this.houseHoldProperties = houseHoldProperties;
//        getResidentsInHouseholdData();
        hid.setValue(houseHoldProperties.getHouseID());
        latlong.setValue(Util.DDtoDMS(houseHoldProperties.getLatitude(), "latitude") + " - " + Util.DDtoDMS(houseHoldProperties.getLongitude(), "longitude"));
    }

    public void getImageUrl(OnImageUrlFetched onImageUrlFetched, String bucketKey) {
        apiInterface.getMembershipImageUrl(bucketKey, Util.getHeader()).enqueue(new Callback<GetImageUrlResponse>() {
            @Override
            public void onResponse(@NotNull Call<GetImageUrlResponse> call, @NotNull Response<GetImageUrlResponse> response) {
                if (response.body() != null)
                    onImageUrlFetched.onImageUrlFetched(response.body().getPreSignedUrl());
                else
                    onImageUrlFetched.onUrlFetchingFail();
            }

            @Override
            public void onFailure(@NotNull Call<GetImageUrlResponse> call, @NotNull Throwable t) {
                onImageUrlFetched.onUrlFetchingFail();
            }
        });
    }

    public void clearData() {
        houseHoldProperties = null;
        residentsInHouseholdResponseLiveData = null;
        householdMappingRepository.clear();
        latlong = new MutableLiveData<>();
        hid = new MutableLiveData<>();
    }
}
