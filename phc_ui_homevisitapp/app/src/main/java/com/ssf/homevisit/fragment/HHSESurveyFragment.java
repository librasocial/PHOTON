package com.ssf.homevisit.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.ssf.homevisit.R;
import com.ssf.homevisit.alerts.HHSESNearbyHousesAlert;
import com.ssf.homevisit.alerts.HouseholdAlert;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.HHSESMappingFragmentBinding;
import com.ssf.homevisit.factories.HHSESurveyViewModelFactory;
import com.ssf.homevisit.models.HouseHoldInVillageResponse;
import com.ssf.homevisit.models.HouseHoldProperties;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.viewmodel.HHSESurveyViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HHSESurveyFragment extends Fragment implements OnMapReadyCallback {

    private HHSESurveyViewModel mViewModel;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private HHSESMappingFragmentBinding binding;
    private Marker currentLocationMarker;
    private VillageProperties village;
    private GoogleMap map;

    public static HHSESurveyFragment newInstance() {
        return new HHSESurveyFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.h_h_s_e_s_mapping_fragment, container, false);
        mViewModel = new ViewModelProvider(
                this,
                new HHSESurveyViewModelFactory(this.getActivity().getApplication(), village, "subCenterName", "phcName")
        ).get(HHSESurveyViewModel.class);
        binding.setViewModel(mViewModel);
        binding.backButton.setOnClickListener(view -> {
            handleBackPress();
        });
        intialization();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void handleBackPress() {
        requireActivity().onBackPressed();
    }

    private void intialization() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AppController.getInstance().getApplicationContext());
        loadMap();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }


    public VillageProperties getVillage() {
        return village;
    }

    public void setVillage(VillageProperties village) {
        this.village = village;
    }

    private String phcName;

    public String getPhcName() {
        return phcName;
    }

    public void setPhcName(String phcName) {
        this.phcName = phcName;
    }

    public String getSubCenterName() {
        return subCenterName;
    }

    public void setSubCenterName(String subCenterName) {
        this.subCenterName = subCenterName;
    }

    private String subCenterName;

    private void showVillageAssets() {
        mViewModel.getHouseHoldInVillageData().observe(this, new Observer<HouseHoldInVillageResponse>() {
            @Override
            public void onChanged(HouseHoldInVillageResponse houseHoldInVillageResponse) {

                try {
                    for (HouseHoldInVillageResponse.Content content : houseHoldInVillageResponse.getContent()) {
                        HouseHoldProperties houseHoldProperties = content.getTarget().getProperties();
                        double longitude = houseHoldProperties.getLongitude();
                        double latitude = houseHoldProperties.getLatitude();
                        LatLng latLng = new LatLng(latitude, longitude);

                        map.addMarker(
                                new MarkerOptions()
                                        .position(latLng)
                                        .title(houseHoldProperties.getHouseID())
                                        .icon(BitmapDescriptorFactory
                                                .fromBitmap(Bitmap.createScaledBitmap(getBitmap(getHousePinAsset()), 50, 50, false)))
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AppController.getInstance().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppController.getInstance().getMainActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }
        LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        Location location = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            Log.d("provider " + provider, l.getAccuracy() + "");
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                location = l;
            }
        }

        if (location != null) {
            setCurrentLocation(location);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(llocation -> {
            if (llocation != null) {
                if (currentLocation == null || llocation.getAccuracy() < currentLocation.getAccuracy()) {
                    setCurrentLocation(llocation);
                }
            }
        });
        fusedLocationProviderClient.requestLocationUpdates(new LocationRequest().setInterval(2000).setMaxWaitTime(2000),
                new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        for (Location location : locationResult.getLocations()) {
                            if (location != null) {
                                if (currentLocation == null || location.getAccuracy() <= currentLocation.getAccuracy())
                                    setCurrentLocation(location);
                            }
                        }
                    }

                    @Override
                    public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                        super.onLocationAvailability(locationAvailability);
                    }
                },
                Looper.getMainLooper());
    }

    private void setCurrentLocation(Location location) {
        boolean alreadyZoomed = false;
        if (currentLocationMarker != null) {
            currentLocationMarker.remove();
            alreadyZoomed = true;
        }
        currentLocation = location;
        if (!alreadyZoomed) {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(30)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder

            map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 30));
        }
        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        try {
            Bitmap currentLocationBitmap = getBitmap(R.drawable.map_pin_your_location);
            currentLocationMarker = map.addMarker(new MarkerOptions().position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(currentLocationBitmap))
                    .title("Current Location")
                    .zIndex(1));
        } catch (Exception e) {
            Log.e("some error", "error occurred while getting bitmap");
            e.printStackTrace();
        }
    }

    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private int getHousePinAsset() {
        return R.drawable.housemap;
    }

    private void loadMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                map = googleMap;
                fetchLocation();
                map.setOnMarkerClickListener(marker -> {
                    HHSESNearbyHousesAlert.getInstance(getContext()).displayMappingAlert(HHSESurveyFragment.this, village, subCenterName, phcName, currentLocation.getLatitude(), currentLocation.getLongitude(), HHSESNearbyHousesAlert.HH_LEVEL_SURVEY);
                    return false;
                });
                showVillageAssets();
            }
        });

    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= HouseholdAlert.cameraResultStart && requestCode <= HouseholdAlert.cameraResultEnd) {
            HouseholdAlert.getInstance(getContext()).onImageCaptured(requestCode-HouseholdAlert.cameraResultStart);
        }
    }
}