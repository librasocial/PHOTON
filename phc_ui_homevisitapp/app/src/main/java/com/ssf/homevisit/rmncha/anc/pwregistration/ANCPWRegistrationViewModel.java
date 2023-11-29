package com.ssf.homevisit.rmncha.anc.pwregistration;

import android.app.Application;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.models.RMNCHAANCRegistrationRequest;
import com.ssf.homevisit.models.RMNCHAANCRegistrationResponse;
import com.ssf.homevisit.rmncha.base.RMNCHABaseViewModel;
import com.ssf.homevisit.utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ANCPWRegistrationViewModel extends RMNCHABaseViewModel {

    public ANCPWRegistrationViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RMNCHAANCRegistrationResponse> makeANCRegistrationRequest(RMNCHAANCRegistrationRequest rmnchaancRegistrationRequest) {
        clearErrorResponse();
        MutableLiveData<RMNCHAANCRegistrationResponse> data = new MutableLiveData<>();
        AppController.getInstance().getMainActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        apiInterface.makeANCRegistration(rmnchaancRegistrationRequest, Util.getHeader(), Util.getIdToken()).enqueue(new Callback<RMNCHAANCRegistrationResponse>() {
            @Override
            public void onResponse(Call<RMNCHAANCRegistrationResponse> call, Response<RMNCHAANCRegistrationResponse> response) {
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
            public void onFailure(Call<RMNCHAANCRegistrationResponse> call, Throwable t) {
                AppController.getInstance().getMainActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                data.setValue(null);
            }
        });

        return data;
    }

}
