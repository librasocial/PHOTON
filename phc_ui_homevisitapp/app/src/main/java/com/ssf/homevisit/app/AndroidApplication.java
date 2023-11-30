package com.ssf.homevisit.app;

import static com.ssf.homevisit.requestmanager.AppDefines.PREFERENCES;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.raj.jadon.filepicker.mannualDi.ImageAndFilePickerInjector;
import com.ssf.homevisit.controller.AppController;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class AndroidApplication extends Application implements LifecycleObserver {

    //handler will be used as global variable in the application. wherever
    //it is required
    private final Handler handler = new Handler();

    private static final String TAG = "Application";
    public static SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();
        AppController.getInstance().setApplication(this);
        AppController.getInstance().setHandler(handler);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        ImageAndFilePickerInjector.Companion.initInjectorInstance(this);
    }

    @Override
    public void onLowMemory() {

        super.onLowMemory();
    }

    @Override
    public void onTerminate() {

        super.onTerminate();
    }

    @Override
    public void onTrimMemory(int level) {

        super.onTrimMemory(level);
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        Log.d(TAG,"Start");
    }


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onMoveToResume() {
        Log.d(TAG,"Resume");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onMoveToPasuse() {
        Log.d(TAG,"Pause");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        //sendBroadcast(new Intent(this, AppterminateReceiver.class).putExtra("IsDataDeleted",AppController.getInstance().getMainActivity().isClearData));
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onMoveToDestroy() {
        Log.d(TAG,"Destroy");
    }

}
