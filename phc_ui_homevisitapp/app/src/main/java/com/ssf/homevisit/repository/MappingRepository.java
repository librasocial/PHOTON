package com.ssf.homevisit.repository;

import android.annotation.SuppressLint;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.AllPhcResponse;
import com.ssf.homevisit.models.GPMemberResponse;
import com.ssf.homevisit.models.GramPanchayatForVillage;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.PhcStaffSubCenter;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubCenterResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;
import com.ssf.homevisit.models.SurveyFilterResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MappingRepository {
    private static final String TAG = MappingRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    public static MutableLiveData<AllPhcResponse> data = new MutableLiveData<>();
    public static MutableLiveData<PhcResponse> phcData = new MutableLiveData<>();
    public static MutableLiveData<SurveyFilterResponse> surveydata = new MutableLiveData<>();
    public static MutableLiveData<SubCenterResponse> subCenterData = new MutableLiveData<>();
    public static MutableLiveData<SubCVillResponse> subCenterVillage = new MutableLiveData<>();
    public static MutableLiveData<SubcentersFromPHCResponse> subCentersOfPhc = new MutableLiveData<>();
    public static MutableLiveData<SubCVillResponse> villageOfSubCenter = new MutableLiveData<>();
    public static MutableLiveData<PhcStaffSubCenter> phcStaffOfSubCenter = new MutableLiveData<>();
    public static MutableLiveData<GPMemberResponse> gpMembersList = new MutableLiveData<>();
    public static MutableLiveData<GramPanchayatForVillage> gramPanchayat = new MutableLiveData<>();

    public MappingRepository() {
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public LiveData<AllPhcResponse> getAllPhcData(String uuid, int pageNo, int size) {
        apiInterface.getAllPhcResponse(uuid, Util.getHeader()).enqueue(new Callback<AllPhcResponse>() {
            @Override
            public void onResponse(Call<AllPhcResponse> call, Response<AllPhcResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                        AllPhcResponse allPhcResponse = response.body();
                        List<AllPhcResponse.Content> contentList = allPhcResponse.getContent();
                        Log.d("Data", new Gson().toJson(response.body().getContent()));

                        System.out.println("ASD" + new Gson().toJson(contentList));
                    }
                } else {
                        data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<AllPhcResponse> call, Throwable t) {
                data.setValue(null);
            }
        });
        return data;
    }


    public LiveData<PhcResponse> getPhc(String pageNo, String size) {
        apiInterface.getPhc(pageNo, size, Util.getHeader()).enqueue(new Callback<PhcResponse>() {
            @Override
            public void onResponse(Call<PhcResponse> call, Response<PhcResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        phcData.setValue(response.body());
                    }
                } else {
                    phcData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<PhcResponse> call, Throwable t) {
                phcData.setValue(null);
            }
        });
        return phcData;
    }



    public LiveData<SurveyFilterResponse> getSurveyData(int limit, int page) {
        apiInterface.getSurveyFilterResponse("Village", limit, page, "Bearer " + Util.getHeader()).enqueue(new Callback<SurveyFilterResponse>() {
            @Override
            public void onResponse(Call<SurveyFilterResponse> call, Response<SurveyFilterResponse> response) {
                if (response.code() == 200) {

                    if (response.body() != null) {
                        surveydata.setValue(response.body());
                        SurveyFilterResponse allPhcResponse = response.body();
                        List<SurveyFilterResponse.Datum> contentList = allPhcResponse.getData();
                        Log.d("Data", new Gson().toJson(response.body().getData()));

                        System.out.println("ASD" + new Gson().toJson(contentList));
                    }
                }
                else {
                    surveydata.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SurveyFilterResponse> call, Throwable t) {
                surveydata.setValue(null);
            }
        });
        return surveydata;
    }
// phc
    public LiveData<SubCenterResponse> getSubCenterData() {
        apiInterface.getAllSubcenterResponse("SubCenter", "SUBORGOF", "Phc", Util.getHeader()).enqueue(new Callback<SubCenterResponse>() {
            @Override
            public void onResponse(Call<SubCenterResponse> call, Response<SubCenterResponse> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        subCenterData.setValue(response.body());
                    }
                } else {
                    subCenterData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SubCenterResponse> call, Throwable t) {
                subCenterData.setValue(null);
            }
        });
        return subCenterData;
    }

    public LiveData<SubCVillResponse> getSubCenterVillageData() {
        apiInterface.getAllSubcenterVillageResponse("SubCenter", "SERVICEDAREA", "Village", Util.getHeader()).enqueue(new Callback<SubCVillResponse>() {
            @Override
            public void onResponse(Call<SubCVillResponse> call, Response<SubCVillResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        subCenterVillage.setValue(response.body());
                    }
                }
                else {
                    subCenterVillage.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<SubCVillResponse> call, Throwable t) {
                subCenterVillage.setValue(null);
            }
        });
        return subCenterVillage;
    }

    public LiveData<SubcentersFromPHCResponse> getSubcentersFromPHC(String phcId) {
        apiInterface.getSubcentersFromPhc("Phc", "SUBORGOF", phcId, "SubCenter", Util.getHeader()).enqueue(
                new Callback<SubcentersFromPHCResponse>() {
                    @Override
                    public void onResponse(Call<SubcentersFromPHCResponse> call, Response<SubcentersFromPHCResponse> response) {
                        AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        Log.d(TAG, "onResponse response:: " + response);
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                subCentersOfPhc.setValue(response.body());
                            }
                        } else {
                            subCentersOfPhc.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubcentersFromPHCResponse> call, Throwable t) {
                        subCentersOfPhc.setValue(null);
                    }
                });
        return subCentersOfPhc;
    }
