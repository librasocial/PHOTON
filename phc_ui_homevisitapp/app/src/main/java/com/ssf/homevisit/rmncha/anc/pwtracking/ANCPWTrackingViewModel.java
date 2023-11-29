package com.ssf.homevisit.rmncha.anc.pwtracking;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAANCRegistrationResponse;
import com.ssf.homevisit.models.RMNCHAANCServiceHistoryResponse;
import com.ssf.homevisit.models.RMNCHAANCServiceRequest;
import com.ssf.homevisit.models.RMNCHAANCServiceResponse;
import com.ssf.homevisit.models.RMNCHAANCVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAANCVisitLogResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ANCPWTrackingViewModel extends RMNCHABaseViewModel {

    private static final String TAG = ANCPWTrackingViewModel.class.getSimpleName();

    public ANCPWTrackingViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RMNCHAANCServiceResponse> makeANCServiceRequest(RMNCHAANCServiceRequest request) {
        clearErrorResponse();
        MutableLiveData<RMNCHAANCServiceResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makeANCServiceRequest(request, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAANCServiceResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCServiceResponse> call, Response<RMNCHAANCServiceResponse> response) {
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
            public void onFailure(Call<RMNCHAANCServiceResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAANCVisitLogResponse> makeANCVisitLogRequest(RMNCHAANCVisitLogRequest request, String ancServiceId) {
        clearErrorResponse();
        MutableLiveData<RMNCHAANCVisitLogResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makeANCVisitLogCall(request, ancServiceId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAANCVisitLogResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCVisitLogResponse> call, Response<RMNCHAANCVisitLogResponse> response) {
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
            public void onFailure(Call<RMNCHAANCVisitLogResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

}
