package com.ssf.homevisit.factories;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.models.Source_Properties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.viewmodel.VillageLevelMappingViewModel;

public class VillageLevelMappingViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String mVillageId;
    private VillageProperties mVillage;
    private String mSubCenterName;
    private String mPhcName;

    public VillageLevelMappingViewModelFactory(Application application, VillageProperties village, String subCenterName, String phcName) {
        mApplication = application;
        mVillage = village;
        mSubCenterName = subCenterName;
        mPhcName = phcName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new VillageLevelMappingViewModel(mApplication, mVillage, mSubCenterName, mPhcName);
    }
}
