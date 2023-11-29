package com.ssf.homevisit.rmncha.pnc.mapping

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ssf.homevisit.R
import com.ssf.homevisit.activity.MainActivity
import com.ssf.homevisit.alerts.HouseholdAlert
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.LayoutRmnchaPncMappingFragmentBinding
import com.ssf.homevisit.models.*
import com.ssf.homevisit.rmncha.base.RMNCHAConstants
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import com.ssf.homevisit.rmncha.pnc.details.PNCDetailsActivity
import com.ssf.homevisit.rmncha.pnc.mapping.PNCMappingAdapter.PNCMappingItemListener
import com.ssf.homevisit.rmncha.pnc.selectwomen.SelectWomenForPNCActivity
import com.ssf.homevisit.rmncha.pnc.service.PNCServiceActivity

class PNCMappingFragment : Fragment(), OnMapReadyCallback, PNCMappingItemListener {

    var currentLocation: Location? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var binding: LayoutRmnchaPncMappingFragmentBinding
    private var map: GoogleMap? = null
    private var currentLocationMarker: Marker? = null
    private lateinit var viewModel: PNCMappingFragmentViewModel
    private var mRMNCHAHouseHoldAdapter: PNCMappingAdapter? = null
    var villageProperties: VillageProperties? = null
    var villageName = ""
    var rmnchaSelectedPhc: PhcResponse.Content? = null
    var rmnchaSelectedSubCenter: SubcentersFromPHCResponse.Content? = null
    var rmnchaSelectedVillage: SubCVillResponse.Content? = null
    var houseHoldsList: List<RMNCHAPNCHouseholdsResponse.Content>? = null
    private var womenList: List<RMNCHAPNCWomenResponse.Content>? = null
    var allFlag = true
    fun setPhc(phcName: PhcResponse.Content?) {
        rmnchaSelectedPhc = phcName
    }

    fun setSubCenter(subCenterName: SubcentersFromPHCResponse.Content?) {
        rmnchaSelectedSubCenter = subCenterName
    }

