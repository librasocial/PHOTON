package com.ssf.homevisit.rmncha.ec.selectspouse;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAECRegistrationRequest;
import com.ssf.homevisit.models.RMNCHAECRegistrationResponse;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.rmncha.ec.mapping.ECMappingFragmentViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSpouseForECViewModel extends RMNCHABaseViewModel {

    MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> menContentListLiveData = new MutableLiveData<>();
    private static final String TAG = ECMappingFragmentViewModel.class.getSimpleName();

    public SelectSpouseForECViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> getAllMembersInHouseHoldLiveData(String uuid) {
        menContentListLiveData = new MutableLiveData<>();
        menContentListLiveData = getAllHouseHoldData(uuid);
        return menContentListLiveData;
    }

    private MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> getAllHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAMembersInHouseHoldResponse(uuid, Util.getHeader()).enqueue(new Callback<RMNCHAMembersInHouseHoldResponse>() {
            @Override
            public void onResponse(Call<RMNCHAMembersInHouseHoldResponse> call, Response<RMNCHAMembersInHouseHoldResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    RMNCHAMembersInHouseHoldResponse responseData = response.body();
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
            public void onFailure(Call<RMNCHAMembersInHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(new ArrayList<>());
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAECRegistrationResponse> makeECRegistration(RMNCHAECRegistrationRequest rmnchaECRegistrationRequest) {
        clearErrorResponse();
        MutableLiveData<RMNCHAECRegistrationResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makeECRegistration(rmnchaECRegistrationRequest, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAECRegistrationResponse>() {
            @Override
            public void onResponse(Call<RMNCHAECRegistrationResponse> call, Response<RMNCHAECRegistrationResponse> response) {
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
            public void onFailure(Call<RMNCHAECRegistrationResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });

        return data;
    }
}
