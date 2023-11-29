package com.ssf.homevisit.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.ssf.homevisit.models.CreatePlaceResponse;
import com.ssf.homevisit.models.CreateVillageAssetBody;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VillageLevelMappingDialogViewModel extends AndroidViewModel {

    private ApiInterface apiInterface;

    public VillageLevelMappingDialogViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

}