//village
    public LiveData<SubCVillResponse> getVillagesFromSubcenters(String subCenterId) {
        apiInterface.getVillagesFromSubCenter("SubCenter", "SERVICEDAREA", subCenterId, "Village", Util.getHeader()).enqueue(
                new Callback<SubCVillResponse>() {
                    @Override
                    public void onResponse(Call<SubCVillResponse> call, Response<SubCVillResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                villageOfSubCenter.setValue(response.body());
                            }
                        } else {
                            villageOfSubCenter.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<SubCVillResponse> call, Throwable t) {
                        villageOfSubCenter.setValue(null);
                    }
                });
        return villageOfSubCenter;
    }

    public LiveData<PhcStaffSubCenter> getPHCStaffList(String subCenterId) {
        apiInterface.getPHCStaffList("SubCenter", "MEMBEROF", subCenterId, "PrimaryHealthCareOfficer", Util.getHeader()).enqueue(
                new Callback<PhcStaffSubCenter>() {
                    @Override
                    public void onResponse(@NonNull Call<PhcStaffSubCenter> call, @NonNull Response<PhcStaffSubCenter> response) {
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                phcStaffOfSubCenter.setValue(response.body());
                            }
                        } else {
                            phcStaffOfSubCenter.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<PhcStaffSubCenter> call, Throwable t) {
                        phcStaffOfSubCenter.setValue(null);
                    }
                });
        return phcStaffOfSubCenter;
    }

    public LiveData<GramPanchayatForVillage> getGramPanchayatOfVillage(String nodeId) {
        apiInterface.getGramPanchayatOfVillage(nodeId, Util.getHeader()).enqueue(
                new Callback<GramPanchayatForVillage>() {
                    @Override
                    public void onResponse(@NonNull Call<GramPanchayatForVillage> call, @NonNull Response<GramPanchayatForVillage> response) {
                        AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                gramPanchayat.setValue(response.body());
                            }
                        } else {
                            gramPanchayat.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<GramPanchayatForVillage> call, Throwable t) {
                        gramPanchayat.setValue(null);
                    }
                });
        return gramPanchayat;
    }

    public LiveData<GPMemberResponse> getGPMemberList(String nodeId) {
        apiInterface.getGPMemberList(nodeId, Util.getHeader()).enqueue(
                new Callback<GPMemberResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<GPMemberResponse> call, @NonNull Response<GPMemberResponse> response) {
                        AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        if (response.code() == 200) {
                            if (response.body() != null) {
                                gpMembersList.setValue(response.body());
                            }
                        } else {
                            gpMembersList.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<GPMemberResponse> call, Throwable t) {
                        AppController.getInstance().getCurrentActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        gpMembersList.setValue(null);
                    }
                });
        return gpMembersList;
    }
}
