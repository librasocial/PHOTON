package com.ssf.homevisit.viewmodel;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.models.AuthenticationResult;
import com.ssf.homevisit.repository.LoginRepository;
import com.ssf.homevisit.requestmanager.AppDefines;

import org.json.JSONObject;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends ViewModel {
    public final MutableLiveData<String> userName = new MutableLiveData<>();
    public final MutableLiveData<String> password = new MutableLiveData<>();
    private final LoginRepository loginRepository;


    private final MutableLiveData<AuthenticationResult> mCognitoResponseLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> mErrorLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> progress = new MutableLiveData<>();
    public LiveData<AuthenticationResult> cognitoResponseLiveData = mCognitoResponseLiveData;
    public LiveData<String> errorLiveData = mErrorLiveData;

    @Inject
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    public void init() {
        progress.setValue(View.GONE);
    }

    public void getLoginAuth() {
        loginRepository.getSavedLogin(mCognitoResponseLiveData, mErrorLiveData);
    }

    public void callLoginApi() {

        if (isValidData()) {
            progress.setValue(View.VISIBLE);
            try {
                JSONObject mainObject = new JSONObject();
                JSONObject param = new JSONObject();
                String _username = userName.getValue() == null ? "" : userName.getValue();
                String _password = password.getValue() == null ? "" : password.getValue();
                _username = _username.trim();
                _password = _password.trim();
                param.put("USERNAME", _username);
                param.put("PASSWORD", _password);
                mainObject.put("AuthParameters", param);
                mainObject.put("AuthFlow", "USER_PASSWORD_AUTH");
                mainObject.put("ClientId", AppDefines.COGNITO_CLIENT_ID);
                JsonObject jsonObject = (JsonObject) JsonParser.parseString(mainObject.toString());

                loginRepository.getLoginTokens(jsonObject, mErrorLiveData, mCognitoResponseLiveData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isValidData() {
        if (userName.getValue() == null || userName.getValue().equals("")) {
            UIController.getInstance().displayToastMessage(AppController.getInstance().getApplicationContext(), "Enter User Name");
            return false;
        }
        if (password.getValue() == null || password.getValue().equals("")) {
            UIController.getInstance().displayToastMessage(AppController.getInstance().getApplicationContext(), "Enter Password");
            return false;
        }
        return true;
    }
}
