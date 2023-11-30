package com.ssf.homevisit.rmncha.ec.selectwomen;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectWomenForECViewModel extends RMNCHABaseViewModel {

    MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> womenContentListLiveData = new MutableLiveData<>();
    private static final String TAG = SelectWomenForECViewModel.class.getSimpleName();

    public SelectWomenForECViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> getWomenMembersInHouseHoldLiveData(String uuid) {
        womenContentListLiveData = new MutableLiveData<>();
        womenContentListLiveData = getAllHouseHoldData(uuid);
        return womenContentListLiveData;
    }

    private MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> getAllHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAMembersInHouseHoldResponse(uuid, Util.getHeader()).enqueue(new Callback<RMNCHAMembersInHouseHoldResponse>() {
            @Override
            public void onResponse(Call<RMNCHAMembersInHouseHoldResponse> call, Response<RMNCHAMembersInHouseHoldResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHAMembersInHouseHoldResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(filterEligibleWomenMember(responseData));
                        Log.d("Data", new Gson().toJson(responseData.getContent()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
                    } else {
                        setErrorResponse(response);
                        data.setValue(new ArrayList<>());
                    }
                }else {
                    setErrorResponse(response);
                    data.setValue(new ArrayList<>());
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

    private List<RMNCHAMembersInHouseHoldResponse.Content> filterEligibleWomenMember(RMNCHAMembersInHouseHoldResponse response) {
        String gender = "";
        Integer age = -1;
        List<RMNCHAMembersInHouseHoldResponse.Content> womenContentList = new ArrayList<>();
        if (response != null) {
            List<RMNCHAMembersInHouseHoldResponse.Content> contentList = response.getContent();
            for (RMNCHAMembersInHouseHoldResponse.Content content : contentList) {
                gender = content.getTargetNode().getProperties().getGender();
                age = content.getTargetNode().getProperties().getAge();
                if (age != null && age != -1 && gender != null && !gender.isEmpty()) {
                    if ("Female".equalsIgnoreCase(gender) && age > 15 && age < 49) {
                        womenContentList.add(content);
                    }
                }
            }
        }
        return womenContentList;
    }

}
