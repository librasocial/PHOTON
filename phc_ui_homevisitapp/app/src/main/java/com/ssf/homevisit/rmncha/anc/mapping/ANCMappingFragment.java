package com.ssf.homevisit.rmncha.anc.mapping;

import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.hideSoftInputKeyboard;
import static com.ssf.homevisit.rmncha.base.RMNCHAUtils.showMyToast;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.ssf.homevisit.activity.MainActivity;
import com.ssf.homevisit.alerts.HouseholdAlert;
import com.ssf.homevisit.controller.AppController;
import com.ssf.homevisit.databinding.LayoutRmnchaAncMappingFragmentBinding;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.RMNCHAANCHouseholdsResponse;
import com.ssf.homevisit.models.RMNCHAANCPWsResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.rmncha.anc.pwregistration.ANCPWRegistrationActivity;
import com.ssf.homevisit.rmncha.anc.pwtracking.ANCPWTrackingActivity;
import com.ssf.homevisit.rmncha.anc.selectwomen.SelectWomenForANCActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ANCMappingFragment extends Fragment implements OnMapReadyCallback, ANCMappingAdapter.ANCMappingItemListener {
    private static final int REQUEST_CODE = 101;
    View view;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LayoutRmnchaAncMappingFragmentBinding binding;
    private GoogleMap map;
    private Marker currentLocationMarker;
    private ANCMappingFragmentViewModel viewModel;
    private ANCMappingAdapter mRMNCHAHouseHoldAdapter;
    VillageProperties villageProperties;
    String villageName = "";
    PhcResponse.Content rmnchaSelectedPhc;
    SubcentersFromPHCResponse.Content rmnchaSelectedSubCenter;
    SubCVillResponse.Content rmnchaSelectedVillage;

    List<RMNCHAANCHouseholdsResponse.Content> houseHoldsList;
    List<RMNCHAANCPWsResponse.Content> pwsList;
    boolean allFlag = true;

    public void setPhc(PhcResponse.Content phcName) {
        this.rmnchaSelectedPhc = phcName;
    }

    public void setSubCenter(SubcentersFromPHCResponse.Content subCenterName) {
        this.rmnchaSelectedSubCenter = subCenterName;
    }

    public void setVillage(SubCVillResponse.Content village) {
        this.rmnchaSelectedVillage = village;
        villageProperties = village.getTarget().getVillageProperties();
        if (villageProperties != null)
            villageName = villageProperties.getName();
    }

    public ANCMappingFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_rmncha_anc_mapping_fragment, container, false);
        view = binding.getRoot();
        viewModel = new ANCMappingFragmentViewModel(this.getActivity().getApplication());
        binding.setLifecycleOwner(this);
        binding.setViewModel(viewModel);
        binding.backButton.setOnClickListener(view -> getActivity().onBackPressed());
        initialization();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setActionBarVisibility(View.VISIBLE);
        if (binding.countHeader.getVisibility() == View.VISIBLE) {
            getANCHouseHolds();
        } else {
            getANCPWs();
        }
    }

    private void initialization() {
        binding.youAreInVillageMessage.setText(getResources().getString(R.string.hh_anc_you_are_in_village_message).replace("@@", villageName));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AppController.getInstance().getApplicationContext());
        binding.footerTv.setText(Html.fromHtml(getContext().getString(R.string.powered_by_)));
        loadMap();

        binding.showCompleteList.setOnClickListener(view -> {
            getANCHouseHolds();
            binding.headerTitle.setText(getResources().getString(R.string.hh_select_anc_hh_message));
        });
        binding.showListOfPw.setOnClickListener(view -> {
            binding.headerTitle.setText(getResources().getString(R.string.hh_select_pw_anc_message));
            getANCPWs();
        });

        binding.searchByHeadName.setOnClickListener(view -> {
            String query = binding.searchByName.getText() + "";
            if (query.length() > 2) {
                hideSoftInputKeyboard(getContext(), view);
                filterByName(query);
            } else {
                showMyToast(getContext(), "Enter Valid Name to Search");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        fetchLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
        }
    }

    private void getANCHouseHolds() {
        showProgressbar();
        viewModel.getANCHouseholdResponseLiveData(villageProperties.getUuid()).observe(getViewLifecycleOwner(), new Observer<RMNCHAANCHouseholdsResponse>() {
            @Override
            public void onChanged(RMNCHAANCHouseholdsResponse rmnchaancHouseholdsResponse) {
                hideProgressbar();
                if (rmnchaancHouseholdsResponse != null) {
                    houseHoldsList = rmnchaancHouseholdsResponse.getContent();
                    setAllHouseHoldsAdapter();
                    for (RMNCHAANCHouseholdsResponse.Content content : houseHoldsList) {
                        try {
                            RMNCHAANCHouseholdsResponse.Content.Source.SourceProperties houseHoldProperties = content.getSource().getProperties();
                            double longitude = houseHoldProperties.getLongitude();
                            double latitude = houseHoldProperties.getLatitude();
                            if ((latitude + "").length() > 0 && (longitude + "").length() > 0) {
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
                } else {
                    showMyToast(getActivity(), "Error fetching HH data : " + viewModel.getErrorMessage());
                }
            }
        });
    }

    private void getANCPWs() {
        showProgressbar();
        viewModel.getANCPWsResponseLiveData(villageProperties.getUuid()).observe(getViewLifecycleOwner(), new Observer<RMNCHAANCPWsResponse>() {
            @Override
            public void onChanged(RMNCHAANCPWsResponse rmnchaancpWsResponse) {
                hideProgressbar();
                if (rmnchaancpWsResponse != null) {
                    pwsList = rmnchaancpWsResponse.getContent();
                    setPWAdapter();
                    for (RMNCHAANCPWsResponse.Content content : pwsList) {
                        try {
                            RMNCHAANCPWsResponse.Content.Source.SourceProperties houseHoldProperties = content.getSource().getProperties();
                            double longitude = houseHoldProperties.getLongitude();
                            double latitude = houseHoldProperties.getLatitude();
                            if ((latitude + "").length() > 0 && (longitude + "").length() > 0) {
                                LatLng latLng = new LatLng(latitude, longitude);
                                map.addMarker(
                                        new MarkerOptions()
                                                .position(latLng)
                                                .title(houseHoldProperties.getFirstName())
                                                .icon(BitmapDescriptorFactory
                                                        .fromBitmap(Bitmap.createScaledBitmap(getBitmap(getHousePinAsset()), 50, 50, false)))
                                );
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    showMyToast(getActivity(), "Error fetching PW's data : " + viewModel.getErrorMessage());
                }
            }
        });
    }

    private void filterByName(String name) {
        if (name != null && name.length() > 2) {
            if (allFlag) {
                setAdapter(true, filterHHListByName(name), null);
            } else {
                setAdapter(false, null, filterPWListByName(name));
            }
        } else {
            showMyToast(getActivity(), "Enter Atleast three Character");
        }
    }

    private List<RMNCHAANCPWsResponse.Content> filterPWListByName(String name) {
        List<RMNCHAANCPWsResponse.Content> filterList = new ArrayList<>();
        for (RMNCHAANCPWsResponse.Content item : pwsList) {
                if (item.getSource().getProperties().getFirstName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                filterList.add(item);
            }
        }
        return filterList;
    }

    private List<RMNCHAANCHouseholdsResponse.Content> filterHHListByName(String name) {
        List<RMNCHAANCHouseholdsResponse.Content> filterList = new ArrayList<>();
        for (RMNCHAANCHouseholdsResponse.Content item : houseHoldsList) {
            if (item.getSource().getProperties().getHouseHeadName().toLowerCase(Locale.ROOT).contains(name.toLowerCase(Locale.ROOT))) {
                filterList.add(item);
            }
        }
        return filterList;
    }

    private void setAllHouseHoldsAdapter() {
        binding.nameHeader.setText("Name of HH Head");
        binding.countHeader.setVisibility(View.VISIBLE);
        binding.showCompleteList.setTextColor(getResources().getColor(R.color.rmncha_unselected_color));
        binding.showListOfPw.setTextColor(getResources().getColor(R.color.button_blue));
        binding.ancTitle.setText(getResources().getString(R.string.anc_hh_header_message));
        setAdapter(true, houseHoldsList, null);
    }

    private void setPWAdapter() {
        binding.nameHeader.setText("Pregnant Women Names");
        binding.countHeader.setVisibility(View.GONE);
        binding.showCompleteList.setTextColor(getResources().getColor(R.color.button_blue));
        binding.showListOfPw.setTextColor(getResources().getColor(R.color.rmncha_unselected_color));
        binding.ancTitle.setText(getResources().getString(R.string.anc_pw_header_message));
        setAdapter(false, null, pwsList);
    }

    private void setAdapter(boolean flag, List<RMNCHAANCHouseholdsResponse.Content> houseHoldsList, List<RMNCHAANCPWsResponse.Content> pwsList) {
        allFlag = flag;
        mRMNCHAHouseHoldAdapter = new ANCMappingAdapter(getContext(), houseHoldsList, pwsList, this, flag);
        binding.memberRecyclerView.setAdapter(mRMNCHAHouseHoldAdapter);
        binding.memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
            Bitmap currentLocationBitmap = getMarkerBitmapFromView(R.drawable.map_pin_your_location);
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

    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_custom_marker, null);
        LinearLayout layout = (LinearLayout) customMarkerView.findViewById(R.id.background);
        layout.setBackground(getResources().getDrawable(R.drawable.ic_marker_orange));
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        // markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    private int getHousePinAsset() {
        return R.drawable.housemap;
    }

    private void loadMap() {
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.rmncha_map);
        assert supportMapFragment != null;
        supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode >= HouseholdAlert.cameraResultStart && requestCode <= HouseholdAlert.cameraResultEnd) {
            HouseholdAlert.getInstance(getContext()).onImageCaptured(requestCode - HouseholdAlert.cameraResultStart);
        }
    }

    private void showProgressbar() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressbar() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void goToANCService(RMNCHAANCPWsResponse.Content selectedWomen) {
        Intent intent = new Intent(getActivity(), ANCPWTrackingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SelectWomenForANCActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter.getTarget().getProperties().getUuid());
        bundle.putSerializable(SelectWomenForANCActivity.PARAM_ANC_SELECTED_WOMEN, selectedWomen);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void goToANCRegistration(RMNCHAANCPWsResponse.Content selectedWomen) {
        Intent intent = new Intent(getActivity(), ANCPWRegistrationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(SelectWomenForANCActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter.getTarget().getProperties().getUuid());
        bundle.putSerializable(SelectWomenForANCActivity.PARAM_ANC_SELECTED_WOMEN, selectedWomen);
        intent.putExtras(bundle);
        getActivity().startActivity(intent);
    }

    @Override
    public void goToSelectWomenForANC(RMNCHAANCHouseholdsResponse.Content content) {
        // Select Women in HH
        Intent intent = new Intent(getActivity(), SelectWomenForANCActivity.class);
        intent.putExtra(SelectWomenForANCActivity.PARAM_1, content.getSource().getProperties().getUuid());
        intent.putExtra(SelectWomenForANCActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter.getTarget().getProperties().getUuid());
        getActivity().startActivity(intent);
    }
}