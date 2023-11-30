package com.ssf.homevisit.rmncha.pnc.details;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesRequest;
import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantRequest;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;
import com.ssf.homevisit.models.RMNCHAPNCMemberResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PNCDetailsViewModel extends RMNCHABaseViewModel {

    private final ApiInterface apiInterface;
    MutableLiveData<List<RMNCHAPNCMemberResponse.Content>> womenContentListLiveData = new MutableLiveData<>();
    private static final String TAG = PNCDetailsViewModel.class.getSimpleName();

    public PNCDetailsViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public MutableLiveData<List<AshaWorkerResponse.Content>> getAshaWorkerLiveData(String uuid) {
        MutableLiveData<List<AshaWorkerResponse.Content>> ashaWorkerContentListLiveData = new MutableLiveData<>();
        ashaWorkerContentListLiveData = getAshaWorkerData(uuid);
        return ashaWorkerContentListLiveData;
    }

    public MutableLiveData<List<RMNCHAPNCMemberResponse.Content>> getPNCWomenMemberLiveData(String uuid) {
        womenContentListLiveData = new MutableLiveData<>();
        womenContentListLiveData = getPNCMembersResponse(uuid);
        return womenContentListLiveData;
    }

    private MutableLiveData<List<RMNCHAPNCMemberResponse.Content>> getPNCMembersResponse(String uuid) {
        MutableLiveData<List<RMNCHAPNCMemberResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAPNCMembersResponse(uuid, Util.getHeader()).enqueue(new Callback<RMNCHAPNCMemberResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCMemberResponse> call, Response<RMNCHAPNCMemberResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHAPNCMemberResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getContent());
                        Log.d("Data", new Gson().toJson(responseData.getContent()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
                    } else {
                        data.setValue(new ArrayList<>());
                    }
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCMemberResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(new ArrayList<>());
            }
        });
        return data;
    }

    private MutableLiveData<List<AshaWorkerResponse.Content>> getAshaWorkerData(String uuid) {
        MutableLiveData<List<AshaWorkerResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getPncLandingActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getAshaWorkerResponse(uuid, Util.getHeader()).enqueue(new Callback<AshaWorkerResponse>() {
            @Override
            public void onResponse(Call<AshaWorkerResponse> call, Response<AshaWorkerResponse> response) {
                AppController.getInstance().getPncLandingActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    AshaWorkerResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getContent());
                        Log.d("Data", new Gson().toJson(responseData.getContent()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
                    } else {
                        data.setValue(new ArrayList<>());
                    }
                }
            }

            @Override
            public void onFailure(Call<AshaWorkerResponse> call, Throwable t) {
                AppController.getInstance().getPncLandingActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(new ArrayList<>());
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAPNCDeliveryOutcomesResponse> makePNCDeliveryOutcomesRegistrationRequest(RMNCHAPNCDeliveryOutcomesRequest rmnchapncDeliveryOutcomesRequest) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCDeliveryOutcomesResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makePNCDeliveryOutcomesRegistration(rmnchapncDeliveryOutcomesRequest, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCDeliveryOutcomesResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCDeliveryOutcomesResponse> call, Response<RMNCHAPNCDeliveryOutcomesResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCDeliveryOutcomesResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<RMNCHAPNCInfantResponse> makePNCInfantRegistrationRequest(String pncRegistrationId, RMNCHAPNCInfantRequest rmnchapncInfantRequest) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCInfantResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makePNCInfantRegistration(pncRegistrationId, rmnchapncInfantRequest, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCInfantResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCInfantResponse> call, Response<RMNCHAPNCInfantResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCInfantResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });

        return data;
    }

    public MutableLiveData<List<FormUtilityResponse.SurveyFormResponse>> getEcUtilityData(String url) {
        MutableLiveData<List<FormUtilityResponse.SurveyFormResponse>> data = new MutableLiveData<>();
        String token= "Bearer "+ Util.getHeader();
        apiInterface.getEcUtilityData(url,token).enqueue(new Callback<FormUtilityResponse>() {
            @Override
            public void onResponse(Call<FormUtilityResponse> call, Response<FormUtilityResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    FormUtilityResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getData());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<FormUtilityResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

}
