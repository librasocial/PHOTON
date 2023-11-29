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
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.repository.HouseHoldLevelMappingRepository;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;

public class FamilyMappingViewModel extends AndroidViewModel {

    private final ApiInterface apiInterface;
    private final HouseHoldLevelMappingRepository householdMappingRepository;
    private LiveData<ResidentInHouseHoldResponse> residentsInHouseholdResponseLiveData;

    public FamilyMappingViewModel(@NonNull Application application) {
        super(application);
        householdMappingRepository = new HouseHoldLevelMappingRepository();
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }
    public LiveData<ResidentInHouseHoldResponse> getResidentsInHouseholdData(String houseUuid) {
        residentsInHouseholdResponseLiveData = new MutableLiveData<>();
        residentsInHouseholdResponseLiveData = householdMappingRepository.getResidentsInHousehold(houseUuid, 0, 50);
        residentsInHouseholdResponseLiveData = HouseHoldLevelMappingRepository.residentsData;
        return residentsInHouseholdResponseLiveData;
    }

    public void createCitizen(ResidentProperties residentProperties, String householdUuid, OnCitizenCreated onCitizenCreated) {
        CreateResidentBody createResidentBody = new CreateResidentBody();
        createResidentBody.setType("Citizen");
        residentProperties.setHouseHold(householdUuid);
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
                Log.d("shrey", responseString);
                onCitizenCreated.onCitizenCreationFailed();
            }
        });
    }

}
