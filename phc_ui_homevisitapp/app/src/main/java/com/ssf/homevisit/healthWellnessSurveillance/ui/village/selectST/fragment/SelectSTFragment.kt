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
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.font.Typeface
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
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
import com.ssf.homevisit.databinding.FragmentSelectStBinding
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.SelectST
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter.AssetAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter.HouseHoldAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter.SelectSTAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.viewModel.SelectSTViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.AnalyticsEvents
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.IOException

@AndroidEntryPoint
class SelectSTFragment :
    Fragment(),
    SelectSTAdapter.OnItemClick, AssetAdapter.OnAssetClick, OnMapReadyCallback,
    HouseHoldAdapter.OnHouseHoldItemClick {

    private lateinit var selectSTAdapter: SelectSTAdapter
    private lateinit var assetAdapter: AssetAdapter
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private lateinit var householdAdapter: HouseHoldAdapter
    private val viewModel: SelectSTViewModel by viewModels()
    private val mainViewModel: LarvaViewModel by activityViewModels()
    private lateinit var surveillanceType: String
    private lateinit var flowType: String
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101
    private var map: GoogleMap? = null
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var currentLocationMarker: Marker? = null
    private var searchName: String = ""
    var currentIndex = 0
    private lateinit var fragmentBinding: FragmentSelectStBinding
    var houseHoldData: MutableList<HouseHoldDataItem> = mutableListOf()
    private var assetName: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_select_st, container, false
        )
        return fragmentBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initArguments() {
        flowType = mainViewModel.flowType
        when (flowType) {
            AppDefines.HNW_VILLAGE_SURVEILLANCE -> {
                fragmentBinding.tvSelectAsset.let {
                    it.setTypeface(null, android.graphics.Typeface.BOLD)
                    it.visible()
                    it.text="Select an Asset for Surveillance"
                }
                fragmentBinding.cvHeader.gone()
                fragmentBinding.ivSelectAsset.visible()
                fragmentBinding.btnContinue.visibility = View.VISIBLE
                fragmentBinding.surveyType.text = "Village Level"
                fragmentBinding.tvSelectPlace.text = "Select Surveillance Type"
                fragmentBinding.llFooter.visibility = View.GONE
                fragmentBinding.mapView.visibility = View.GONE
            }
            AppDefines.HNW_HOUSEHOLD_SURVEILLANCE -> {
                fragmentBinding.tvSelectAsset.gone()
                fragmentBinding.ivSelectAsset.gone()
                fragmentBinding.tvNoData.gone()
                fragmentBinding.rvHouseholdList.gone()
                fragmentBinding.footerLogoFirst.gone()
                fragmentBinding.plus.gone()
                fragmentBinding.footerLogoTwo.gone()
                fragmentBinding.surveyType.text = "Household Level"
                fragmentBinding.tvSelectPlace.text = "Selection of Households"
                fragmentBinding.llFooter.visibility = View.VISIBLE
                fragmentBinding.btnContinue.visibility = View.GONE
                initialization()

            }
        }
    }


    private fun initialization() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        loadMap()
    }

    private fun initAdapter() {
            if (viewModel.assetType.isNotEmpty()) {
                viewModel.getSurveillanceTypeData(flowType)
                viewModel.surveillanceTypeData[viewModel.surveillanceTypePosition].isSelected = true
                context?.let {
                    selectSTAdapter = SelectSTAdapter(viewModel.surveillanceTypeData, this, it)
                }
                selectSTAdapter.lastSelectedIndex = viewModel.surveillanceTypePosition
                fragmentBinding.rvSelectSt.adapter = selectSTAdapter
                if (viewModel.assetType == AppDefines.LARVA_TEST) {
                    fragmentBinding.tvSelectHousehold.text =
                        "Select a Household for Larva Test"
                }
                if (viewModel.assetType == AppDefines.IODINE_TEST) {
                    fragmentBinding.tvSelectHousehold.text =
                        "Select a Household for Iodine Test"
                }
            } else {
                viewModel.getSurveillanceTypeData(flowType)
                context?.let {
                    selectSTAdapter = SelectSTAdapter(viewModel.surveillanceTypeData, this, it)
                }
                fragmentBinding.rvSelectSt.adapter = selectSTAdapter
            }
        if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
            context?.let {
                assetAdapter = AssetAdapter(mutableListOf(), this, it)
            }
            fragmentBinding.rvSelectAsset.adapter = assetAdapter
            fragmentBinding.rvSelectAsset.layoutManager = GridLayoutManager(context, 7)
        }
        if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
            householdAdapter = HouseHoldAdapter(mutableListOf(), this)
            fragmentBinding.rvHouseholdList.adapter = householdAdapter
        }

    }


    @SuppressLint("SetTextI18n")
    override fun onItemClick(
        data: MutableList<SelectST>, currentPosition: Int, previousPosition: Int
    ) {
        surveillanceType = data[currentPosition].type
        if (previousPosition != -1) {
            data[previousPosition].isSelected = false
            data[currentPosition].isSelected = true
            selectSTAdapter.notifyItemChanged(previousPosition)
            selectSTAdapter.notifyItemChanged(currentPosition)
        } else {
            data[currentPosition].isSelected = true
            selectSTAdapter.notifyItemChanged(currentPosition)
        }
        viewModel.surveillanceTypePosition = currentPosition
        viewModel.assetType = data[currentPosition].type
        when (flowType) {
            AppDefines.HNW_HOUSEHOLD_SURVEILLANCE -> {
                fragmentBinding.tvNoData.gone()
                fragmentBinding.progressBar.visible()
                houseHoldData = mutableListOf()
                householdAdapter.setData(houseHoldData)
                householdAdapter.notifyDataSetChanged()
                fragmentBinding.grpHouseholdSurveillance.visibility = View.VISIBLE
                fragmentBinding.grpVillageSurveillance.visibility = View.GONE
                fragmentBinding.progressBar.visible()
                if (data[currentPosition].type == AppDefines.LARVA_TEST) {
                    fragmentBinding.tvSelectHousehold.text = "Select a Household for Larva Test"
                    viewModel.getHouseHoldList(larvaViewModel.villageUuid, AppDefines.LARVA_TEST)
                }
                if (data[currentPosition].type == AppDefines.IODINE_TEST) {
                    fragmentBinding.tvSelectHousehold.text = "Select a Household for Iodine Test"
                    viewModel.getHouseHoldList(larvaViewModel.villageUuid, AppDefines.IODINE_TEST)
                }
            }
            AppDefines.HNW_VILLAGE_SURVEILLANCE -> {
                fragmentBinding.grpVillageSurveillance.visibility = View.VISIBLE
                fragmentBinding.grpHouseholdSurveillance.visibility = View.GONE
                fragmentBinding.tvSelectAsset.setTextColor(resources.getColor(R.color.black))
                assetAdapter.lastSelectedIndex = -1
                if (data[currentPosition].type == AppDefines.LARVA_TEST) {
                    viewModel.getAssetData(AppDefines.LARVA_TEST)
                    fragmentBinding.tvSelectAsset.text = "Select an Asset for Larva Surveillance"
                }
                if (data[currentPosition].type == AppDefines.IODINE_TEST) {
                    fragmentBinding.tvSelectAsset.text = "Select an Asset for Iodine Test"
                    viewModel.getAssetData(AppDefines.IODINE_TEST)
                }
                if (data[currentPosition].type == AppDefines.WATER_SAMPLE_TEST) {
                    fragmentBinding.tvSelectAsset.text =
                        "Select an Asset for Water Sample Collection"
                    viewModel.getAssetData(AppDefines.WATER_SAMPLE_TEST)
                }
                assetAdapter.setData(viewModel.assetData)
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initAdapter()
        initObserver()
        initClick()
    }

    private fun initClick() {

        fragmentBinding.btnContinue.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(AppDefines.FLOW_TYPE, surveillanceType)
            bundle.putString(AppDefines.ASSET_TYPE,assetName)
            findNavController().navigate(R.id.selectPlaceFragment, bundle)
        }
        fragmentBinding.backButton.setOnClickListener {
            activity?.finish()
        }
        fragmentBinding.showCompleteList.setOnClickListener {
            fragmentBinding.tvNoData.visibility = View.GONE
            fragmentBinding.rvHouseholdList.visible()
            viewModel.houseHoldList.value?.let { it1 -> householdAdapter.setData(it1) }
            fragmentBinding.progressBar.gone()
            householdAdapter.notifyDataSetChanged()
        }
        fragmentBinding.findByLocation.setOnClickListener {
            householdAdapter.setData(mutableListOf())
            householdAdapter.notifyDataSetChanged()
            fragmentBinding.tvNoData.gone()
            fragmentBinding.progressBar.visible()
            currentLocation?.let {
                viewModel.getHouseHoldByLocation(
                    it.latitude, it.longitude, larvaViewModel.villageUuid, surveillanceType
                )
            }


        }
        fragmentBinding.searchIcon.setOnClickListener {
            if (searchName.isNotEmpty()) {
                fragmentBinding.tvNoData.gone()
                fragmentBinding.progressBar.visible()
                householdAdapter.setData(mutableListOf())
                householdAdapter.notifyDataSetChanged()
                viewModel.getHouseholdByName(
                    larvaViewModel.villageLgdCode, searchName, surveillanceType
                )
            }
        }
        fragmentBinding.searchByName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                searchName = s.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    override fun onAssetClick(
        data: MutableList<Asset>, previousPosition: Int, currentPosition: Int
    ) {
        if (previousPosition != -1) {
            data[previousPosition].selected = false
            data[currentPosition].selected = true
            assetAdapter.notifyItemChanged(previousPosition)
            assetAdapter.notifyItemChanged(currentPosition)
        } else {
            fragmentBinding.btnContinue.isEnabled = true
            fragmentBinding.btnContinue.isClickable = true
            fragmentBinding.btnContinue.background=ContextCompat.getDrawable(this.requireContext(),R.drawable.btn_normal)
            data[currentPosition].selected = true
            assetAdapter.notifyItemChanged(currentPosition)
        }
        assetName = data[currentPosition].title
        if(assetName == "Construction"){
            assetName="ConstructionArea"
        }
        if(assetName == "Others"){
            assetName="OtherPlaces"
        }
        viewModel.assetType = assetName
    }


    private fun getImageUrl() {

        while (houseHoldData[currentIndex].imageUrl?.isEmpty() == true) {
            houseHoldData[currentIndex].imageUrl = emptyList()
            currentIndex += 1
            if (currentIndex >= houseHoldData.size) {
                break
            }
        }
        if (currentIndex < houseHoldData.size) {
            var checkUrlData = false
            if (houseHoldData[currentIndex].imageUrl?.isNotEmpty() == true) {
                for (url in houseHoldData[currentIndex].imageUrl!!) {
                    if (url.isNotEmpty()) {
                        checkUrlData = true
                        viewModel.getImageUrl(url)
                        break
                    }
                }
                if (!checkUrlData) {
                    viewModel._placeDetails.postValue("")
                }
            } else {
                viewModel._placeDetails.postValue("")
            }
        } else {
            fragmentBinding.progressBar.gone()
            householdAdapter.setData(houseHoldData)
            householdAdapter.notifyDataSetChanged()
        }
    }


    private fun initObserver() {

        viewModel._placeDetails.observe(viewLifecycleOwner) {
            if (currentIndex < houseHoldData.size) {
                var imageUrl: MutableList<String> = mutableListOf()
                if (houseHoldData[currentIndex].imageUrl?.isNotEmpty() == true) {
                    imageUrl = houseHoldData[currentIndex].imageUrl as MutableList<String>
                    imageUrl[0] = it
                } else {
                    imageUrl.add(0, "")
                }
                currentIndex += 1
                if (currentIndex < houseHoldData.size) {
                    var checkUrlData = false
                    if (houseHoldData[currentIndex].imageUrl?.isNotEmpty() == true) {
                        for (url in houseHoldData[currentIndex].imageUrl!!) {
                            if (url.isNotEmpty()) {
                                checkUrlData = true
                                viewModel.getImageUrl(url)
                                break
                            }
                        }
                        if (!checkUrlData) {
                            viewModel._placeDetails.postValue("")
                        }
                    } else {
                        viewModel._placeDetails.postValue("")
                    }
                } else {
                    fragmentBinding.progressBar.gone()
                    fragmentBinding.grpVillageSurveillance.visibility = View.GONE
                    fragmentBinding.grpHouseholdSurveillance.visibility = View.VISIBLE
                    val houseHoldListSize=houseHoldData.size
                    var itemsToBeDisplayed=0
                    itemsToBeDisplayed = if(houseHoldListSize>20){
                        20
                    } else{
                        houseHoldData.size
                    }
                    householdAdapter.setData(houseHoldData.subList(0,itemsToBeDisplayed))
                    householdAdapter.notifyDataSetChanged()
                }

            }
        }

        viewModel.houseHoldList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            houseHoldData = it
            if (it.size > 0) {
                currentIndex=0
                getImageUrl()
            } else {
                fragmentBinding.tvNoData.visibility = View.VISIBLE
                fragmentBinding.rvHouseholdList.gone()
            }

        })

        viewModel.houseHoldListByName.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            fragmentBinding.progressBar.gone()
            if (it.size == 0) {
                fragmentBinding.tvNoData.visibility = View.VISIBLE
                fragmentBinding.rvHouseholdList.gone()
            } else {
                fragmentBinding.tvNoData.visibility = View.GONE
                fragmentBinding.rvHouseholdList.visible()
                householdAdapter.setData(it)
                householdAdapter.notifyDataSetChanged()
            }
        })

        viewModel.houseHoldListByLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            fragmentBinding.progressBar.gone()
            if (it.size == 0) {
                fragmentBinding.tvNoData.visibility = View.VISIBLE
                fragmentBinding.rvHouseholdList.gone()
            } else {
                fragmentBinding.rvHouseholdList.visible()
                fragmentBinding.tvNoData.visibility = View.GONE
                householdAdapter.setData(it)
                householdAdapter.notifyDataSetChanged()
            }
        })

    }


    @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
    override fun onMapReady(googleMap: GoogleMap) {
        if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
            map = googleMap
            fragmentBinding.cardMap.visible()
            fragmentBinding.ivSt.background =
                context?.resources?.getDrawable(R.drawable.ic_household_asset)
            fragmentBinding.tvLocationTitle.text =
                "You are in ${larvaViewModel.villageName} Village for HouseHold Level Surveillance"
            fetchLocation()
        }
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
                    location.getLatitude(), location.getLongitude()
                )
            ) // Sets the center of the map to location user
                .zoom(30f) // Sets the zoom
                .bearing(90f) // Sets the orientation of the camera to east
                .tilt(40f) // Sets the tilt of the camera to 30 degrees
                .build() // Creates a CameraPosition from the builder
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            map?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        location.getLatitude(), location.getLongitude()
                    ), 30f
                )
            )
        }
        val currentLatLng = LatLng(location.getLatitude(), location.getLongitude())
        try {
            val currentLocationBitmap = getBitmap(R.drawable.map_pin_your_location)
            currentLocationMarker = map?.addMarker(
                MarkerOptions().position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(currentLocationBitmap))
                    .title("Current Location").zIndex(1f)
            )
        } catch (e: Exception) {
            Timber.tag("some error").e("error occurred while getting bitmap")
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        currentIndex=0
        if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
            try {
                fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            } catch (ignore: Exception) {
                Analytics.trackEvent(AnalyticsEvents.LOCATION_LISTENER_NULL)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
            try {
                fusedLocationProviderClient?.removeLocationUpdates(locationCallback)
            } catch (ignore: Exception) {
                Analytics.trackEvent(AnalyticsEvents.LOCATION_LISTENER_NULL)
            }
            DisplayAlert.dismiss()
        }
    }


    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }



    private fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onHouseHoldItemClick(data: HouseHoldDataItem) {
        val bundle = Bundle()
        bundle.putString(
            AppDefines.HOUSEHOLD_DETAILS, Gson().toJson(data)
        )

        if (data.action == AppDefines.LARVA_ACTION) {
            bundle.putString(
                AppDefines.FLOW_TYPE, surveillanceType
            )
            findNavController().navigate(R.id.performLarvaSurveillance, bundle)
        }
        if (data.action == AppDefines.IODINE_ACTION) {
            bundle.putString(
                AppDefines.FLOW_TYPE, surveillanceType
            )
            findNavController().navigate(R.id.updateIodineTestFragment, bundle)
        }
    }


}