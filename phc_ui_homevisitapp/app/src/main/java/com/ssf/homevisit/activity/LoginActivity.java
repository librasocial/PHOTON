package com.ssf.homevisit.activity;

import static com.ssf.homevisit.requestmanager.AppDefines.IS_LOGIN;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.microsoft.appcenter.AppCenter;
import com.microsoft.appcenter.analytics.Analytics;
import com.microsoft.appcenter.crashes.Crashes;
import com.ssf.homevisit.R;
import com.ssf.homevisit.app.AndroidApplication;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.ActivityLoginBinding;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.CheckNetwork;
import com.ssf.homevisit.viewmodel.CommonAlertViewModel;
import com.ssf.homevisit.viewmodel.LoginViewModel;

import java.util.HashMap;
import java.util.Map;

import dagger.hilt.android.AndroidEntryPoint;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    ActivityLoginBinding binding;
    private boolean showingPassword;
    private final SharedPreferences.Editor editor = AndroidApplication.prefs.edit();
    CommonAlertViewModel commonAlertViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppCenter.start(getApplication(), AppDefines.CRASH_ANALYTICS_APP_KEY,
                Analytics.class, Crashes.class);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // View Model
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        loginViewModel.init();
//        loginViewModel.getLoginAuth();
        //set view model
        binding.setViewModel(loginViewModel);
        binding.setLifecycleOwner(this);
        commonAlertViewModel = ViewModelProviders.of(this).get(CommonAlertViewModel.class);

//        commonAlertViewModel = new CommonAlertViewModel(((Activity) this).getApplication());
////        commonAlertViewModel = new ViewModelProvider(this).get(CommonAlertViewModel.class);

        if (AndroidApplication.prefs.getBoolean(IS_LOGIN, false)) {

            Map<String, String> properties = new HashMap<>();
            if (!Analytics.isEnabled().get()) {
                Analytics.setEnabled(true);
            }
            properties.put("username", loginViewModel.userName.getValue());

            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
        } else {
            binding.logoText.setText(Html.fromHtml(getResources().getString(R.string.powered_by_)));

            binding.lllogin.setOnClickListener(view -> callApiForLogin());

            loginViewModel.cognitoResponseLiveData.observe(this, authenticationResult -> {
                clearWindowFlag();
                if (authenticationResult != null) {
                    Map<String, String> properties = new HashMap<>();
                    if (!Analytics.isEnabled().get()) {
                        Analytics.setEnabled(true);
                    }
                    properties.put("username", loginViewModel.userName.toString());

                    Analytics.trackEvent("User Logged in", properties);
                    editor.putString(AppDefines.ACCESS_TOKEN, authenticationResult.getAccessToken());
                    editor.putString(AppDefines.ID_TOKEN, authenticationResult.getIdToken());
                    editor.putString(AppDefines.REFRESH_TOKEN, authenticationResult.getRefreshToken());
                    editor.putInt(AppDefines.TOKEN_EXPIRE, authenticationResult.getExpiresIn());
                    editor.putString(AppDefines.TOKEN_TYPE, authenticationResult.getTokenType());
                    editor.putString(AppDefines.USER_NAME, loginViewModel.userName.getValue());
                    editor.putBoolean(IS_LOGIN, true);
                    editor.apply();

                    String membershipId = convertTokenGetId();

                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                    LoginActivity.this.finish();

                }
            });
            binding.showPassword.setOnClickListener(view -> {
                if (showingPassword) {
                    view.setBackground(ContextCompat.getDrawable(this, R.drawable.hide_password_icon));
                    binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
                    showingPassword = false;
                } else {
                    view.setBackground(ContextCompat.getDrawable(this, R.drawable.show_password_icon));
                    binding.etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    showingPassword = true;
                }
            });

            loginViewModel.errorLiveData.observe(this, new Observer<String>() {
                @Override
                public void onChanged(String s) {
                    String msg = !s.isEmpty() ? s : " Something went wrong";
                    UIController.getInstance().displayToastMessage(LoginActivity.this, msg);
                    Map<String, String> properties = new HashMap<>();
                    if (!Analytics.isEnabled().get()) {
                        Analytics.setEnabled(true);
                    }
                    properties.put("username", loginViewModel.userName.toString());
                    Analytics.trackEvent("User Login failed", properties);
                    binding.progressBar.setVisibility(View.GONE);
                    clearWindowFlag();
                }
            });
        }
    }

    private void clearWindowFlag() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private String convertTokenGetId() {
        String id = "";
        String idToken = AndroidApplication.prefs.getString(AppDefines.ID_TOKEN, "");
        int i = idToken.lastIndexOf('.');
        String withoutSignature = idToken.substring(0, i + 1);
        Jwt<Header, Claims> untrusted = Jwts.parser().parseClaimsJwt(withoutSignature);

        return id;
    }


    private void callApiForLogin() {
        if (CheckNetwork.isNetworkAvailable(this)) {
            loginViewModel.callLoginApi();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else
            UIController.getInstance().displayToastMessage(this, getResources().getString(R.string.check_internet_connection));
    }
}