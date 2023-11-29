package com.ssf.homevisit.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;
import com.ssf.homevisit.repository.MappingRepository;


public class CommonAlertViewModel extends AndroidViewModel {

    private final MappingRepository mappingRepository;
    private LiveData<PhcResponse> phcLiveData;
    private LiveData<AllPhcResponse> allPhc = new MutableLiveData<>();
    private LiveData<SubcentersFromPHCResponse> subcentersFromPHCResponseLiveData;
    private LiveData<SubCVillResponse> villagesFromSubcenterLiveData;
    public MutableLiveData str = new MutableLiveData("");

    public CommonAlertViewModel(@NonNull Application application) {
        super(application);
        mappingRepository = new MappingRepository();
    }

    public LiveData<PhcResponse> getPhcLiveData() {
        phcLiveData = new MutableLiveData<>();
        phcLiveData = mappingRepository.getPhc("0", "100");
        return phcLiveData;
    }

    public LiveData<AllPhcResponse> getAllPhcLiveData(String uuid) {
        allPhc = mappingRepository.getAllPhcData(uuid, 0, 10);
        ;
        return allPhc;
    }


    public LiveData<SubcentersFromPHCResponse> getSubcentersFromPHCResponseLiveData(String phcId) {
        subcentersFromPHCResponseLiveData = new MutableLiveData<>();
        subcentersFromPHCResponseLiveData = mappingRepository.getSubcentersFromPHC(phcId);
        return subcentersFromPHCResponseLiveData;
    }

    public LiveData<SubCVillResponse> getVillagesFromSubcentersLiveData(String subCenterId) {
        villagesFromSubcenterLiveData = new MutableLiveData<>();
        villagesFromSubcenterLiveData = mappingRepository.getVillagesFromSubcenters(subCenterId);
        return villagesFromSubcenterLiveData;
    }

}