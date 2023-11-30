package com.ssf.homevisit.rmncha.base;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.AshaWorkerResponse;
import com.ssf.homevisit.models.FormUtilityResponse;
import com.ssf.homevisit.models.RMNCHAANCErrorResponse;
import com.ssf.homevisit.models.RMNCHAANCRegistrationResponse;
import com.ssf.homevisit.models.RMNCHAANCServiceHistoryResponse;
import com.ssf.homevisit.models.RMNCHAANCVisitLogResponse;
import com.ssf.homevisit.models.RMNCHACoupleDetailsRequest;
import com.ssf.homevisit.models.RMNCHACoupleDetailsResponse;
import com.ssf.homevisit.models.RMNCHAPNCDeliveryOutcomesResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RMNCHABaseViewModel extends AndroidViewModel {

    private static final String TAG = RMNCHABaseViewModel.class.getSimpleName();
    protected final ApiInterface apiInterface;
    private RMNCHAANCErrorResponse errorResponse;

    public RMNCHABaseViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public MutableLiveData<List<AshaWorkerResponse.Content>> getAshaWorkerLiveData(String uuid) {
        clearErrorResponse();
        MutableLiveData<List<AshaWorkerResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getAshaWorkerResponse(uuid, Util.getHeader()).enqueue(new Callback<AshaWorkerResponse>() {
            @Override
            public void onResponse(Call<AshaWorkerResponse> call, Response<AshaWorkerResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    AshaWorkerResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getContent());
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
            public void onFailure(Call<AshaWorkerResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<RMNCHACoupleDetailsResponse.Content>> getRMNCHACoupleDetailsLiveData(String uuid) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHACoupleDetailsResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHACoupleDetailsRequest(new RMNCHACoupleDetailsRequest(uuid), Util.getHeader()).enqueue(new Callback<RMNCHACoupleDetailsResponse>() {
            @Override
            public void onResponse(Call<RMNCHACoupleDetailsResponse> call, Response<RMNCHACoupleDetailsResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    RMNCHACoupleDetailsResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getContent());
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
            public void onFailure(Call<RMNCHACoupleDetailsResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAPNCDeliveryOutcomesResponse> getDeliveryOutcomesData(String pncRegistrationId) {
        MutableLiveData<RMNCHAPNCDeliveryOutcomesResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getPNCDeliveryOutcomesRegistrationData(pncRegistrationId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCDeliveryOutcomesResponse>() {
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

    public MutableLiveData<RMNCHAANCRegistrationResponse> getANCRegistrationData(String ancRegistrationId) {
        clearErrorResponse();
        MutableLiveData<RMNCHAANCRegistrationResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getANCRegistration(ancRegistrationId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAANCRegistrationResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCRegistrationResponse> call, Response<RMNCHAANCRegistrationResponse> response) {
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
            public void onFailure(Call<RMNCHAANCRegistrationResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<RMNCHAANCVisitLogResponse>> getANCServiceHistoryLiveData(String ancServiceId) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAANCVisitLogResponse>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAANCServiceHistoryRequest(ancServiceId, Util.getHeader()).enqueue(new Callback<RMNCHAANCServiceHistoryResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCServiceHistoryResponse> call, Response<RMNCHAANCServiceHistoryResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    RMNCHAANCServiceHistoryResponse responseData = response.body();
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
            public void onFailure(Call<RMNCHAANCServiceHistoryResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public String getErrorMessage() {
        String message = "";
        if (errorResponse != null) {
            List<RMNCHAANCErrorResponse.Error> errors = errorResponse.getErrors();
            if (errors != null && errors.size() > 0) {
                message = errors.get(0).getErrorCode() + " - " + errors.get(0).getMessage();
            }
        }

        return message;
    }

    protected void setErrorResponse(Response response) {
        try {
            errorResponse = new Gson().fromJson(response.errorBody().string(), RMNCHAANCErrorResponse.class);
        } catch (Exception e) {
            RMNCHAANCErrorResponse tempResp = new RMNCHAANCErrorResponse();
            List<RMNCHAANCErrorResponse.Error> temErrorList = new ArrayList<>();
            RMNCHAANCErrorResponse.Error error = new RMNCHAANCErrorResponse.Error();
            error.setErrorCode(response.code() + "");
            error.setMessage(response.message() + "");
            temErrorList.add(error);
            tempResp.setErrors(temErrorList);
            errorResponse = tempResp;
            e.printStackTrace();
        }
    }

    protected void clearErrorResponse() {
        errorResponse = null;
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