    fun setVillage(village: SubCVillResponse.Content) {
        rmnchaSelectedVillage = village
        villageProperties = village.target.villageProperties
        if (villageProperties != null) villageName = villageProperties?.name.toString()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_rmncha_pnc_mapping_fragment, container, false)
        viewModel = PNCMappingFragmentViewModel(this.requireActivity().application)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.backButton.setOnClickListener { view: View? -> requireActivity().onBackPressed() }
        initialization()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity?)?.setActionBarVisibility(View.VISIBLE)
        if (binding.countHeader.visibility == View.VISIBLE) {
            pNCHouseHolds
        } else {
            pNCWomen
        }
    }

    private fun initialization() {
        binding.youAreInVillageMessage.text = resources.getString(R.string.hh_pnc_you_are_in_village_message).replace("@@", villageName)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AppController.getInstance().applicationContext)
        binding.footerTv.text = Html.fromHtml(context?.getString(R.string.powered_by_))
        loadMap()
        binding.showCompleteList.setOnClickListener { view: View? ->
            pNCHouseHolds
            binding.headerTitle.text = resources.getString(R.string.hh_select_pnc_hh_message)
        }
        binding.showListOfWomen.setOnClickListener { view: View? ->
            binding.headerTitle.text = resources.getString(R.string.hh_select_pnc_women_message)
            pNCWomen
        }
        binding.searchByHeadName.setOnClickListener { view: View? ->
            val query = binding.searchByName.text.toString() + ""
            RMNCHAUtils.hideSoftInputKeyboard(context, view)
            if (query.length > 2) {
                searchPNCHouseHold(query)
                //                filterByName(query);
            } else {
                pNCHouseHolds
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        fetchLocation()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    private val pNCHouseHolds: Unit
        get() {
            showProgressbar()
            viewModel.getPNCHouseholdResponseLiveData(villageProperties?.uuid).observe(viewLifecycleOwner) { rmnchapncHouseholdsResponse: RMNCHAPNCHouseholdsResponse? ->
                hideProgressbar()
                if (rmnchapncHouseholdsResponse != null) {
                    houseHoldsList = rmnchapncHouseholdsResponse.content
                    setAllHouseHoldsAdapter()
                    for (content in houseHoldsList as MutableList<RMNCHAPNCHouseholdsResponse.Content>) {
                        try {
                            val houseHoldProperties = content.source.properties
                            val longitude = houseHoldProperties.longitude
                            val latitude = houseHoldProperties.latitude
                            if ((latitude.toString() + "").length > 0 && (longitude.toString() + "").length > 0) {
                                val latLng = LatLng(latitude, longitude)
                                map?.addMarker(
                                        MarkerOptions()
                                                .position(latLng)
                                                .title(houseHoldProperties.houseID)
                                                .icon(BitmapDescriptorFactory
                                                        .fromBitmap(Bitmap.createScaledBitmap(getBitmap(housePinAsset), 50, 50, false)))
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("Error ", "Invalid long, lat " + e.message)
                        }
                    }
                } else {
                    RMNCHAUtils.showMyToast(activity, "Error fetching HH data  : " + viewModel?.errorMessage)
                }
            }
        }

    private fun searchPNCHouseHold(query: String) {
        showProgressbar()
        viewModel.getHouseHoldsByHeadName(query, villageProperties?.uuid).observe(viewLifecycleOwner) { rmnchapncHouseholdsResponse: RMNCHAHouseHoldSearchResponse? ->
            hideProgressbar()
            if (rmnchapncHouseholdsResponse != null) {
                houseHoldsList = ObjectMapper().convertValue(rmnchapncHouseholdsResponse, RMNCHAPNCHouseholdsResponse::class.java).content
                setAllHouseHoldsAdapter()
                for (content in houseHoldsList as MutableList<RMNCHAPNCHouseholdsResponse.Content>) {
                    try {
                        val houseHoldProperties = content.source.properties
                        val longitude = houseHoldProperties.longitude
                        val latitude = houseHoldProperties.latitude
                        if ((latitude.toString() + "").isNotEmpty() && (longitude.toString() + "").isNotEmpty()) {
                            val latLng = LatLng(latitude, longitude)
                            map?.addMarker(
                                    MarkerOptions()
                                            .position(latLng)
                                            .title(houseHoldProperties.houseID)
                                            .icon(BitmapDescriptorFactory
                                                    .fromBitmap(Bitmap.createScaledBitmap(getBitmap(housePinAsset), 50, 50, false)))
                            )
                        }
                    } catch (e: Exception) {
                    }
                }
            } else {
                RMNCHAUtils.showMyToast(activity, "Error fetching HH data  : " + viewModel.errorMessage)
            }
        }
    }

    private val pNCWomen: Unit
        get() {
            showProgressbar()
            viewModel.getPNCWomenResponseLiveData(villageProperties?.uuid).observe(viewLifecycleOwner) { rmnchaPNCWomenResponse: RMNCHAPNCWomenResponse? ->
                hideProgressbar()
                if (rmnchaPNCWomenResponse != null) {
                    womenList = rmnchaPNCWomenResponse.content
                    setWomenAdapter()
                    for (content in (womenList as MutableList<RMNCHAPNCWomenResponse.Content>)) {
                        try {
                            val houseHoldProperties = content.source.properties
                            val longitude = houseHoldProperties.longitude
                            val latitude = houseHoldProperties.latitude
                            if ((latitude.toString() + "").isNotEmpty() && (longitude.toString() + "").isNotEmpty()) {
                                val latLng = LatLng(latitude, longitude)
                                map?.addMarker(
                                        MarkerOptions()
                                                .position(latLng)
                                                .title(houseHoldProperties.firstName)
                                                .icon(BitmapDescriptorFactory
                                                        .fromBitmap(Bitmap.createScaledBitmap(getBitmap(housePinAsset), 50, 50, false)))
                                )
                            }
                        } catch (e: Exception) {
                            Log.e("Error ", "Invalid long, lat " + e.message)
                        }
                    }
                } else {
                    RMNCHAUtils.showMyToast(activity, "Error fetching Women's data  : " + viewModel.errorMessage)
                }
            }
        }

    private fun setAllHouseHoldsAdapter() {
        binding.nameHeader.text = "Name of HH Head"
        binding.countHeader.visibility = View.VISIBLE
        binding.showCompleteList.setTextColor(resources.getColor(R.color.rmncha_unselected_color))
        binding.showListOfWomen.setTextColor(resources.getColor(R.color.button_blue))
        binding.pncTitle.text = resources.getString(R.string.pnc_hh_header_message)
        setAdapter(true, houseHoldsList, null)
    }

    private fun setWomenAdapter() {
        binding.nameHeader.text = "Name of the Woman"
        binding.countHeader.visibility = View.GONE
        binding.showCompleteList.setTextColor(resources.getColor(R.color.button_blue))
        binding.showListOfWomen.setTextColor(resources.getColor(R.color.rmncha_unselected_color))
        binding.pncTitle.text = resources.getString(R.string.pnc_women_header_message)
        setAdapter(false, null, womenList)
    }

    private fun setAdapter(flag: Boolean, houseHoldsList: List<RMNCHAPNCHouseholdsResponse.Content>?, womenList: List<RMNCHAPNCWomenResponse.Content>?) {
        allFlag = flag
        mRMNCHAHouseHoldAdapter = PNCMappingAdapter(context, houseHoldsList, womenList, this, flag)
        binding.recyclerView.adapter = mRMNCHAHouseHoldAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun filterByName(name: String?) {
        if (name != null && name.length > 3) {
            if (allFlag) {
                setAdapter(true, filterHHListByName(name), null)
            } else {
                setAdapter(false, null, filterWomenListByName(name))
            }
        } else {
            RMNCHAUtils.showMyToast(activity, "Enter valid name")
        }
    }

    private fun filterWomenListByName(name: String): List<RMNCHAPNCWomenResponse.Content> {
        val filterList: MutableList<RMNCHAPNCWomenResponse.Content> = ArrayList()
        for (item in womenList!!) {
            if (name.contains(item.source.properties.firstName + "")) {
                filterList.add(item)
            }
        }
        return filterList
    }

    private fun filterHHListByName(name: String): List<RMNCHAPNCHouseholdsResponse.Content> {
        val filterList: MutableList<RMNCHAPNCHouseholdsResponse.Content> = ArrayList()
        for (item in houseHoldsList!!) {
            if (name.contains(item.source.properties.houseHeadName + "")) {
                filterList.add(item)
            }
        }
        return filterList
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                        AppController.getInstance().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        AppController.getInstance().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AppController.getInstance().mainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            return
        }
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var location: Location? = null
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider)
            if (l == null) {
                Log.d("provider $provider", "null")
                continue
            }
            Log.d("provider $provider", l.accuracy.toString() + "")
            if (location == null || l.accuracy < location.accuracy) {
                // Found best last known location: %s", l);
                location = l
            }
        }
        location?.let { setCurrentLocation(it) }
        val task = fusedLocationProviderClient?.lastLocation
        task?.addOnSuccessListener { llocation: Location? ->
            if (llocation != null) {
                Log.d("provider " + llocation.provider, llocation.accuracy.toString() + "")
                if (currentLocation == null || llocation.accuracy < currentLocation!!.accuracy) {
                    setCurrentLocation(llocation)
                }
            } else {
                Log.d("provider fused ", "Null")
            }
        }
        task?.addOnFailureListener { rr: Exception -> Log.e("loc", rr.toString()) }
        fusedLocationProviderClient?.requestLocationUpdates(LocationRequest().setInterval(2000).setMaxWaitTime(2000),
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        for (location in locationResult.locations) {
                            if (location != null) {
                                if (currentLocation == null || location.accuracy <= currentLocation?.accuracy!!) setCurrentLocation(location)
                            }
                        }
                    }

                    override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                        super.onLocationAvailability(locationAvailability)
                    }
                },
                Looper.getMainLooper())
    }

    @JvmName("setCurrentLocation1")
    private fun setCurrentLocation(location: Location) {
        var alreadyZoomed = false
        if (currentLocationMarker != null) {
            currentLocationMarker!!.remove()
            alreadyZoomed = true
        }
        currentLocation = location
        if (!alreadyZoomed) {
            val cameraPosition = CameraPosition.Builder()
                    .target(LatLng(location.latitude, location.longitude)) // Sets the center of the map to location user
                    .zoom(30f) // Sets the zoom
                    .bearing(90f) // Sets the orientation of the camera to east
                    .tilt(40f) // Sets the tilt of the camera to 30 degrees
                    .build() // Creates a CameraPosition from the builder
            map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 30f))
        }
        val currentLatLng = LatLng(location.latitude, location.longitude)
        try {
            val currentLocationBitmap = markerBitmapFromView
            currentLocationMarker = map?.addMarker(MarkerOptions().position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(currentLocationBitmap))
                    .title("Current Location")
                    .zIndex(1f))
        } catch (e: Exception) {
            Log.e("some error", "error occurred while getting bitmap")
            e.printStackTrace()
        }
    }

    private fun getBitmap(drawableRes: Int): Bitmap {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }
    private val markerBitmapFromView: Bitmap
        get() {
            val customMarkerView = (requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(R.layout.layout_custom_marker, null)
            val layout = customMarkerView.findViewById<View>(R.id.background) as LinearLayout
            layout.background = resources.getDrawable(R.drawable.ic_marker_green)
            customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            customMarkerView.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight)
            customMarkerView.buildDrawingCache()
            val returnedBitmap = Bitmap.createBitmap(customMarkerView.measuredWidth, customMarkerView.measuredHeight,
                    Bitmap.Config.ARGB_8888)
            val canvas = Canvas(returnedBitmap)
            canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
            val drawable = customMarkerView.background
            drawable?.draw(canvas)
            customMarkerView.draw(canvas)
            return returnedBitmap
        }
    private val housePinAsset: Int
        private get() = R.drawable.housemap

    private fun loadMap() {
        val supportMapFragment = (childFragmentManager.findFragmentById(R.id.rmncha_map) as SupportMapFragment?)
        supportMapFragment?.getMapAsync(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode >= HouseholdAlert.cameraResultStart && requestCode <= HouseholdAlert.cameraResultEnd) {
            HouseholdAlert.getInstance(context).onImageCaptured(requestCode - HouseholdAlert.cameraResultStart)
        }
    }

    override fun goToSelectWomenForPNC(content: RMNCHAPNCHouseholdsResponse.Content) {
        val intent = Intent(context, SelectWomenForPNCActivity::class.java)
        intent.putExtra(SelectWomenForPNCActivity.PARAM_1, content.source.properties.uuid)
        intent.putExtra(SelectWomenForPNCActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter?.target?.properties?.uuid)
        requireActivity().startActivity(intent)
    }

    override fun goTopPNCService(content: RMNCHAPNCWomenResponse.Content) {
        val intent = Intent(context, PNCServiceActivity::class.java)
        val bundle = Bundle()
        bundle.putString(PNCServiceActivity.PARAM_1, content.target.properties.uuid)
        bundle.putString(PNCServiceActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter?.target?.properties?.uuid)
        bundle.putSerializable(PNCDetailsActivity.SELECTED_WOMEN_KEY, content)
        intent.putExtras(bundle)
        startActivityForResult(intent, RMNCHAConstants.PNC_REQUEST_CODE)
    }

    override fun goToPNCRegistration(content: RMNCHAPNCWomenResponse.Content) {
        val intent = Intent(context, PNCDetailsActivity::class.java)
        val bundle = Bundle()
        bundle.putString(PNCDetailsActivity.PARAM_1, content.target.properties.uuid)
        bundle.putString(PNCDetailsActivity.PARAM_SUB_CENTER, rmnchaSelectedSubCenter?.target?.properties?.uuid)
        bundle.putSerializable(PNCDetailsActivity.SELECTED_WOMEN_KEY, content)
        intent.putExtras(bundle)
        startActivityForResult(intent, RMNCHAConstants.PNC_REQUEST_CODE)
    }

    private fun showProgressbar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        binding.progressBar.visibility = View.GONE
    }

    companion object {
        private const val REQUEST_CODE = 101
    }
}