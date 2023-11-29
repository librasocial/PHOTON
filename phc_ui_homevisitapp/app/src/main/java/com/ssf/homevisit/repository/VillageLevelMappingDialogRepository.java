package com.ssf.homevisit.repository;

import android.util.Log;
import android.view.WindowManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.PlacesInVillageResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class VillageLevelMappingDialogRepository {
    private static final String TAG = VillageLevelMappingRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    public static MutableLiveData<PlacesInVillageResponse> placesData = new MutableLiveData<>();

    public VillageLevelMappingDialogRepository() {
        apiInterface  = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public LiveData<PlacesInVillageResponse> getPlacesInVillageData(String villageId, int pageNo, int size) {
        Log.d("requested Village", villageId);
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getPlacesInVillageResponse("Village", /*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/villageId, "CONTAINEDINPLACE", "Place", pageNo, size, Util.getHeader()).enqueue(new Callback<PlacesInVillageResponse>() {
            @Override
            public void onResponse(Call<PlacesInVillageResponse> call, Response<PlacesInVillageResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        placesData.setValue(response.body());
                        PlacesInVillageResponse allPlacesResponse = response.body();
                        List<PlacesInVillageResponse.Content> contentList = allPlacesResponse.getContent();

                        Log.d("Data", new Gson().toJson(response.body().getContent()));
                        for (PlacesInVillageResponse.Content content : contentList) {
                            Log.d(TAG, content.getTarget().getProperties().getName() + " attatat");
                        }
                    }
                } else {
                    placesData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PlacesInVillageResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                placesData.setValue(null);
            }
        });
        return placesData;
    }
}
