package com.ssf.homevisit.rmncha.ec.mapping;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAHouseHoldSearchResponse;
import com.ssf.homevisit.models.RMNCHANearMEHouseHoldRequest;
import com.ssf.homevisit.models.SearchHouseholdBody;
import com.ssf.homevisit.models.SearchHouseholdBodyProperty;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ECMappingFragmentViewModel extends RMNCHABaseViewModel {

    private MutableLiveData<RMNCHAHouseHoldResponse> householdFromVillageResponseLiveData;
    private MutableLiveData<RMNCHAHouseHoldSearchResponse> householdByHeadResponseLiveData;
    private static final String TAG = ECMappingFragmentViewModel.class.getSimpleName();

    public ECMappingFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RMNCHAHouseHoldResponse> getAllHouseholdResponseLiveData(String uuid) {
        householdFromVillageResponseLiveData = new MutableLiveData<>();
        householdFromVillageResponseLiveData = getAllHouseHoldData(uuid);
        return householdFromVillageResponseLiveData;
    }

    public LiveData<RMNCHAHouseHoldResponse> getAllHouseholdByLatLongResponseLiveData(String uuid, Double lat, Double lon) {
        householdFromVillageResponseLiveData = new MutableLiveData<>();
        householdFromVillageResponseLiveData = getNearMeHouseHoldData(new RMNCHANearMEHouseHoldRequest(uuid, lat, lon));
        return householdFromVillageResponseLiveData;
    }

    public LiveData<RMNCHAHouseHoldSearchResponse> getHouseHoldsByHeadName(String headName, String lgdCode) {
        householdByHeadResponseLiveData = new MutableLiveData<>();
        householdByHeadResponseLiveData = getHouseHoldWithHeadNameData(headName, lgdCode);
        return householdByHeadResponseLiveData;
    }

    private MutableLiveData<RMNCHAHouseHoldResponse> getAllHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<RMNCHAHouseHoldResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAHouseHoldResponse(uuid, 0, 20, Util.getHeader()).enqueue(new Callback<RMNCHAHouseHoldResponse>() {
            @Override
            public void onResponse(Call<RMNCHAHouseHoldResponse> call, Response<RMNCHAHouseHoldResponse> response) {
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
            public void onFailure(Call<RMNCHAHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }

    private MutableLiveData<RMNCHAHouseHoldResponse> getNearMeHouseHoldData(RMNCHANearMEHouseHoldRequest rmnchaNearMEHouseHoldRequest) {
        clearErrorResponse();
        MutableLiveData<RMNCHAHouseHoldResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHANearMEHouseHoldResponse(rmnchaNearMEHouseHoldRequest, "application/json", Util.getHeader()).enqueue(new Callback<RMNCHAHouseHoldResponse>() {
            @Override
            public void onResponse(Call<RMNCHAHouseHoldResponse> call, Response<RMNCHAHouseHoldResponse> response) {
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
            public void onFailure(Call<RMNCHAHouseHoldResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }

    private MutableLiveData<RMNCHAHouseHoldSearchResponse> getHouseHoldWithHeadNameData(String headName, String lgdCode) {
        clearErrorResponse();
        MutableLiveData<RMNCHAHouseHoldSearchResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAHouseHoldWithHeadNameResponse(headName, getRequestBody(lgdCode), Util.getHeader()).enqueue(new Callback<RMNCHAHouseHoldSearchResponse>() {
            @Override
            public void onResponse(Call<RMNCHAHouseHoldSearchResponse> call, Response<RMNCHAHouseHoldSearchResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body village " + response.body());
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                        RMNCHAHouseHoldSearchResponse mRMNCHAHouseHoldResponse = response.body();
                        List<RMNCHAHouseHoldSearchResponse.Content> contentList = mRMNCHAHouseHoldResponse.getContent();
                        Log.d("Data", new Gson().toJson(response.body().getContent()));
                        System.out.println("ASD" + new Gson().toJson(contentList));
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
            public void onFailure(Call<RMNCHAHouseHoldSearchResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }

    private SearchHouseholdBody getRequestBody(String lgdCode) {
        SearchHouseholdBody body = new SearchHouseholdBody();
        body.setType("HouseHold");
        body.setPage(0);
        body.setSize(5000);
        SearchHouseholdBodyProperty property = new SearchHouseholdBodyProperty();
        property.setVillageId(lgdCode);
        body.setProperties(property);
        return body;
    }

}
