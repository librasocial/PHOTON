package com.ssf.homevisit.rmncha.childCare.mapping

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.compose.ui.text.toLowerCase
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.AllChildCareChildrenAdapter
import com.ssf.homevisit.adapters.AllChildCareHouseHoldAdapter
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.FragmentFirstBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.models.*
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import com.ssf.homevisit.rmncha.childCare.immunization.ChildImmunizationActivity
import com.ssf.homevisit.rmncha.childCare.registration.ChildCareRegistrationActivity
import com.ssf.homevisit.rmncha.pnc.mapping.PNCMappingFragment

class ChildMappingListFragment : Fragment(), OnMapReadyCallback {

    private val MAP_PERMISSION_CODE = 355;
    private lateinit var currentLocation: Location
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var _binding: FragmentFirstBinding
    private val viewModel: ChildCareViewModel by viewModels()
    private var map: GoogleMap? = null
    private var currentLocationMarker: Marker? = null
    private lateinit var selectedPhc: PhcResponse.Content
    private lateinit var selectedSubCenter: SubcentersFromPHCResponse.Content
    private lateinit var selectedVillage: SubCVillResponse.Content
    private lateinit var allHouseholdAdapter: AllChildCareHouseHoldAdapter
    private lateinit var childrenAdapter: AllChildCareChildrenAdapter
    private var childrenList: List<CcChildListContent> = listOf()
    private var houseHoldList: List<TargetNodeCCHouseHoldProperties> = listOf()
    private var addedMarkers = false
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.apply {
            selectedPhc.value = this@ChildMappingListFragment.selectedPhc
            selectedSubCenter.value = this@ChildMappingListFragment.selectedSubCenter
            selectedVillage.value = this@ChildMappingListFragment.selectedVillage
        }
        _binding = FragmentFirstBinding.inflate(inflater, container, false).apply {
            viewModel = this@ChildMappingListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialUI()
        loadMap()
        initAdapter()
        setData()
        initObserver()
    }

