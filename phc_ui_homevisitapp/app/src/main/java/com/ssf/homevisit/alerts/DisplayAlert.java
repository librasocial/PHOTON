package com.ssf.homevisit.alerts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.view.Window;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.AlertLayoutMap1Binding;
import com.ssf.homevisit.databinding.AlertLayoutMap2Binding;
import com.ssf.homevisit.databinding.AlertLayoutSomethingBinding;
import com.ssf.homevisit.interfaces.OnLocationFetched;
import com.ssf.homevisit.utils.Util;

import java.util.List;

public class DisplayAlert {

    private static Context context;
    private static DisplayAlert alert;
    private static final int REQUEST_CODE = 101;
    private Dialog currentDialog;

    /**
     * Get the instance of UIController
     *
     * @return
     */
    public static DisplayAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (DisplayAlert.class) {
                if (alert == null) {
                    alert = new DisplayAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(String lgdCode, OnLocationFetched onLocationFetched) {
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        AlertLayoutMap1Binding binding1 = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.alert_layout_map1, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding1.getRoot());
        currentDialog = dialog;
        dialog.show();
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AppController.getInstance().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppController.getInstance().getMainActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }
        dialog.dismiss();
        if (location != null) {
            final Dialog dialog2 = new Dialog(AppController.getInstance().getMainActivity());
            currentDialog = dialog2;
            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            AlertLayoutMap2Binding binding2 = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.alert_layout_map2, null, false);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setCancelable(true);
            dialog2.setContentView(binding2.getRoot());
            String mLatitude = Double.toString(location.getLatitude());
            String mLongitude = Double.toString(location.getLongitude());
            try {
//                binding2.mappingSuc.setText("Mapping Successful --- " + mLatitude.substring(0, 8) + "°N - " + mLongitude.substring(0, 8) + "°E");
                binding2.mappingSuc.setText("Mapping Successful --- " + Util.DDtoDMS(latitude, "latitude") + " - " + Util.DDtoDMS(longitude, "longitude"));
            } catch (StringIndexOutOfBoundsException e) {
                binding2.mappingSuc.setText("Mapping Successful --- " + Util.DDtoDMS(latitude, "latitude") + " - " + Util.DDtoDMS(longitude, "longitude"));
//                binding2.mappingSuc.setText("Mapping Successful --- " + mLatitude + "°N - " + mLongitude + "°E");
            }
            binding2.lgdCode00.setText("LGD-Code " + lgdCode);
            dialog2.show();

            binding2.getRoot().findViewById(R.id.alert_layout_map2).setOnClickListener(view1 -> {
                // todo get a lambda from creator and call that with captured lat, long
                onLocationFetched.onLocationFetched(latitude, longitude);
                dialog2.dismiss();
            });

        } else {
            // todo show error dialog, cant find location (for now only showing toast)
            UIController.getInstance().displayToastMessage(context, "Cant find location");
            dialog.dismiss();
            final Dialog dialog2 = new Dialog(AppController.getInstance().getMainActivity());
            AlertLayoutSomethingBinding binding2 = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.alert_layout_something, null, false);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setCancelable(true);
            dialog2.setContentView(binding2.getRoot());

        }
    }

    public static void dismiss() {
        if (alert != null)
            alert.currentDialog.dismiss();
    }
}

