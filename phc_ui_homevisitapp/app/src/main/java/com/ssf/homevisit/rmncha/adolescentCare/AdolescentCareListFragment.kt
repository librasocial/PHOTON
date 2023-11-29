package com.ssf.homevisit.rmncha.adolescentCare

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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.AllAdolescentCareChildrenAdapter
import com.ssf.homevisit.adapters.AllChildCareHouseHoldAdapter
import com.ssf.homevisit.databinding.FragmentAdolescentCareListBinding
import com.ssf.homevisit.models.*

class AdolescentCareListFragment : Fragment(), OnMapReadyCallback {
    private val MAP_PERMISSION_CODE = 355;
    private lateinit var currentLocation: Location
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private lateinit var _binding: FragmentAdolescentCareListBinding
    private val viewModel: AdolescentCareViewModel by viewModels()
    private var map: GoogleMap? = null
    private var currentLocationMarker: Marker? = null
    private lateinit var selectedPhc: PhcResponse.Content
    private lateinit var selectedSubCenter: SubcentersFromPHCResponse.Content
    private lateinit var selectedVillage: SubCVillResponse.Content
    private lateinit var allHouseholdAdapter: AllChildCareHouseHoldAdapter
    private lateinit var childrenAdapter: AllAdolescentCareChildrenAdapter
    private var childrenList: List<CcChildListContent> = listOf()
    private var houseHoldList: List<TargetNodeCCHouseHoldProperties> = listOf()
    private var addedMarkers = false
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.apply {
            selectedPhc.value = this@AdolescentCareListFragment.selectedPhc
            selectedSubCenter.value = this@AdolescentCareListFragment.selectedSubCenter
            selectedVillage.value = this@AdolescentCareListFragment.selectedVillage
        }
        _binding = FragmentAdolescentCareListBinding.inflate(inflater, container, false).apply {
            viewModel = this@AdolescentCareListFragment.viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupInitialUI()
        setData()
        loadMap()
    }

    private fun setData() {
        binding.tvHHList.setOnClickListener {
            binding.searchBar.setText("")
            viewModel.listModeAll.value = true
        }
        binding.regChildren.setOnClickListener {
            binding.searchBar.setText("")
            viewModel.listModeAll.value = false
        }
        viewModel.listModeAll.observe(viewLifecycleOwner) {
            if (it) {
                setAllHHList()
            } else {
                setChildList()
            }
        }

        binding.searchBar.doAfterTextChanged { query ->
            if (viewModel.listModeAll.value == true) {
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
    }

    private fun setChildList() {
        childrenAdapter = AllAdolescentCareChildrenAdapter {
            val adolescentCareService = AdolescentCareServiceActivity.getNewIntent(
                requireContext(),
                selectedVillage,
                selectedSubCenter,
                selectedPhc,
                it
            )
            startActivity(adolescentCareService)
        }
        childrenAdapter.context=this.requireContext()
        binding.childCareRecycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.childCareRecycler.adapter = childrenAdapter
        viewModel.allChildren.observe(viewLifecycleOwner) {
            viewModel.setLoading(false)
            it?.let {
                childrenList = it.content
                binding.emptyState.root.isVisible = childrenList.isEmpty()
                childrenAdapter.submitList(childrenList)
            }
        }
    }

    private fun setAllHHList() {
        allHouseholdAdapter = AllChildCareHouseHoldAdapter {
            viewModel.selectedHousehold.value = it
            val childActivityIntent = AdolescentCareRegistrationActivity.getNewIntent(
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
        viewModel.allHHList.observe(viewLifecycleOwner) {
            viewModel.setLoading(false)
            it?.let {
                houseHoldList = it.content.map { it.targetNode.properties }
                binding.emptyState.root.isVisible = houseHoldList.isEmpty()
                allHouseholdAdapter.submitList(houseHoldList)
                try {
                    setMapMarkers()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
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
            path = "RMNCH+A > Adolescent Care > "
            destination = "Selection of Households"
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MAP_PERMISSION_CODE
            )
            return
        }
        val locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers = locationManager.getProviders(true)
        var location: Location? = null
        for (provider in providers) {
            val l = locationManager.getLastKnownLocation(provider) ?: return
            if (location == null || l.accuracy < location.accuracy) {
                location = l
            }
        }
        location?.let { setCurrentLocation(it) }
        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener { llocation: Location? ->
            llocation?.let {
                if (::currentLocation.isInitialized.not() || llocation.accuracy < currentLocation.accuracy) {
                    setCurrentLocation(llocation)
                }
            }
        }
        fusedLocationProviderClient?.requestLocationUpdates(
            LocationRequest().setInterval(2000).setMaxWaitTime(2000),
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (loc in locationResult.locations) {
                        if (::currentLocation.isInitialized.not() || loc.accuracy <= currentLocation.accuracy) {
                            setCurrentLocation(loc)
                        }
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun setCurrentLocation(location: Location) {
        var alreadyZoomed = false
        if (currentLocationMarker != null) {
            currentLocationMarker?.remove()
            alreadyZoomed = true
        }
        currentLocation = location
        if (!alreadyZoomed) {
            val cameraPosition = CameraPosition.Builder()
                .target(
                    LatLng(
                        location.latitude,
                        location.longitude
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
                        location.latitude,
                        location.longitude
                    ), 30f
                )
            )
        }
        val currentLatLng = LatLng(location.latitude, location.longitude)
        try {
            val currentLocationBitmap = getMarkerBitmapFromView(R.drawable.map_pin_your_location)
            currentLocationMarker = map?.addMarker(
                MarkerOptions().position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(currentLocationBitmap))
                    .title("Current Location")
                    .zIndex(1f)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    private fun getMarkerBitmapFromView(@DrawableRes resId: Int): Bitmap {
        val customMarkerView: View =
            LayoutInflater.from(requireContext()).inflate(R.layout.layout_custom_marker, null)
        val markerImageView = customMarkerView.findViewById<View>(R.id.profile_image) as ImageView
        // markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        customMarkerView.layout(
            0,
            0,
            customMarkerView.measuredWidth,
            customMarkerView.measuredHeight
        )
        customMarkerView.buildDrawingCache()
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background
        drawable?.draw(canvas)
        customMarkerView.draw(canvas)
        return returnedBitmap
    }

    private fun getHousePinAsset(): Int {
        return R.drawable.housemap
    }

    private fun loadMap() {
        val supportMapFragment =
            (childFragmentManager.findFragmentById(R.id.childCareMap) as SupportMapFragment?)!!
        supportMapFragment.getMapAsync(this)
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
        ) = AdolescentCareListFragment().apply {
            selectedPhc = phc
            selectedSubCenter = subCenter
            selectedVillage = village
        }
    }
}