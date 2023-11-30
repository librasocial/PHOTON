package com.ssf.homevisit.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ssf.homevisit.datasource.LocalDataSourceImpl;
import com.ssf.homevisit.models.AuthenticationResult;
import com.ssf.homevisit.models.CognitoResponse;
import com.ssf.homevisit.models.ErrorResponse;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;

import java.lang.reflect.Type;

import javax.inject.Inject;

import dagger.hilt.android.scopes.ActivityRetainedScoped;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@ActivityRetainedScoped
public class LoginRepository {
    private static final String TAG = LoginRepository.class.getSimpleName();
    private ApiInterface apiInterface;
    private LocalDataSourceImpl localDataSource;

    @Inject
    public LoginRepository(LocalDataSourceImpl localDataSource) {
        apiInterface = ApiClient.getClient(AppDefines.FROM_LOGIN).create(ApiInterface.class);
        this.localDataSource = localDataSource;
    }

    public void getSavedLogin(MutableLiveData<AuthenticationResult> mLoginLiveData, MutableLiveData<String> mErrorLiveData) {
        if (localDataSource.getAuthToken() != null) {
            mLoginLiveData.setValue(localDataSource.getAuthToken());
        } else {
            mErrorLiveData.setValue("Please Login with user credential");
        }

    }

    public void getLoginTokens(JsonObject jsonObject, MutableLiveData<String> mErrorLiveData, MutableLiveData<AuthenticationResult> mLoginLiveData) {

        apiInterface.getLoginTokenResponse(jsonObject, AppDefines.APICONTENTTYPEVALUECOGNITO, AppDefines.X_AMZ_TARGET_VALUE, AppDefines.COGNITO_URL).enqueue(new Callback<CognitoResponse>() {
            @Override
            public void onResponse(Call<CognitoResponse> call, Response<CognitoResponse> response) {
//                AppController.getInstance().getLoginActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//                Log.d(TAG, "onResponse response:: " + response);
                if (response.code() == 200 && response.body() != null && response.body().getAuthenticationResult() != null) {
                    AuthenticationResult authenticationResult = response.body().getAuthenticationResult();
                    mLoginLiveData.setValue(authenticationResult);
                    localDataSource.saveAuthToken(authenticationResult);

                } else {
                    Gson gson = new Gson();
                    Type type = new TypeToken<ErrorResponse>() {
                    }.getType();
                    ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(), type);
                    mErrorLiveData.setValue(errorResponse.message);
                }
            }

            @Override
            public void onFailure(Call<CognitoResponse> call, Throwable t) {
                mErrorLiveData.setValue("Something went wrong");
            }
        });
    }
}
