package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.microsoft.appcenter.analytics.Analytics
import com.ssf.homevisit.R
import com.ssf.homevisit.alerts.DisplayAlert
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.FragmentSelectPlaceBinding
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.inVisible
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Place
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.PlaceItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter.SelectPlaceAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.viewModel.SelectPlaceViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.AnalyticsEvents
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class SelectPlaceFragment : Fragment(),
    SelectPlaceAdapter.OnPlaceItemClick, OnMapReadyCallback {

    private val viewModel: SelectPlaceViewModel by viewModels()
    private lateinit var adapter: SelectPlaceAdapter
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101
    private var map: GoogleMap? = null
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var currentLocationMarker: Marker? = null
    private lateinit var surveillanceType: String
    var placeData: MutableList<Place> = mutableListOf()
    var currentIndex = 0
    private lateinit var fragmentBinding: FragmentSelectPlaceBinding
    var assetType: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_select_place, container, false
        )
        return fragmentBinding.root
    }


    private fun getImageUrl() {
        currentIndex=0
        while (placeData[currentIndex].imageUrl.isEmpty()) {
            placeData[currentIndex].imageUrl = emptyList()
            currentIndex += 1
            if (currentIndex >= placeData.size) {
                break
            }
        }
        if (currentIndex < placeData.size) {
            var checkUrlData = false
            for (url in placeData[currentIndex].imageUrl) {
                if (url.isNotEmpty()) {
                    checkUrlData = true
                    viewModel.getImageUrl(url)
                    break
                }
            }
            if (!checkUrlData) {
                viewModel.getImageUrl("")
            }
        } else {
            adapter.setData(placeData)
            adapter.notifyDataSetChanged()
        }
    }


    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.FLOW_TYPE)) {
                surveillanceType = it.getString(AppDefines.FLOW_TYPE).toString()
            }
            if (it.containsKey(AppDefines.ASSET_TYPE)) {
                assetType = it.getString(AppDefines.ASSET_TYPE).toString()
            }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        getData()
        initialization()
        initListener()
        setData()
        initObserver()
        initAdapter()
    }

    private fun getData() {
        viewModel.getPlaceList(larvaViewModel.villageUuid, assetType)
    }

    private fun initObserver() {

        viewModel._placeData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) {
                    fragmentBinding.progressBar.inVisible()
                    placeData = it as MutableList<Place>
                    if (placeData.size > 0) {
                        fragmentBinding.tvNoData.gone()
                        fragmentBinding.rvPlaceList.visible()
                        getImageUrl()
                    } else {
                        fragmentBinding.tvNoData.visible()
                        fragmentBinding.rvPlaceList.gone()
                    }
                } else {
                    fragmentBinding.tvNoData.visible()
                    fragmentBinding.rvPlaceList.gone()
                    fragmentBinding.progressBar.inVisible()

                }
            }

        }

        collectSharedFlowData(viewModel.placeDetails) {
            if (currentIndex < placeData.size) {
                val imageUrl = placeData[currentIndex].imageUrl as MutableList<String>
                if(imageUrl.isNotEmpty()){
                    imageUrl[0] = it
                }
                currentIndex += 1
                if (currentIndex < placeData.size) {
                    var checkUrlData = false
                    for (url in placeData[currentIndex].imageUrl) {
                        if (url.isNotEmpty()) {
                            checkUrlData = true
                            viewModel.getImageUrl(url)
                            break
                        }
                    }
                    if (!checkUrlData) {
                        viewModel.getImageUrl("")
                    }
                } else {
                    adapter.setData(placeData)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData() {
        fragmentBinding.progressBar.visible()
        when (surveillanceType) {
            AppDefines.WATER_SAMPLE_TEST -> {
                fragmentBinding.ivSt.background = context?.resources?.getDrawable(R.drawable.ic_tap)
                fragmentBinding.tvLocationTitle.text =
                    "You are in ${larvaViewModel.villageName} Village for Water Surveillance"
                fragmentBinding.tvSelectPlaceTitle.text = "Select a Place for Water Sample"
                fragmentBinding.tvSelectPlace.text = "Select a Place for Water Sample"
                fragmentBinding.ivSearchPlace.background =
                    context?.resources?.getDrawable(R.drawable.ic_water_sample)
            }
            AppDefines.LARVA_TEST -> {
                fragmentBinding.ivSearchPlace.background =
                    context?.resources?.getDrawable(R.drawable.ic_larva)
                fragmentBinding.ivSt.background =
                    context?.resources?.getDrawable(R.drawable.ic_larva)
                fragmentBinding.tvLocationTitle.text =
                    "You are in ${larvaViewModel.villageName} Village for Larva Surveillance"
                fragmentBinding.tvSelectPlaceTitle.text = "Select a Place for Larva Surveillance"
                fragmentBinding.tvSelectPlace.text = "Select a Place for Larva Surveillance"
            }
            AppDefines.IODINE_TEST -> {
                fragmentBinding.ivSt.background = context?.resources?.getDrawable(R.drawable.ic_iodine)
                fragmentBinding.tvLocationTitle.text="You are in ${larvaViewModel.villageName} Village for Iodine Surveillance"
                fragmentBinding.tvSelectPlaceTitle.text = "Select a Place for Iodine Test"
                fragmentBinding.tvSelectPlace.text="Select a Place for Iodine Test"
                fragmentBinding.ivSearchPlace.background =
                    context?.resources?.getDrawable(R.drawable.ic_iodine)
            }
        }
    }

    private fun initListener() {
        fragmentBinding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        fragmentBinding.cvChangeSurveillance.setOnClickListener {
            findNavController().navigate(R.id.selectSTFragment)
        }
    }


    override fun onPlaceClick(
        data: PlaceItem
    ) {
        val bundle = Bundle()
        bundle.putString(
            AppDefines.PLACE_DETAILS, Gson().toJson(data)
        )

        bundle.putString(AppDefines.ASSET_TYPE,assetType)
        if (surveillanceType == AppDefines.WATER_SAMPLE_TEST) {
            findNavController().navigate(R.id.updateWaterSampleFragment, bundle)
        }
        if (surveillanceType == AppDefines.LARVA_TEST) {
            bundle.putString(
                AppDefines.FLOW_TYPE, AppDefines.LARVA_TEST
            )
            findNavController().navigate(R.id.performLarvaSurveillance, bundle)
        }
        if (surveillanceType == AppDefines.IODINE_TEST) {
            bundle.putString(AppDefines.FLOW_TYPE, AppDefines.IODINE_TEST)
            findNavController().navigate(R.id.updateIodineTestFragment, bundle)
        }
    }

    private fun initAdapter() {
        context?.let {
            adapter = SelectPlaceAdapter(mutableListOf(), this, it)
        }
        fragmentBinding.rvPlaceList.adapter = adapter
    }

    private fun initialization() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        loadMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        fetchLocation()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>, grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    // todo fix this function
    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                AppController.getInstance().mainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { location ->
            location?.let {
                setCurrentLocation(it)
            }
        }

    }

    private fun setCurrentLocation(location: Location) {
        var alreadyZoomed = false
        if (currentLocationMarker != null) {
            currentLocationMarker!!.remove()
            alreadyZoomed = true
        }
        currentLocation = location
        if (!alreadyZoomed) {
            val cameraPosition = CameraPosition.Builder().target(
                LatLng(
                    location.latitude, location.longitude
                )
            )
                .zoom(30f) // Sets the zoom
                .bearing(90f) // Sets the orientation of the camera to east
                .tilt(40f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.latitude, location.longitude
                    ), 30f
                )
            )
        }
        val currentLatLng = LatLng(location.latitude, location.longitude)
        try {
            val currentLocationBitmap = getBitmap(R.drawable.map_pin_your_location)
            currentLocationMarker = map?.addMarker(
                MarkerOptions().position(currentLatLng)
                    .icon(currentLocationBitmap?.let { BitmapDescriptorFactory.fromBitmap(it) })
                    .title("Current Location").zIndex(1f)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        currentIndex = 0
        try {
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        } catch (ignore: Exception) {
            Analytics.trackEvent(AnalyticsEvents.LOCATION_LISTENER_NULL)
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
        } catch (ignore: Exception) {
            Analytics.trackEvent(AnalyticsEvents.LOCATION_LISTENER_NULL)
        }
        DisplayAlert.dismiss()
    }


    private fun getBitmap(drawableRes: Int): Bitmap? {
        val drawable=ContextCompat.getDrawable(this.requireContext(),drawableRes)
        drawable?.let {
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(
                    drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            canvas.setBitmap(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return  bitmap
        }
        return null
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        currentIndex = 0
    }


}