package com.ssf.homevisit.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.extensions.SingleLiveEvent;
import com.ssf.homevisit.interfaces.OnImageUrlFetched;
import com.ssf.homevisit.interfaces.OnSureveyAnswerPosted;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.repository.HouseHoldLevelMappingRepository;
import com.ssf.homevisit.repository.HouseHoldSearchRepository;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class HHSESurveyAlertViewModel extends AndroidViewModel {
    private SingleLiveEvent<SurveyFilterResponse> surveyFilterResponseLiveEvent;
    private HouseHoldSearchRepository houseHoldSearchRepository;
    private final ApiInterface apiInterface;
    private HouseHoldProperties houseHoldProperties;
    public MutableLiveData<String> houseHeadName = new MutableLiveData<>();
    public MutableLiveData<String> houseId = new MutableLiveData<>();
    private MutableLiveData<ResidentInHouseHoldResponse> residentsInHouseholdResponseLiveData;
    private MutableLiveData<SurveyAnswersResponse> surveyAnswersResponseMutableLiveData;


    public HHSESurveyAlertViewModel(@NonNull Application application, HouseHoldProperties houseHoldProperties) {
        super(application);
        houseId.setValue(houseHoldProperties.getHouseID());
        houseHeadName.setValue(houseHoldProperties.getHouseHeadName());
        this.houseHoldProperties = houseHoldProperties;
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public SingleLiveEvent<SurveyFilterResponse> getSurveyMaster(String surveyType) {
        surveyFilterResponseLiveEvent = new SingleLiveEvent<>();
        apiInterface.getSurveyFilterResponse(surveyType, 1, 1, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyFilterResponse>() {
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
        apiInterface.getSurveyFilterResponse("HouseHold", 2, 1, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyFilterResponse>() {
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

    public void postSurveyAnswers(String surveyId, String houseUuid, SurveyAnswersBody surveyAnswersBody, OnSureveyAnswerPosted onSureveyAnswerPosted) {
        SurveyAnswerContext surveyAnswerContext = new SurveyAnswerContext();
        surveyAnswerContext.sethId(houseUuid);
        surveyAnswersBody.setContext(surveyAnswerContext);
        surveyAnswersBody.setSurveyName("HouseHold");
        surveyAnswersBody.setSurveyType("HouseHold");
        apiInterface.postSurveyAnswers(surveyAnswersBody, surveyId, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyAnswersResponse>() {
            @Override
            public void onResponse(Call<SurveyAnswersResponse> call, Response<SurveyAnswersResponse> response) {
                onSureveyAnswerPosted.onPosted(response.body());
            }
            @Override
            public void onFailure(Call<SurveyAnswersResponse> call, Throwable t) {
                onSureveyAnswerPosted.onFail(t);
            }
        });
    }

    public LiveData<ResidentInHouseHoldResponse> getResidentsInHouseholdData() {
        residentsInHouseholdResponseLiveData = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getResidentsInHousehold("HouseHold", houseHoldProperties.getUuid(), "RESIDESIN", "Citizen", 0, 50, Util.getHeader()).enqueue(new Callback<ResidentInHouseHoldResponse>() {
            @Override
            public void onResponse(Call<ResidentInHouseHoldResponse> call, Response<ResidentInHouseHoldResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        residentsInHouseholdResponseLiveData.setValue(response.body());
                        ResidentInHouseHoldResponse allPlacesResponse = response.body();
                        List<ResidentInHouseHoldResponse.Content> contentList = allPlacesResponse.getContent();

                    }
                } else {
                    residentsInHouseholdResponseLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResidentInHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        return residentsInHouseholdResponseLiveData;
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

    public void setHousehold(HouseHoldProperties houseHoldProperties) {
        this.houseHoldProperties = houseHoldProperties;
        this.houseHeadName.setValue(houseHoldProperties.getHouseHeadName());
        this.houseId.setValue(houseHoldProperties.getHouseID());
    }

    public void postIndividualSurveyAnswers(String surveyId, SurveyAnswersBody surveyAnswersBody, OnSureveyAnswerPosted onSureveyAnswerPosted) {
        apiInterface.postSurveyAnswers(surveyAnswersBody, surveyId, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyAnswersResponse>() {
            @Override
            public void onResponse(Call<SurveyAnswersResponse> call, Response<SurveyAnswersResponse> response) {
                onSureveyAnswerPosted.onPosted(response.body());
            }

            @Override
            public void onFailure(Call<SurveyAnswersResponse> call, Throwable t) {
                onSureveyAnswerPosted.onFail(t);
            }
        });
    }

    public LiveData<SurveyAnswersResponse> getSurveyResponse(String contextId, String surveyType) {
        surveyAnswersResponseMutableLiveData = new MutableLiveData<>();
        apiInterface.filterSurveyByContextId(contextId, "Bearer "+Util.getHeader(), surveyType).enqueue(new Callback<SurveyAnswersResponse>() {
            @Override
            public void onResponse(Call<SurveyAnswersResponse> call, Response<SurveyAnswersResponse> response) {
                if (response.code() == 200) {
                    surveyAnswersResponseMutableLiveData.setValue(response.body());
                } else {
                    surveyAnswersResponseMutableLiveData.setValue(null);
                }
            }
            @Override
            public void onFailure(Call<SurveyAnswersResponse> call, Throwable t) {
                surveyAnswersResponseMutableLiveData.setValue(null);
            }
        });
        return surveyAnswersResponseMutableLiveData;
    }
}
