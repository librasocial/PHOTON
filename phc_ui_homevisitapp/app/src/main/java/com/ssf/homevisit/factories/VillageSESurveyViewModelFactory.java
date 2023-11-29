package com.ssf.homevisit.factories;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.viewmodel.HouseholdAlertViewModel;
import com.ssf.homevisit.viewmodel.VillageSESurveyViewModel;

public class VillageSESurveyViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplication;
    private VillageProperties mVillageProperties;

    public VillageSESurveyViewModelFactory(Application application, VillageProperties vil) {
        mApplication = application;
        mVillageProperties = vil;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VillageSESurveyViewModel(mApplication, mVillageProperties);
    }
}
