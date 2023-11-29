package com.ssf.homevisit.rmncha.anc.selectwomen;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAANCPWsRequest;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectWomenForANCViewModel extends RMNCHABaseViewModel {

    MutableLiveData<List<RMNCHAANCPWsResponse.Content>> pwsContentListLiveData = new MutableLiveData<>();
    private static final String TAG = SelectWomenForANCViewModel.class.getSimpleName();
    public static final String TYPE_HOUSEHOLD = "HouseHold";

    public SelectWomenForANCViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<RMNCHAANCPWsResponse.Content>> getANCPWsInHouseHoldLiveData(String uuid) {
        pwsContentListLiveData = new MutableLiveData<>();
        pwsContentListLiveData = getAllHouseHoldData(uuid);
        return pwsContentListLiveData;
    }

    private MutableLiveData<List<RMNCHAANCPWsResponse.Content>> getAllHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAANCPWsResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAANCPWsRequest(new RMNCHAANCPWsRequest(uuid, TYPE_HOUSEHOLD,1), 0,200,Util.getHeader()).enqueue(new Callback<RMNCHAANCPWsResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCPWsResponse> call, Response<RMNCHAANCPWsResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    RMNCHAANCPWsResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(filterANCEligibleWomenMember(responseData));
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

    private List<RMNCHAANCPWsResponse.Content> filterANCEligibleWomenMember(RMNCHAANCPWsResponse response) {
        List<RMNCHAANCPWsResponse.Content> womenContentList = new ArrayList<>();
        if (response != null) {
            List<RMNCHAANCPWsResponse.Content> contentList = response.getContent();
            for (RMNCHAANCPWsResponse.Content content : contentList) {
                boolean isPregnant = content.getSource().getProperties().isPregnant();
                if (isPregnant) {
                    womenContentList.add(content);
                }
            }
        }
        return womenContentList;
    }

}
