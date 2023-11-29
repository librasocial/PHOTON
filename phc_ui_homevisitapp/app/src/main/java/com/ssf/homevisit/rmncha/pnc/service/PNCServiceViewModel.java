package com.ssf.homevisit.rmncha.pnc.service;

import android.app.Application;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAMembersInHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAMotherInfantDetailsRequest;
import com.ssf.homevisit.models.RMNCHAMotherInfantDetailsResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantServiceHistoryResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantServiceRequest;
import com.ssf.homevisit.models.RMNCHAPNCInfantServiceResponse;
import com.ssf.homevisit.models.RMNCHAPNCInfantVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAPNCInfantVisitLogResponse;
import com.ssf.homevisit.models.RMNCHAPNCMotherServiceHistoryResponse;
import com.ssf.homevisit.models.RMNCHAPNCMotherServiceRequest;
import com.ssf.homevisit.models.RMNCHAPNCMotherServiceResponse;
import com.ssf.homevisit.models.RMNCHAPNCMotherVisitLogRequest;
import com.ssf.homevisit.models.RMNCHAPNCMotherVisitLogResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PNCServiceViewModel extends RMNCHABaseViewModel {

    private final ApiInterface apiInterface;
    MutableLiveData<List<RMNCHAMembersInHouseHoldResponse.Content>> womenContentListLiveData = new MutableLiveData<>();
    private static final String TAG = PNCServiceViewModel.class.getSimpleName();

    public PNCServiceViewModel(@NonNull Application application) {
        super(application);
        apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
    }

    public MutableLiveData<RMNCHAPNCInfantResponse> getInfantDetailsData(String pncRegistrationId) {
        MutableLiveData<RMNCHAPNCInfantResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getPNCInfantRegistrationData(pncRegistrationId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCInfantResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCInfantResponse> call, Response<RMNCHAPNCInfantResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCInfantResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAMotherInfantDetailsResponse> getMotherInfantDetailsData(String motherUUID) {
        MutableLiveData<RMNCHAMotherInfantDetailsResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        apiInterface.getPNCMotherInfantDate(new RMNCHAMotherInfantDetailsRequest(motherUUID), Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAMotherInfantDetailsResponse>() {
            @Override
            public void onResponse(Call<RMNCHAMotherInfantDetailsResponse> call, Response<RMNCHAMotherInfantDetailsResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAMotherInfantDetailsResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }


    public MutableLiveData<RMNCHAPNCMotherServiceResponse> makePNCMotherServiceRequest(RMNCHAPNCMotherServiceRequest request) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCMotherServiceResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makePNCMotherServiceRequest(request, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCMotherServiceResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCMotherServiceResponse> call, Response<RMNCHAPNCMotherServiceResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCMotherServiceResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAPNCMotherVisitLogResponse> makePNCMotherVisitLogRequest(RMNCHAPNCMotherVisitLogRequest request, String pncServiceId) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCMotherVisitLogResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makePNCMotherVisitLogCall(request, pncServiceId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCMotherVisitLogResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCMotherVisitLogResponse> call, Response<RMNCHAPNCMotherVisitLogResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCMotherVisitLogResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<RMNCHAPNCMotherVisitLogResponse>> getPNCMotherServiceHistoryLiveData(String pncServiceId) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAPNCMotherVisitLogResponse>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAPNCMotherServiceHistory(pncServiceId, Util.getHeader()).enqueue(new Callback<RMNCHAPNCMotherServiceHistoryResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCMotherServiceHistoryResponse> call, Response<RMNCHAPNCMotherServiceHistoryResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHAPNCMotherServiceHistoryResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getData());
                        Log.d("Data", new Gson().toJson(responseData.getData()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCMotherServiceHistoryResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAPNCInfantServiceResponse> makePNCInfantServiceRequest(RMNCHAPNCInfantServiceRequest request, String pncServiceId) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCInfantServiceResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makePNCInfantServiceRequest(request, pncServiceId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCInfantServiceResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCInfantServiceResponse> call, Response<RMNCHAPNCInfantServiceResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCInfantServiceResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<RMNCHAPNCInfantVisitLogResponse> makePNCInfantVisitLogRequest(RMNCHAPNCInfantVisitLogRequest request, String pncServiceId) {
        clearErrorResponse();
        MutableLiveData<RMNCHAPNCInfantVisitLogResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makePNCInfantVisitLogCall(request, pncServiceId, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAPNCInfantVisitLogResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCInfantVisitLogResponse> call, Response<RMNCHAPNCInfantVisitLogResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                if (response.code() == 200) {
                    if (response.body() != null) {
                        data.setValue(response.body());
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCInfantVisitLogResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }

    public MutableLiveData<List<RMNCHAPNCInfantVisitLogResponse>> getPNCInfantServiceHistoryLiveData(String childID) {
        clearErrorResponse();
        MutableLiveData<List<RMNCHAPNCInfantVisitLogResponse>> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.getRMNCHAPNCInfantServiceHistory(childID, Util.getHeader()).enqueue(new Callback<RMNCHAPNCInfantServiceHistoryResponse>() {
            @Override
            public void onResponse(Call<RMNCHAPNCInfantServiceHistoryResponse> call, Response<RMNCHAPNCInfantServiceHistoryResponse> response) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                Log.d(TAG, "onResponse response:: " + response.code() + "   \nResponse body: " + response.body());
                if (response.code() == 200) {
                    RMNCHAPNCInfantServiceHistoryResponse responseData = response.body();
                    if (responseData != null) {
                        data.setValue(responseData.getData());
                        Log.d("Data", new Gson().toJson(responseData.getData()));
                        System.out.println("ASD" + new Gson().toJson(responseData));
                    } else {
                        setErrorResponse(response);
                        data.setValue(null);
                    }
                } else {
                    setErrorResponse(response);
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<RMNCHAPNCInfantServiceHistoryResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });
        return data;
    }


}
