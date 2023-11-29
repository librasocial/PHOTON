package com.ssf.homevisit.factories;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.viewmodel.HouseholdAlertViewModel;

public class HouseholdAlertViewModelFactory implements ViewModelProvider.Factory{

    private Application mApplication;
    private HouseHoldProperties mHouseholdProperties;

    public HouseholdAlertViewModelFactory(Application application, HouseHoldProperties houseHoldProperties) {
        mApplication = application;
        mHouseholdProperties = houseHoldProperties;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HouseholdAlertViewModel(mApplication, mHouseholdProperties);
    }
}
