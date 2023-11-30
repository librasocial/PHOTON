package com.ssf.homevisit.repository;

import android.util.Log;
import android.view.WindowManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.*;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HouseHoldSearchRepository {
    private static final String TAG = HouseHoldSearchRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    public static MutableLiveData<SearchHouseholdResponse> searchHouseHoldResp = new MutableLiveData<>();
    public static MutableLiveData<SearchHouseholdResponse> searchHouseholdResponse = new MutableLiveData<>();

    public HouseHoldSearchRepository() {
        apiInterface  = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

}
