
package com.ssf.homevisit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.ssf.homevisit.extensions.SingleLiveEvent;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.repository.HouseHoldSearchRepository;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VillageSESurveyViewModel extends AndroidViewModel {
    private SingleLiveEvent<SurveyFilterResponse> surveyFilterResponseLiveEvent;
    private HouseHoldSearchRepository houseHoldSearchRepository;
    private final ApiInterface apiInterface;
    private VillageProperties villageProperties;
    MutableLiveData<String> houseHeadName = new MutableLiveData<>();
    MutableLiveData<String> houseId = new MutableLiveData<>();


    public VillageSESurveyViewModel(@NonNull Application application, VillageProperties villageProperties) {
        super(application);
        this.villageProperties = villageProperties;
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public SingleLiveEvent<SurveyFilterResponse> getSurveyMaster() {
        surveyFilterResponseLiveEvent = new SingleLiveEvent<>();
        apiInterface.getSurveyFilterResponse("Village", 1, 1, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyFilterResponse>() {
            @Override
            public void onResponse(Call<SurveyFilterResponse> call, Response<SurveyFilterResponse> response) {
                if (response.body() != null)
                    surveyFilterResponseLiveEvent.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SurveyFilterResponse> call, Throwable t) {
                surveyFilterResponseLiveEvent.setValue(null);
            }
        });
        return surveyFilterResponseLiveEvent;
    }

    public SingleLiveEvent<SurveyFilterResponse> getSurveyMasterResponse() {
        surveyFilterResponseLiveEvent = new SingleLiveEvent<>();
        apiInterface.getSurveyFilterResponse("Village", 1, 2, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyFilterResponse>() {
            @Override
            public void onResponse(Call<SurveyFilterResponse> call, Response<SurveyFilterResponse> response) {
                if (response.body() != null)
                    surveyFilterResponseLiveEvent.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SurveyFilterResponse> call, Throwable t) {
                surveyFilterResponseLiveEvent.setValue(null);
            }
        });
        return surveyFilterResponseLiveEvent;
    }
}
