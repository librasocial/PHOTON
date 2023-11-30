package com.ssf.homevisit.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;

import com.ssf.homevisit.activity.LoginActivity;
import com.ssf.homevisit.activity.MainActivity;
import com.ssf.homevisit.app.AndroidApplication;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.rmncha.ec.ecservice.ECServiceActivity;
import com.ssf.homevisit.rmncha.pnc.details.PNCDetailsActivity;
import com.ssf.homevisit.rmncha.pnc.service.PNCServiceActivity;


public class AppController implements AppDefines {

    //singleton instance
    private static AppController instance;

    //Reference to android Application class object
    private AndroidApplication application;
    //Handler object reference
    private Handler handler;

    /**
     * In our app we will have only one main activity and we will use the fragments only.
     */
    MainActivity mainActivity;

    LoginActivity loginActivity;

    ECServiceActivity ecServiceActivity;

    PNCDetailsActivity pncDetailsActivity;

    PNCServiceActivity pncServiceActivity;

    Activity currentActivity;

    /**
     * To get singleton reference of AppController class
     *
     * @return
     */
    public static AppController getInstance() {
        if (instance == null) {
            synchronized (AppController.class) {
                if (instance == null) {
                    instance = new AppController();
                }
            }
        }
        return instance;
    }

    /**
     * Set Android application reference
     *
     * @param application
     */
    public void setApplication(AndroidApplication application) {
        this.application = application;
    }

    /**
     * Get the Android application reference
     *
     * @return
     */
    public AndroidApplication getApplication() {
        return application;
    }

    /**
     * To get the application context refernce to be used in different posstion
     *
     * @return
     */
    public Context getApplicationContext() {

        return application.getApplicationContext();
    }

    /**
     * Function to set the Handler reference
     *
     * @param handler
     */
    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    /**
     * Function to get the Handler Reference
     *
     * @return
     */
    public Handler getHandler() {
        return handler;
    }


    /**
     * HandleEvent function to manage events inside the appliction.
     * this functions should be used for various background events and activity launch activity
     *
     * @param eventID
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleEvent(String eventID) {
        handleEvent(eventID, null);
    }

    /**
     * HandleEvent function to manage events inside the appliction.
     * this functions should be used for various background events and activity launch activity
     *
     * @param eventName      eventName to process the particular events
     * @param bundle data to be passed to next activity
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void handleEvent(String eventName, Bundle bundle) {

    }

    public void setMainActivity(MainActivity mainActivity) {
        this.currentActivity = mainActivity;
        this.mainActivity = mainActivity;
    }

    public MainActivity getMainActivity() {
        return mainActivity;
    }

    public void setECServiceActivity(ECServiceActivity ecServiceActivity) {
        this.ecServiceActivity = ecServiceActivity;
    }

    public ECServiceActivity getECServiceActivity() {
        return ecServiceActivity;
    }

    public PNCDetailsActivity getPncLandingActivity() {
        return pncDetailsActivity;
    }

    public void setPncLandingActivity(PNCDetailsActivity pncDetailsActivity) {
        this.pncDetailsActivity = pncDetailsActivity;
    }

    public PNCServiceActivity getPncServiceActivity() {
        return pncServiceActivity;
    }

    public void setPncServiceActivity(PNCServiceActivity pncServiceActivity) {
        this.pncServiceActivity = pncServiceActivity;
    }

    public LoginActivity getLoginActivity() {
        return loginActivity;
    }

    public void setLoginActivity(LoginActivity loginActivity) {
        this.currentActivity = mainActivity;
        this.loginActivity = loginActivity;
    }

    public Activity getCurrentActivity() {
        return this.currentActivity;
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }
}
