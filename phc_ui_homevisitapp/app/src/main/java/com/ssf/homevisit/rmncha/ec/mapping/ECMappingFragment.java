package com.ssf.homevisit.rmncha.ec.mapping;

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
import android.widget.Toast;

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
import com.ssf.homevisit.databinding.LayoutRmnchaEcMappingFragmentBinding;
import com.ssf.homevisit.models.PhcResponse;
import com.ssf.homevisit.models.RMNCHAHouseHoldResponse;
import com.ssf.homevisit.models.RMNCHAHouseHoldSearchResponse;
import com.ssf.homevisit.models.SubCVillResponse;
import com.ssf.homevisit.models.SubcentersFromPHCResponse;
import com.ssf.homevisit.models.VillageProperties;
import com.ssf.homevisit.rmncha.ec.selectwomen.SelectWomenForECActivity;

import java.util.List;

public class ECMappingFragment extends Fragment implements OnMapReadyCallback, EMMappingAdapter.RMNCHAHHMemberAdapterListener {
    private static final int REQUEST_CODE = 101;
    View view;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private LayoutRmnchaEcMappingFragmentBinding binding;
    private GoogleMap map;
    private Marker currentLocationMarker;
    private ECMappingFragmentViewModel viewModel;
    private EMMappingAdapter mEMMappingAdapter;
    VillageProperties villageProperties;
    String villageName = "";
    PhcResponse.Content rmnchaSelectedPhc;
    SubcentersFromPHCResponse.Content rmnchaSelectedSubCenter;
    SubCVillResponse.Content rmnchaSelectedVillage;

    List<RMNCHAHouseHoldResponse.Content> contentList;
    List<RMNCHAHouseHoldSearchResponse.Content> searchContentList;

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

