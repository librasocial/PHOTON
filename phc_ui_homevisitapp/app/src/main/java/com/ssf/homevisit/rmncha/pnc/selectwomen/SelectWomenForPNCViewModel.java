package com.ssf.homevisit.rmncha.pnc.selectwomen;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAPNCWomenInHHRequest;
import com.ssf.homevisit.models.RMNCHAPNCWomenResponse;
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

public class SelectWomenForPNCViewModel extends RMNCHABaseViewModel {

    private final ApiInterface apiInterface;
    MutableLiveData<List<RMNCHAPNCWomenResponse.Content>> womenContentListLiveData = new MutableLiveData<>();
    private static final String TAG = SelectWomenForPNCViewModel.class.getSimpleName();
    public static final String TYPE_HOUSEHOLD = "HouseHold";

    public SelectWomenForPNCViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public MutableLiveData<List<RMNCHAPNCWomenResponse.Content>> getPNCWomenMembersInHouseHoldLiveData(String uuid) {
        womenContentListLiveData = new MutableLiveData<>();
        womenContentListLiveData = getAllWomenInHouseHoldData(uuid);
        return womenContentListLiveData;
    }

    private MutableLiveData<List<RMNCHAPNCWomenResponse.Content>> getAllWomenInHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAPNCWomenResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAPNCWomenInHHRequest(new RMNCHAPNCWomenInHHRequest(uuid), Util.getHeader()).enqueue(new Callback<RMNCHAPNCWomenResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCWomenResponse> call, Response<RMNCHAPNCWomenResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    RMNCHAPNCWomenResponse responseData = response.body();
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
            public void onFailure(Call<RMNCHAPNCWomenResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    private List<RMNCHAPNCWomenResponse.Content> filterPNCEligibleWomenMember(RMNCHAPNCWomenResponse response) {
        List<RMNCHAPNCWomenResponse.Content> womenContentList = new ArrayList<>();
        int age = -1;
        try {
            if (response != null) {
                List<RMNCHAPNCWomenResponse.Content> contentList = response.getContent();
                for (RMNCHAPNCWomenResponse.Content content : contentList) {
                    boolean isHasNewBorn = content.getTarget().getProperties().isHasNewBorn();
                    String ageStr = content.getSource().getProperties().getAge();
                    if (ageStr != null && ageStr.length() > 0)
                        age = (int) Float.parseFloat(ageStr);
                    if (isHasNewBorn && age > 15 && age < 49) {
                        womenContentList.add(content);
                    }
                }
            }
        } catch (Exception e) {
            womenContentList = response.getContent();
            e.printStackTrace();
        }
        return womenContentList;
    }


}
