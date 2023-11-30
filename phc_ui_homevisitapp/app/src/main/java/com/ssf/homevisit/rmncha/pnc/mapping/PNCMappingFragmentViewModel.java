package com.ssf.homevisit.rmncha.pnc.mapping;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAHouseHoldSearchResponse;
import com.ssf.homevisit.models.RMNCHAPNCHouseHoldsRequest;
import com.ssf.homevisit.models.RMNCHAPNCHouseholdsResponse;
import com.ssf.homevisit.models.RMNCHAPNCWomenRequest;
import com.ssf.homevisit.models.RMNCHAPNCWomenResponse;
import com.ssf.homevisit.models.SearchHouseholdBody;
import com.ssf.homevisit.models.SearchHouseholdBodyProperty;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PNCMappingFragmentViewModel extends RMNCHABaseViewModel {

    private MutableLiveData<RMNCHAPNCHouseholdsResponse> householdsLiveData;
    private MutableLiveData<RMNCHAPNCWomenResponse> pwsLiveData;

    private MutableLiveData<RMNCHAHouseHoldSearchResponse> householdByHeadResponseLiveData;

    private static final String TAG = PNCMappingFragmentViewModel.class.getSimpleName();
    public static final String TYPE_VILLAGE = "Village";

    public PNCMappingFragmentViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RMNCHAPNCHouseholdsResponse> getPNCHouseholdResponseLiveData(String uuid) {
        householdsLiveData = new MutableLiveData<>();
        householdsLiveData = getPNCHouseHoldData(uuid);
        return householdsLiveData;
    }

    public MutableLiveData<RMNCHAPNCWomenResponse> getPNCWomenResponseLiveData(String uuid) {
        pwsLiveData = new MutableLiveData<>();
        pwsLiveData = getPNCWomenData(uuid);
        return pwsLiveData;
    }

    public LiveData<RMNCHAHouseHoldSearchResponse> getHouseHoldsByHeadName(String headName, String lgdCode) {
        householdByHeadResponseLiveData = new MutableLiveData<>();
        householdByHeadResponseLiveData = getHouseHoldWithHeadNameData(headName, lgdCode);
        return householdByHeadResponseLiveData;
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


    private MutableLiveData<RMNCHAPNCHouseholdsResponse> getPNCHouseHoldData(String uuid) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCHouseholdsResponse> data = new MutableLiveData<>();

        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getRMNCHAPNCHouseHoldsRequest(new RMNCHAPNCHouseHoldsRequest(uuid), 20,Util.getHeader()).enqueue(new Callback<RMNCHAPNCHouseholdsResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCHouseholdsResponse> call, Response<RMNCHAPNCHouseholdsResponse> response) {
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
            public void onFailure(Call<RMNCHAPNCHouseholdsResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }

    private MutableLiveData<RMNCHAPNCWomenResponse> getPNCWomenData(String uuid) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCWomenResponse> data = new MutableLiveData<>();

        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getRMNCHAPNCWomenRequest(new RMNCHAPNCWomenRequest(uuid, TYPE_VILLAGE), Util.getHeader()).enqueue(new Callback<RMNCHAPNCWomenResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCWomenResponse> call, Response<RMNCHAPNCWomenResponse> response) {
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
            public void onFailure(Call<RMNCHAPNCWomenResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }

        });

        return data;
    }


}
