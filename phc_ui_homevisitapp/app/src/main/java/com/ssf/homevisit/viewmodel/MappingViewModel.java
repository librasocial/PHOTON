package com.ssf.homevisit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubCenterResponse;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.repository.MappingRepository;

public class MappingViewModel extends AndroidViewModel {

    private MappingRepository mappingRepository;
    private LiveData<PhcResponse> phcLiveData;
    private LiveData<AllPhcResponse> allPhcResponseLiveData;
    private LiveData<AllPhcResponse> userLiveData;
    private LiveData<SurveyFilterResponse> allSurveyFilterResponseLiveData;
    private LiveData<SubCenterResponse> subCenterResponseLiveData;
    private LiveData<SubCVillResponse> subCVillResponseLiveData;

    public String getSelectedVillageId() {
        return selectedVillageId;
    }

    public void setSelectedVillageId(String selectedVillageId) {
        this.selectedVillageId = selectedVillageId;
    }

    private String selectedVillageId;

    public MappingViewModel(@NonNull Application application) {
        super(application);
        mappingRepository = new MappingRepository();
//        allPhcResponseLiveData = mappingRepository.getAllPhcData("", 0,10);
        subCenterResponseLiveData = mappingRepository.getSubCenterData();
        subCVillResponseLiveData = mappingRepository.getSubCenterVillageData();
        allSurveyFilterResponseLiveData = mappingRepository.getSurveyData(1, 2);
    }

    public LiveData<AllPhcResponse> getAllPhcLiveData(String uuid) {
        allPhcResponseLiveData = new MutableLiveData<>();
        allPhcResponseLiveData = mappingRepository.getAllPhcData(uuid, 0,10);;
        return allPhcResponseLiveData;
    }

    public LiveData<SurveyFilterResponse> getAllSurveyFilterLiveData() {
        allSurveyFilterResponseLiveData = new MutableLiveData<>();
        allSurveyFilterResponseLiveData = MappingRepository.surveydata;
        return allSurveyFilterResponseLiveData;
    }

    public LiveData<SubCenterResponse> getSubCenterLiveData() {
        subCenterResponseLiveData = new MutableLiveData<>();
        subCenterResponseLiveData = MappingRepository.subCenterData;
        return subCenterResponseLiveData;
    }

    public LiveData<SubCVillResponse> getSubCenterVillLiveData() {
        subCVillResponseLiveData = new MutableLiveData<>();
        subCVillResponseLiveData = MappingRepository.subCenterVillage;
        return subCVillResponseLiveData;
    }
}