    public ECMappingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_rmncha_ec_mapping_fragment, container, false);
        view = binding.getRoot();

        // View Model
        viewModel = new ECMappingFragmentViewModel(this.getActivity().getApplication());
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
    }

    private void initialization() {
        binding.youAreInVillageMessage.setText(getResources().getString(R.string.hh_you_are_in_village_message).replace("@@", villageName));
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AppController.getInstance().getApplicationContext());
        binding.footerTv.setText(Html.fromHtml(getContext().getString(R.string.powered_by_)));
        loadMap();
        //fetchLocation();
        getAllHouseHolds();
        binding.showCompleteList.setOnClickListener(view -> getAllHouseHolds());
        binding.findByLocation.setOnClickListener(view -> {
            if (currentLocation != null) {
                getHouseHoldsNearMe(villageProperties.getUuid(), currentLocation.getLatitude(), currentLocation.getLongitude());
            } else {
                loadMap();
            }
        });

        binding.searchByHeadName.setOnClickListener(view -> {
            String query = binding.searchByName.getText() + "";
            if (query.length() > 0) {
                hideSoftInputKeyboard(getContext(), view);
                getHouseHoldsByHeadName(query, villageProperties.getLgdCode());
            } else {
                showMyToast(getContext(), "Enter Head Name to Search");
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        fetchLocation();
        /*map.setOnMarkerClickListener(marker -> {
            DisplayAlert.getInstance(getActivity()).displayMappingAlert(villageProperties, this::onLocationFetched);
            return false;
        });*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation();
            }
        }
    }

    private void getAllHouseHolds() {
        viewModel.getAllHouseholdResponseLiveData(villageProperties.getUuid()).observe(getViewLifecycleOwner(), new Observer<RMNCHAHouseHoldResponse>() {
            @Override
            public void onChanged(RMNCHAHouseHoldResponse rmnchaHouseHoldResponse) {
                if (rmnchaHouseHoldResponse != null) {
                    contentList = rmnchaHouseHoldResponse.getContent();
                    setAllHouseHoldsAdapter();
                    for (RMNCHAHouseHoldResponse.Content content : contentList) {
                        try {
                            RMNCHAHouseHoldResponse.Content.Target.TargetProperties houseHoldProperties = content.getTarget().getProperties();
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
                            Log.e("Error ", "Invalid long, lat " + e.getMessage());
                        }
                    }
                } else {
                    showMyToast(getActivity(), "Error fetching HH data  : " + viewModel.getErrorMessage());
                }
            }
        });
    }

    private void getHouseHoldsNearMe(String uuid, double latitude, double longitude) {
        viewModel.getAllHouseholdByLatLongResponseLiveData(uuid, latitude, longitude).observe(getViewLifecycleOwner(), new Observer<RMNCHAHouseHoldResponse>() {
            @Override
            public void onChanged(RMNCHAHouseHoldResponse rmnchaHouseHoldResponse) {
                if (rmnchaHouseHoldResponse != null) {
                    contentList = rmnchaHouseHoldResponse.getContent();
                    setHouseHoldsNearMeAdapter();
                    for (RMNCHAHouseHoldResponse.Content content : contentList) {
                        try {
                            RMNCHAHouseHoldResponse.Content.Target.TargetProperties houseHoldProperties = content.getTarget().getProperties();
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
                            Log.e("Error ", "Invalid long, lat " + e.getMessage());
                        }
                    }
                } else {
                    showMyToast(getActivity(), "Error fetching HH NearMe  : " + viewModel.getErrorMessage());
                }
            }
        });
    }

    private void getHouseHoldsByHeadName(String headName, String lgdCode) {
        viewModel.getHouseHoldsByHeadName(headName, lgdCode).observe(getViewLifecycleOwner(), new Observer<RMNCHAHouseHoldSearchResponse>() {
            @Override
            public void onChanged(RMNCHAHouseHoldSearchResponse rmnchaHouseHoldSearchResponse) {
                if (rmnchaHouseHoldSearchResponse != null) {
                    searchContentList = rmnchaHouseHoldSearchResponse.getContent();
                    setHouseHoldsByHeadNameAdapter();
                    for (RMNCHAHouseHoldSearchResponse.Content content : searchContentList) {
                        try {
                            RMNCHAHouseHoldSearchResponse.Content.Properties houseHoldProperties = content.getProperties();
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
                            Log.e("Error ", "Invalid long, lat " + e.getMessage());
                        }
                    }
                } else {
                    showMyToast(getActivity(), "Error getting HH by Name  : " + viewModel.getErrorMessage());
                }
            }
        });
    }

    private void setAllHouseHoldsAdapter() {
        binding.showCompleteList.setTextColor(getResources().getColor(R.color.rmncha_unselected_color));
        binding.findByLocation.setTextColor(getResources().getColor(R.color.button_blue));
        setAdapter();
    }

    private void setHouseHoldsNearMeAdapter() {
        binding.showCompleteList.setTextColor(getResources().getColor(R.color.button_blue));
        binding.findByLocation.setTextColor(getResources().getColor(R.color.rmncha_unselected_color));
        setAdapter();
    }

    private void setHouseHoldsByHeadNameAdapter() {
        binding.showCompleteList.setTextColor(getResources().getColor(R.color.button_blue));
        binding.findByLocation.setTextColor(getResources().getColor(R.color.button_blue));
        setSearchAdapter();
    }

    private void setAdapter() {
        mEMMappingAdapter = new EMMappingAdapter(getContext(), contentList, this);
        binding.memberRecyclerView.setAdapter(mEMMappingAdapter);
        binding.memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void setSearchAdapter() {
        mEMMappingAdapter = new EMMappingAdapter(getContext(), searchContentList, true, this);
        binding.memberRecyclerView.setAdapter(mEMMappingAdapter);
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
                Toast.makeText(requireContext(), "We could not fetch your location!", Toast.LENGTH_SHORT).show();
            }
        });
        task.addOnFailureListener(rr -> {
            Toast.makeText(requireContext(), "We could not fetch your location!", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onECHouseholdClicked(int position, boolean searchTypeFlag) {
        Intent intent = new Intent(getActivity(), SelectWomenForECActivity.class);
        if (searchTypeFlag) {
            intent.putExtra(SelectWomenForECActivity.PARAM_1, searchContentList.get(position).getProperties().getUuid());
        } else {
            intent.putExtra(SelectWomenForECActivity.PARAM_1, contentList.get(position).getTarget().getProperties().getUuid());
        }
        intent.putExtra(SelectWomenForECActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter.getTarget().getProperties().getUuid());
        getContext().startActivity(intent);
    }

}