package com.ssf.homevisit.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.microsoft.appcenter.analytics.Analytics;
import com.ssf.homevisit.R;
import com.ssf.homevisit.alerts.DisplayAlert;
import com.ssf.homevisit.alerts.VillageLevelMappingAlert;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.FragmentSociaEcoSurveyBinding;
import com.ssf.homevisit.factories.VillageLevelMappingViewModelFactory;
import com.ssf.homevisit.models.PlaceProperties;
import com.ssf.homevisit.models.PlacesCountResponse;
import com.ssf.homevisit.models.PlacesInVillageResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.utils.AnalyticsEvents;
import com.ssf.homevisit.viewmodel.VillageLevelMappingViewModel;
import com.ssf.homevisit.views.MarkedPlacesView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class SociaEcoSurveyFragment extends Fragment implements OnMapReadyCallback {
    View view;
    private FragmentSociaEcoSurveyBinding binding;
    private VillageLevelMappingViewModel villageLevelMappingViewModel;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    private GoogleMap map;
    private DisplayAlert displayAlert;
    private Marker currentLocationMarker;
    private VillageProperties village;
    private LocationCallback locationCallback;
    private String phcName;
    private String subCenterName;
    private boolean countFetched = false;

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

    public VillageProperties getVillage() {
        return village;
    }

    public void setVillage(VillageProperties village) {
        this.village = village;
    }


    public SociaEcoSurveyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_socia_eco_survey, container, false);
        view = binding.getRoot();

        // View Model
        villageLevelMappingViewModel = new ViewModelProvider(
                this,
                new VillageLevelMappingViewModelFactory(this.getActivity().getApplication(), village, subCenterName, phcName)
        ).get(VillageLevelMappingViewModel.class);

        //set view model
        binding.setViewModel(villageLevelMappingViewModel);
        binding.setLifecycleOwner(this);
        intialization();
        binding.backButton.setOnClickListener(view -> getActivity().onBackPressed());
        return view;
    }

    private void intialization() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AppController.getInstance().getApplicationContext());
        loadMap();
        //fetchLocation();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        fetchLocation();
        map.setOnMarkerClickListener(marker -> {
            if (Objects.equals(marker.getTitle(), "Current Location"))
                DisplayAlert.getInstance(getActivity()).displayMappingAlert(village.getLgdCode(), this::openVillageLevelMappingAlert);
            return false;
        });
        showVillageAssets();
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

    private void showAssetStatus() {
        binding.markedPlacesContainer.removeAllViews();
        int[] colors = new int[]{Color.parseColor("#F44336"), Color.parseColor("#9C27B0"), Color.parseColor("#3F51B5")};
        villageLevelMappingViewModel.getPlacesInCount().observe(getViewLifecycleOwner(), placesCountResponse -> {
            int index = 0, total = 0;
            if (placesCountResponse == null) {
                Analytics.trackEvent(AnalyticsEvents.PLACES_DATA_GOT_NULl);
                return;
            }
            binding.markedPlacesContainer.removeAllViews();
            for (PlacesCountResponse.Content content : placesCountResponse.getContent()
            ) {
                if (content.getTypeCount() != 0) {
                    MarkedPlacesView markedPlacesView = new MarkedPlacesView(getActivity());
                    markedPlacesView.setAssetType(content.getAssetType());
                    markedPlacesView.setAssetCount(content.getTypeCount());
                    markedPlacesView.setColor(colors[index % 3]);
                    total += content.getTypeCount();
                    binding.markedPlacesContainer.addView(markedPlacesView);
                    index++;
                }
            }
            binding.totalCountText.setText(total + "");
        });
    }

    private void openVillageLevelMappingAlert(double latitude, double longitude) {
        VillageLevelMappingAlert.getInstance(getActivity()).displayMappingAlert(this, latitude, longitude, village, this::showVillageAssets);
    }

    private void showVillageAssets() {
        showAssetStatus();
        villageLevelMappingViewModel.getPlacesInVillageData().observe(this, placesInVillageResponse -> {
            if (placesInVillageResponse != null)
                for (PlacesInVillageResponse.Content content : placesInVillageResponse.getContent()) {
                    try {
                        PlaceProperties placeProperties = content.getTarget().getProperties();
                        double longitude = placeProperties.getLongitude();
                        double latitude = placeProperties.getLatitude();
                        LatLng latLng = new LatLng(latitude, longitude);

                        map.addMarker(
                                new MarkerOptions()
                                        .position(latLng)
                                        .title(placeProperties.getName())
                                        .icon(BitmapDescriptorFactory
                                                .fromBitmap(Bitmap.createScaledBitmap(getBitmap(getMapPinAsset(placeProperties.getAssetType())), 110, 110, false)))
                        );

                    } catch (Exception e) {
                        Log.e("Error ", e.getMessage());
                    }
                }
        });
    }

    // todo fix this function
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
                Log.d("provider " + provider, "null");
                continue;
            }
            Log.d("provider " + provider, l.getAccuracy() + "");
            if (location == null || l.getAccuracy() < location.getAccuracy()) {
                // Found best last known location: %s", l);
                location = l;
            }
        }

        if (location != null) {
            setCurrentLocation(location);
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(llocation -> {
            if (llocation != null) {
                Log.d("provider " + llocation.getProvider(), llocation.getAccuracy() + "");
                if (currentLocation == null || llocation.getAccuracy() < currentLocation.getAccuracy()) {
                    setCurrentLocation(llocation);
                }
            } else {
                Log.d("provider fused ", "Null");
            }
        });
        task.addOnFailureListener(rr -> {
            Log.e("loc", rr.toString());
        });
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        if (currentLocation == null || location.getAccuracy() <= currentLocation.getAccuracy()) {
                            setCurrentLocation(location);
                        }
                    }
                }
            }

            @Override
            public void onLocationAvailability(@NonNull LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
            }
        };
        fusedLocationProviderClient.requestLocationUpdates(new LocationRequest().setInterval(2000).setMaxWaitTime(2000),
                locationCallback,
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("DESTROY", "DESTROY");

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("destroyed", "view");
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (Exception ignore) {
            Analytics.trackEvent(AnalyticsEvents.LOCATION_LISTENER_NULL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (Exception ignore) {
            Analytics.trackEvent(AnalyticsEvents.LOCATION_LISTENER_NULL);
        }
        DisplayAlert.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_1 || requestCode == VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_2) {
            VillageLevelMappingAlert.getInstance(getContext()).onCameraResult(requestCode, data);
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

    private int getMapPinAsset(String assetType) {
        Log.d("asdfasdaf", assetType);
        switch (assetType) {
            case "Temple":
                return R.drawable.map_pin_temple;
            case "Mosque":
                return R.drawable.map_pin_mosque;
            case "Church":
                return R.drawable.map_pin_church;
            case "Hotel":
                return R.drawable.map_pin_hotel;
            case "Hospital":
                return R.drawable.map_pin_hospital;
            case "Office":
                return R.drawable.map_pin_office;
            case "Construction":
                return R.drawable.map_pin_construction;
            case "Shop":
                return R.drawable.map_pin_shop;
            case "SchoolCollage":
                return R.drawable.map_pin_school;
            case "BusStop":
                return R.drawable.map_pin_bus_stop;
            case "WaterBody":
                return R.drawable.map_pin_water_body;
            case "Toilet":
                return R.drawable.map_pin_toilet;
            case "Park":
                return R.drawable.map_pin_park;
        }
        Log.d("not found", assetType);
        return R.drawable.map_pin_shop;
    }

    private String getAddress() {
        String strAddress = null;
        Geocoder gc = new Geocoder(AppController.getInstance().getApplicationContext());

        if (gc.isPresent()) {
            List<Address> list = null;
            try {
                list = gc.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                Address address = list.get(0);

                StringBuffer str = new StringBuffer();
                str.append("Name: " + address.getLocality() + "\n");
                str.append("Sub-Admin Ares: " + address.getSubAdminArea() + "\n");
                str.append("Admin Area: " + address.getAdminArea() + "\n");
                str.append("Country: " + address.getCountryName() + "\n");
                str.append("Country Code: " + address.getCountryCode() + "\n");
                strAddress = str.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return strAddress;
    }

    private void loadMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);

    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {

                }
            }
        });
    }

    public interface OnVillageAssetAdd {
        public void onVillageAssetAdd();
    }
}