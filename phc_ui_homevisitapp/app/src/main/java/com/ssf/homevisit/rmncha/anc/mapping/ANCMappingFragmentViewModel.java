package com.ssf.homevisit.rmncha.anc.mapping;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAANCHouseHoldsRequest;
import com.ssf.homevisit.models.RMNCHAANCHouseholdsResponse;
import com.ssf.homevisit.models.RMNCHAANCPWsRequest;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ANCMappingFragmentViewModel extends RMNCHABaseViewModel {

    private MutableLiveData<RMNCHAANCHouseholdsResponse> householdsLiveData;
    private MutableLiveData<RMNCHAANCPWsResponse> pwsLiveData;
    private static final String TAG = ANCMappingFragmentViewModel.class.getSimpleName();
    public static final String TYPE_VILLAGE = "Village";

    public ANCMappingFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RMNCHAANCHouseholdsResponse> getANCHouseholdResponseLiveData(String uuid) {
        householdsLiveData = new MutableLiveData<>();
        householdsLiveData = getANCHouseHoldData(uuid);
        return householdsLiveData;
    }

    public MutableLiveData<RMNCHAANCPWsResponse> getANCPWsResponseLiveData(String uuid) {
        pwsLiveData = new MutableLiveData<>();
        pwsLiveData = getANCPwsData(uuid);
        return pwsLiveData;
    }

    private MutableLiveData<RMNCHAANCHouseholdsResponse> getANCHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<RMNCHAANCHouseholdsResponse> data = new MutableLiveData<>();

        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getRMNCHAANCHouseHoldsRequest(new RMNCHAANCHouseHoldsRequest(uuid), 0,200,Util.getHeader()).enqueue(new Callback<RMNCHAANCHouseholdsResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCHouseholdsResponse> call, Response<RMNCHAANCHouseholdsResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                        RMNCHAANCHouseholdsResponse RMNCHAANCHouseholdsResponse = response.body();
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
            public void onFailure(Call<RMNCHAANCHouseholdsResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }

    private MutableLiveData<RMNCHAANCPWsResponse> getANCPwsData(String uuid) {
        clearErrorResponse();
        MutableLiveData<RMNCHAANCPWsResponse> data = new MutableLiveData<>();

        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getRMNCHAANCPWsRequest(new RMNCHAANCPWsRequest(uuid, TYPE_VILLAGE,2),0,200, Util.getHeader()).enqueue(new Callback<RMNCHAANCPWsResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCPWsResponse> call, Response<RMNCHAANCPWsResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                        RMNCHAANCPWsResponse rmnchaancpWsResponse = response.body();
                        List<RMNCHAANCPWsResponse.Content> contentList = rmnchaancpWsResponse.getContent();
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
            public void onFailure(Call<RMNCHAANCPWsResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }


}
