package com.ssf.homevisit.factories;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.viewmodel.HHSESurveyAlertViewModel;
import com.ssf.homevisit.viewmodel.HHSESurveyViewModel;

public class HHSESurveyAlertViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private HouseHoldProperties houseHoldProperties;

    public HHSESurveyAlertViewModelFactory(Application application, HouseHoldProperties houseHoldProperties) {
        mApplication = application;
        this.houseHoldProperties = houseHoldProperties;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HHSESurveyAlertViewModel(mApplication, houseHoldProperties);
    }
}