    override fun onResume() {
        super.onResume()
        binding.childCareRecycler.adapter = allHouseholdAdapter
        viewModel.getChildCareCompleteChildrenList()
    }
    fun initAdapter(){
        childrenAdapter = AllChildCareChildrenAdapter {
        it.sourceNode.properties.rmnchaServiceStatus.let { serviceStatus ->
            if (serviceStatus == RMNCHAServiceStatus.PNC_ONGOING.name) {
                val childDetails= ChildInHouseContent(
                        relationship = ChildInHouseRelationship(
                                id = it.relationship.id,
                                properties = ChildInHouseProperties(
                                        type = it.relationship.type
                                ),
                                type = it.relationship.type
                        ),
                        targetNode = ChildInHouseTargetNode(
                                id = it.targetNode.id,
                                properties = null,
                                labels = it.targetNode.labels
                        ),
                        sourceNode = ChildInHouseSourceNode(
                                id = it.sourceNode.id,
                                properties = ChildInHousePropertiesX(
                                        age = it.sourceNode.properties.age,
                                        createdBy = it.sourceNode.properties.createdBy,
                                        dateCreated = it.sourceNode.properties.dateCreated,
                                        dateModified = it.sourceNode.properties.dateModified,
                                        dateOfBirth = it.sourceNode.properties.dateOfBirth,
                                        gender = it.sourceNode.properties.gender,
                                        isAdult = it.sourceNode.properties.isAdult,
                                        modifiedBy = it.sourceNode.properties.modifiedBy,
                                        rmnchaServiceStatus = it.sourceNode.properties.rmnchaServiceStatus,
                                        type = it.sourceNode.properties.type,
                                        uuid = it.sourceNode.properties.uuid,
                                        firstName = it.sourceNode.properties.firstName,
                                        healthID = it.sourceNode.properties.healthID,
                                        imageUrls = it.sourceNode.properties.imageUrls
                                ),
                                labels = it.sourceNode.labels
                        ))
                val intent = ChildCareRegistrationActivity.getNewIntent(
                        requireContext(),
                        selectedVillage,
                        selectedSubCenter,
                        selectedPhc,
                        childDetails = childDetails
                )
                startActivity(intent)
            }
            if(serviceStatus ==RMNCHAServiceStatus.CHILDCARE_REGISTERED.name || serviceStatus== RMNCHAServiceStatus.CHILDCARE_ONGOING.name){
                val intent = ChildImmunizationActivity.getNewIntent(
                        requireContext(),
                        selectedVillage,
                        selectedSubCenter,
                        selectedPhc,
                        it
                )
                startActivity(intent)
            }
        }
    }
        childrenAdapter.context=this.requireContext()
        binding.childCareRecycler.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.childCareRecycler.adapter = childrenAdapter
        allHouseholdAdapter = AllChildCareHouseHoldAdapter {
            val childActivityIntent = ChildCareActivity.getNewIntent(
                    requireContext(),
                    selectedVillage,
                    selectedSubCenter,
                    selectedPhc,
                    it
            )
            startActivity(childActivityIntent)
        }
        allHouseholdAdapter.context=this.requireContext()
        binding.childCareRecycler.layoutManager =
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.childCareRecycler.adapter = allHouseholdAdapter
    }
    fun initObserver(){
        viewModel.registeredChildrenLiveData.observe(viewLifecycleOwner) {
            viewModel.setLoading(false)
            it?.let {
                childrenList = it.content
                if(childrenList.isEmpty()){
                    binding.emptyState.root.visible()
                }
                else{
                    binding.emptyState.root.gone()
                    allHouseholdAdapter.submitList(emptyList())
                    childrenAdapter.submitList(childrenList)
                }
            }

        }
        viewModel.houseHoldChildrenLiveData.observe(viewLifecycleOwner) {
            viewModel.setLoading(false)
            it?.let {
                houseHoldList = it.content.map { it.targetNode.properties }
                if(houseHoldList.isEmpty()){
                    binding.emptyState.root.visible()
                }
                else{
                    binding.emptyState.root.gone()
                    childrenAdapter.submitList(emptyList())
                    allHouseholdAdapter.submitList(houseHoldList)

                }
                try {
                    setMapMarkers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setData() {
        binding.tvHHList.setOnClickListener {
            binding.searchBar.setText("")
            binding.emptyState.root.gone()
            viewModel.listModeAll.value=true
            binding.childCareRecycler.adapter = allHouseholdAdapter
            viewModel.getChildCareCompleteChildrenList()
        }
        binding.regChildren.setOnClickListener {
            binding.searchBar.setText("")
            binding.emptyState.root.gone()
            viewModel.listModeAll.value=false
            binding.childCareRecycler.adapter = childrenAdapter
            viewModel.getRegisteredChildrenList()
        }

        binding.searchBar.doAfterTextChanged { query ->
            if (viewModel.listModeAll.value == true) {
                binding.childCareRecycler.adapter = allHouseholdAdapter
                val list = if (!query.isNullOrEmpty()) {
                    houseHoldList.filter {
                        it.houseHeadName.contains(
                            query.toString(),
                            true
                        )
                    }
                } else {
                    houseHoldList
                }
                binding.emptyState.root.isVisible = list.isEmpty()
                allHouseholdAdapter.submitList(list)
            } else {
                binding.childCareRecycler.adapter = childrenAdapter
                val list = if (!query.isNullOrEmpty()) {
                    childrenList.filter {
                        it.sourceNode.properties.firstName?.contains(
                            query.toString(),
                            true
                        ) ?: false
                    }
                } else {
                    childrenList
                }
                binding.emptyState.root.isVisible = list.isEmpty()
                childrenAdapter.submitList(list)
            }
        }
        binding.tvBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setMapMarkers() {
        if (addedMarkers) {
            return
        }
        addedMarkers = true
        for (item in houseHoldList) {
            if (item.latitude > 0 && item.longitude > 0) {
                val latLng = LatLng(item.latitude, item.longitude)
                map?.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(item.houseID)
                        .icon(
                            BitmapDescriptorFactory
                                .fromBitmap(
                                    Bitmap.createScaledBitmap(
                                        getBitmap(getHousePinAsset())!!,
                                        30,
                                        30,
                                        false
                                    )
                                )
                        )
                )
            }

        }
    }

    private fun setupInitialUI() {
        binding.toobar.apply {
            path = "RMNCH+A > Child Care > "
            destination = "Selection of Households"
        }
    }


    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                AppController.getInstance().applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                AppController.getInstance().mainActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MAP_PERMISSION_CODE
            )
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
                            if (location.accuracy <= currentLocation.accuracy) setCurrentLocation(location)
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

    private fun getBitmap(drawableRes: Int): Bitmap? {
        val drawable = resources.getDrawable(drawableRes)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)
        return bitmap
    }

    private fun getHousePinAsset(): Int {
        return R.drawable.housemap
    }

    private fun loadMap() {
        val supportMapFragment = (childFragmentManager.findFragmentById(R.id.childCareMap) as SupportMapFragment?)
        supportMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        map = p0
        fetchLocation()
    }


    companion object {
        fun getInstance(
            phc: PhcResponse.Content,
            subCenter: SubcentersFromPHCResponse.Content,
            village: SubCVillResponse.Content
        ) = ChildMappingListFragment().apply {
            selectedPhc = phc
            selectedSubCenter = subCenter
            selectedVillage = village
        }
    }
}