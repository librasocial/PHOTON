package com.ssf.homevisit.alerts;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import com.ssf.homevisit.R;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.controller.UIController;
import com.ssf.homevisit.databinding.AlertLayoutMap1Binding;
import com.ssf.homevisit.databinding.AlertLayoutMap2Binding;
import com.ssf.homevisit.databinding.VillagelevelmappingBinding;
import com.ssf.homevisit.fragment.SociaEcoSurveyFragment;
import com.ssf.homevisit.models.CreatePlaceResponse;
import com.ssf.homevisit.models.CreateVillageAssetBody;
import com.ssf.homevisit.models.PlaceProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.requestmanager.ApiClient;
import com.ssf.homevisit.requestmanager.ApiInterface;
import com.ssf.homevisit.requestmanager.AppDefines;
import com.ssf.homevisit.utils.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class HouseMapAlert {
    private static Context context;
    private static HouseMapAlert alert;
    private static final int REQUEST_CODE = 101;

    /**
     * Get the instance of UIController
     *
     * @return
     */
    public static HouseMapAlert getInstance(Context mcontext) {
        if (alert == null) {
            synchronized (HouseMapAlert.class) {
                if (alert == null) {
                    alert = new HouseMapAlert();
                    context = mcontext;

                }
            }
        }
        return alert;
    }


    @SuppressLint("SetTextI18n")
    public void displayMappingAlert(VillageProperties village, SociaEcoSurveyFragment.OnVillageAssetAdd onVillageAssetAdd) {
        final Dialog dialog = new Dialog(AppController.getInstance().getMainActivity());
        AlertLayoutMap1Binding binding1 = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.alert_layout_map1, null, false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(binding1.getRoot());

        dialog.show();
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AppController.getInstance().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppController.getInstance().getMainActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        LocationManager locationManager = (LocationManager) context.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
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

            final double latitude = location.getLatitude();
            final double longitude = location.getLongitude();

            AlertLayoutMap2Binding binding2 = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.alert_layout_map2, null, false);
            dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog2.setCancelable(false);
            dialog2.setContentView(binding2.getRoot());
            String mLatitude = Double.toString(location.getLatitude());
            String mLongitude = Double.toString(location.getLongitude());
            try {
                binding2.mappingSuc.setText("Mapping Successful --- " + mLatitude.substring(0, 8) + "°N - " + mLongitude.substring(0, 8) + "°E");
            } catch (StringIndexOutOfBoundsException e) {
                binding2.mappingSuc.setText("Mapping Successful --- " + mLatitude + "°N - " + mLongitude + "°E");
            }
            binding2.lgdCode00.setText("LGD-Code " + village.getLgdCode());
            dialog2.show();

            binding2.getRoot().findViewById(R.id.alert_layout_map2).setOnClickListener(view1 -> {
                final Dialog dialog3 = new Dialog(AppController.getInstance().getMainActivity());
                dialog2.dismiss();
                VillagelevelmappingBinding binding3 = DataBindingUtil.inflate(dialog.getLayoutInflater(), R.layout.villagelevelmapping, null, false);
                binding3.textLocation.setText(latitude + "°N - " + longitude + "°E");
                binding3.ivClose.setOnClickListener(view -> dialog3.dismiss());
                binding3.btnSave.setOnClickListener(v -> {
                    ApiInterface apiInterface = ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface.class);
                    CreateVillageAssetBody createVillageAssetBody = new CreateVillageAssetBody();
                    createVillageAssetBody.setType("Place");

                    String assetType = getSelectedAssetType(binding3);
                    if (assetType.equals("not available")) {
                        UIController.getInstance().displayToastMessage(context, "Cant Save, Try with other asset type");
                        return;
                    }
                    PlaceProperties placeProperties = new PlaceProperties();
                    placeProperties.setVillageId(/*"2a56cf43-4f23-40a1-9599-3d2c2a57f7b3"*/village.getUuid());
                    placeProperties.setName(binding3.editTextName.getText().toString());
                    placeProperties.setLongitude(longitude);
                    placeProperties.setLatitude(latitude);
                    placeProperties.setAssetType(assetType);
                    placeProperties.setRequiredSurveys(getSelectedSurveys(binding3));
//                    todo uncomment this
                    placeProperties.setAssetSubType(binding3.spinnerTypeOfAsset.getSelectedItem().toString());

                    createVillageAssetBody.setPlaceProperties(placeProperties);
                    apiInterface.createNewVillageAsset(createVillageAssetBody, "application/json", Util.getIdToken(), Util.getHeader()).enqueue(new Callback<CreatePlaceResponse>() {
                        @Override
                        public void onResponse(Call<CreatePlaceResponse> call, Response<CreatePlaceResponse> response) {
                            dialog3.dismiss();
                            UIController.getInstance().displayToastMessage(context, "Village Asset Saved");
                            onVillageAssetAdd.onVillageAssetAdd();
                        }

                        @Override
                        public void onFailure(Call<CreatePlaceResponse> call, Throwable t) {
                            UIController.getInstance().displayToastMessage(context, "Could Not Save Village Asset");
                        }
                    });
                });
                binding3.btnCancel.setOnClickListener(v -> dialog3.dismiss());
                setOnClick(binding3);
                dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog3.setContentView(binding3.getRoot());
                dialog3.show();
            });

        } else {
            // todo show error dialog
        }


    }

    private String getSelectedSurveys(VillagelevelmappingBinding binding3) {
        StringBuilder requiredSurveys = new StringBuilder();
        if (binding3.larvaSurvey.isChecked()) requiredSurveys.append("Larva ");
        if (binding3.waterSampleCollection.isChecked()) requiredSurveys.append("Water ");
        if (binding3.iodineTest.isChecked()) requiredSurveys.append("Iodine ");
        if (binding3.iecProgramme.isChecked()) requiredSurveys.append("IEC ");
        return requiredSurveys.toString();

    }

    private String getSelectedAssetType(VillagelevelmappingBinding binding3) {
        if (binding3.radioTemple.isChecked()) return "Temple";
        if (binding3.radioMosque.isChecked()) return "Mosque";
        if (binding3.radioChurch.isChecked()) return "Church";
        if (binding3.radioHotel.isChecked()) return "Hotel";
        if (binding3.radioOffice.isChecked()) return "Office";
        if (binding3.radioHospital.isChecked()) return "Hospital";
        if (binding3.radioConstruction.isChecked()) return "Construction";
        if (binding3.radioEducation.isChecked()) return "SchoolCollage";
        if (binding3.radioBusStop.isChecked()) return "BusStop";
        if (binding3.radioWaterBody.isChecked()) return "WaterBody";
        if (binding3.radioShop.isChecked()) return "Shop";
        if (binding3.radioToilet.isChecked()) return "Toilet";
        if (binding3.radioPark.isChecked()) return "Park";
        if (binding3.radioOther.isChecked()) return "OtherPlaces";

        return "not available";

    }

    private void populateSpinner(String assetType, VillagelevelmappingBinding binding) {
        String[] arraySpinner = getTypeOfAssetList(assetType);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spinnerTypeOfAsset.setAdapter(adapter);
    }

    private String[] getTypeOfAssetList(String assetType) {
        switch (assetType) {
            case "Office":
                return new String[]{
                        "Panchayat Bavan",
                        "Library/Reading Room",
                        "Common service center",
                        "Public distribution system",
                        "Primary Agriculture Credit society",
                        "Co-operation marketing society",
                        "Diary/Poultry/Fisheries co-operation society",
                        "Bank"
                };
            case "Hospital":
                return new String[]{
                        "Govt Hospital",
                        "PHC",
                        "Subcentre",
                        "Pvt Hospital",
                        "Pvt clinic"
                };
            case "SchoolCollage":
                return new String[]{
                        "Anganwadi",
                        "Nursery",
                        "Govt Primary School",
                        "Govt College",
                        "Pvt Primary school",
                        "Pvt College",
                        "Govt High School",
                        "Pvt High School"
                };
            case "WaterBody":
                return new String[]{
                        "Tap water",
                        "Well",
                        "Water Tank",
                        "Tubewell",
                        "Handpump",
                        "Canal",
                        "River",
                        "Lake",
                        "Watershed",
                        "Pond",
                        "Rainwater harvesting"
                };
            case "OtherPlaces":
                return new String[]{
                        "Petrol Bunk(examples)",
                        "Forest",
                        "Cemetery"
                };
        }
        return new String[]{};
    }

    private void setOnClick(VillagelevelmappingBinding binding) {
        setOnClickRadio(binding, binding.radioTemple);
        setOnClickRadio(binding, binding.radioMosque);
        setOnClickRadio(binding, binding.radioChurch);
        setOnClickRadio(binding, binding.radioHotel);
        setOnClickRadio(binding, binding.radioHospital);
        setOnClickRadio(binding, binding.radioOffice);
        setOnClickRadio(binding, binding.radioConstruction);
        setOnClickRadio(binding, binding.radioEducation);
        setOnClickRadio(binding, binding.radioBusStop);
        setOnClickRadio(binding, binding.radioWaterBody);
        setOnClickRadio(binding, binding.radioShop);
        setOnClickRadio(binding, binding.radioToilet);
        setOnClickRadio(binding, binding.radioPark);
        setOnClickRadio(binding, binding.radioOther);
    }

    private int CLICK_ACTION_THRESHOLD = 200;
    private float startX;
    private float startY;

    @SuppressLint("ClickableViewAccessibility")
    private void setOnClickRadio(VillagelevelmappingBinding binding, RadioButton button) {
        button.setOnTouchListener((view, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getX();
                    float endY = event.getY();
                    if (isAClick(startX, endX, startY, endY)) {
                        button.callOnClick();
                        populateSpinner(getSelectedAssetType(binding), binding);
                    }
                    break;
            }
            return true;
        });
    }

    private boolean isAClick(float startX, float endX, float startY, float endY) {
        float differenceX = Math.abs(startX - endX);
        float differenceY = Math.abs(startY - endY);
        return !(differenceX > CLICK_ACTION_THRESHOLD/* =5 */ || differenceY > CLICK_ACTION_THRESHOLD);
    }
}

