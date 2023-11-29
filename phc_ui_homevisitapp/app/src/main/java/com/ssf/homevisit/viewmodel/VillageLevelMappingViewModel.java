package com.ssf.homevisit.viewmodel;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.ssf.homevisit.models.GetPlaceCountInVillageBody;
import com.ssf.homevisit.models.PlacesCountResponse;
import com.ssf.homevisit.models.PlacesInVillageResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.repository.VillageLevelMappingRepository;

public class VillageLevelMappingViewModel extends AndroidViewModel {

    public final MutableLiveData<String> phcName = new MutableLiveData<>();
    public final MutableLiveData<String> subCenterName = new MutableLiveData<>();
    public final MutableLiveData<String> villageName = new MutableLiveData<>();
    private VillageLevelMappingRepository villageLevelMappingRepository;
    private LiveData<PlacesInVillageResponse> placesResponseLiveData;
    private LiveData<PlacesCountResponse> placesCountLiveData;
    private String villageId;

    // todo make liveData for latitude and longitude
    public VillageLevelMappingViewModel(@NonNull Application application, VillageProperties village, String _SubCenterName, String _PhcName) {
        super(application);
        villageName.setValue(village.getName());
        phcName.setValue(_PhcName);
        subCenterName.setValue(_SubCenterName);
        villageLevelMappingRepository = new VillageLevelMappingRepository();
        this.villageId=village.getUuid();
        placesResponseLiveData = villageLevelMappingRepository.getPlacesInVillageData(/*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/villageId,0,1000);
    }

    public LiveData<PlacesInVillageResponse> getPlacesInVillageData() {
        placesResponseLiveData = new MutableLiveData<>();
        placesResponseLiveData = villageLevelMappingRepository.getPlacesInVillageData(/*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/villageId,0,1000);
        placesResponseLiveData = VillageLevelMappingRepository.placesData;
        return placesResponseLiveData;
    }

    public LiveData<PlacesCountResponse> getPlacesInCount(){
        placesCountLiveData = new MutableLiveData<>();
        GetPlaceCountInVillageBody reqBody = new GetPlaceCountInVillageBody();
        reqBody.setType("Place");
        reqBody.setPage(0);
        reqBody.setSize(1000);
        GetPlaceCountInVillageBody.Properties properties = new GetPlaceCountInVillageBody.Properties();
        Log.d("tag", villageId);
        properties.setVillageId(this.villageId);
        reqBody.setProperties(properties);

        placesCountLiveData = villageLevelMappingRepository.getPlacesCountData(reqBody);
        placesCountLiveData = VillageLevelMappingRepository.placesCountData;
        return placesCountLiveData;

    }

}
