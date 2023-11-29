package com.ssf.homevisit.rmncha.ec.ecservice;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHACoupleResponse;
import com.ssf.homevisit.models.RMNCHACreateECServiceRequest;
import com.ssf.homevisit.models.RMNCHACreateECServiceResponse;
import com.ssf.homevisit.models.RMNCHACreateVisitLogRequest;
import com.ssf.homevisit.models.RMNCHACreateVisitLogResponse;
import com.ssf.homevisit.models.RMNCHAVisitLogsResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ECServiceViewModel extends RMNCHABaseViewModel {

    private static final String TAG = ECServiceViewModel.class.getSimpleName();

    public ECServiceViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RMNCHACoupleResponse.Content> getCoupleDetailsLiveData(String uuid) {
        return getCoupleLiveData(uuid);
    }

    public MutableLiveData<List<RMNCHAVisitLogsResponse.VisitLog>> getBCVisitLogsLiveData(String serviceID) {
        return getVisitLogsLiveData(serviceID);
    }

    public MutableLiveData<RMNCHACreateECServiceResponse> makeECServiceRequest(RMNCHACreateECServiceRequest request) {
        return makeECServiceLiveData(request);
    }

    public MutableLiveData<RMNCHACreateVisitLogResponse> makeCreateVisitLogRequest(String serviceID, RMNCHACreateVisitLogRequest request) {
        return makeCreateVisitLogLiveData(serviceID, request);
    }

    private MutableLiveData<RMNCHACoupleResponse.Content> getCoupleLiveData(String uuid) {
        clearErrorResponse();
        MutableLiveData<RMNCHACoupleResponse.Content> data = new MutableLiveData<>();
        AppController.getInstance().getECServiceActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHACoupleResponse(uuid, Util.getHeader()).enqueue(new Callback<RMNCHACoupleResponse>() {
            @Override
            public void onResponse(Call<RMNCHACoupleResponse> call, Response<RMNCHACoupleResponse> response) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHACoupleResponse responseData = response.body();
                    if (responseData != null && responseData.getContent() != null && responseData.getContent().size() > 0) {
                        data.setValue(responseData.getContent().get(0));
                        Log.d("Data", new Gson().toJson(responseData.getContent()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
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
            public void onFailure(Call<RMNCHACoupleResponse> call, Throwable t) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    private MutableLiveData<List<RMNCHAVisitLogsResponse.VisitLog>> getVisitLogsLiveData(String serviceID) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAVisitLogsResponse.VisitLog>> data = new MutableLiveData<>();
        AppController.getInstance().getECServiceActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAVisitLogsResponse(serviceID, Util.getHeader()).enqueue(new Callback<RMNCHAVisitLogsResponse>() {
            @Override
            public void onResponse(Call<RMNCHAVisitLogsResponse> call, Response<RMNCHAVisitLogsResponse> response) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHAVisitLogsResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getData());
                        Log.d("Data", new Gson().toJson(responseData.getData()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
                    } else {
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<RMNCHAVisitLogsResponse> call, Throwable t) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    private MutableLiveData<RMNCHACreateECServiceResponse> makeECServiceLiveData(RMNCHACreateECServiceRequest request) {
        clearErrorResponse();
        MutableLiveData<RMNCHACreateECServiceResponse> data = new MutableLiveData<>();
        AppController.getInstance().getECServiceActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makeRMNCHACreateECServiceRequest(request, Util.getHeader()).enqueue(new Callback<RMNCHACreateECServiceResponse>() {
            @Override
            public void onResponse(Call<RMNCHACreateECServiceResponse> call, Response<RMNCHACreateECServiceResponse> response) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHACreateECServiceResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData);
                        Log.d("Data", new Gson().toJson(responseData));
                        System.out.println("ASD" + new Gson().toJson(responseData));
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
            public void onFailure(Call<RMNCHACreateECServiceResponse> call, Throwable t) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    private MutableLiveData<RMNCHACreateVisitLogResponse> makeCreateVisitLogLiveData(String serviceID, RMNCHACreateVisitLogRequest request) {
        clearErrorResponse();
        MutableLiveData<RMNCHACreateVisitLogResponse> data = new MutableLiveData<>();
        AppController.getInstance().getECServiceActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makeRMNCHACreateVisitLogRequest(serviceID, request, Util.getHeader()).enqueue(new Callback<RMNCHACreateVisitLogResponse>() {
            @Override
            public void onResponse(Call<RMNCHACreateVisitLogResponse> call, Response<RMNCHACreateVisitLogResponse> response) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHACreateVisitLogResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData);
                        Log.d("Data", new Gson().toJson(responseData));
                        System.out.println("ASD" + new Gson().toJson(responseData));
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
            public void onFailure(Call<RMNCHACreateVisitLogResponse> call, Throwable t) {
                AppController.getInstance().getECServiceActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

}
