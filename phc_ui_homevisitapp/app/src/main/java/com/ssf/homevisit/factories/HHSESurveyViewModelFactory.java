package com.ssf.homevisit.factories;

import android.app.Application;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.viewmodel.HHSESurveyViewModel;

public class HHSESurveyViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String mVillageId;
    private String mHouseId;
    private VillageProperties mVillage;
    private String mSubCenterName;
    private String mPhcName;

    public HHSESurveyViewModelFactory(Application application, VillageProperties village, String subCenterName, String phcName) {
        mApplication = application;
        mVillage = village;
        mSubCenterName = subCenterName;
        mPhcName = phcName;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Log.d("mVillage", mVillage.getUuid());
        return (T) new HHSESurveyViewModel(mApplication, mVillage, mSubCenterName, mPhcName, mHouseId);
    }
}
