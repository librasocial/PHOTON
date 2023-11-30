package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import com.ssf.homevisit.databinding.IndividualMappingBinding
import com.ssf.homevisit.extensions.customAlert
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.ChildrenRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.ChildrenSourceProperties
import com.ssf.homevisit.healthWellnessSurveillance.data.ChildrenTargetProperties
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualMappingViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter.HouseHoldAdapter
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.AnalyticsEvents
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException


@AndroidEntryPoint
class IndividualMappingFragment : Fragment(), OnMapReadyCallback,
    HouseHoldAdapter.OnHouseHoldItemClick {
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE = 101
    private var map: GoogleMap? = null
    private lateinit var locationCallback: LocationCallback
    private var currentLocation: Location? = null
    private var currentLocationMarker: Marker? = null
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val viewModel: IndividualMappingViewModel by activityViewModels()
    var houseHoldData: MutableList<HouseHoldDataItem> = mutableListOf()
    private lateinit var binding: IndividualMappingBinding
    private lateinit var householdAdapter: HouseHoldAdapter
    var currentIndex = 0
    private var searchName: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initArguments()
        initListener()
        initAdapter()
        initialization()
        setData()
        getData()
    }

    private fun getData() {
        binding.progressBar.visible()
        if (viewModel.surveillanceType == AppDefines.URINE_SAMPLE) {
            binding.tvHouseholdNumber.text = "Child’s Name"
            binding.tvHouseholdName.text = "Age"
            binding.tvNumberMembers.text = "Name of the HH Head"
            viewModel.getChildrenInVillage(
                larvaViewModel.villageUuid, requestData = ChildrenRequestData(
                    relationshipType = "RESIDESIN",
                    sourceType = "Citizen",
                    sourceProperties = ChildrenSourceProperties(isAdult = false),
                    targetProperties = ChildrenTargetProperties(uuid = larvaViewModel.villageUuid),
                    stepCount = 2,
                    targetType = "Village"
                )
            )
        } else {
            viewModel.getHouseHoldList(larvaViewModel.villageUuid, viewModel.surveillanceType)
        }
    }

    fun setData() {
        binding.topHeader.dropdownMenu.gone()
        binding.topHeader.separatorView1.gone()
        binding.tvLocationTitle.text =
            "You are in ${larvaViewModel.villageName} Village for ${viewModel.surveillanceType}"
        when (viewModel.surveillanceType) {
            AppDefines.IDSP_S_Form -> {
                binding.tvSelectHousehold.text = "Select a Household to Fill IDSP S-Form"
                binding.tvNilReport.visible()
            }
            AppDefines.LEPROSY -> {
                binding.tvSelectHousehold.text = "Select a Household for Leprosy Surveillance"
            }
            AppDefines.COVID -> {
                binding.tvSelectHousehold.text = "Select a Household for COVID Surveillance"
            }
            AppDefines.BLOOD_SMEAR -> {
                binding.tvSelectHousehold.text = "Select a Household for Blood Smear Testing"
            }
            AppDefines.ACTIVE_CASE_FINDING -> {
                binding.tvSelectHousehold.text = "Select a Household for Active Case Finding"
            }
            AppDefines.URINE_SAMPLE -> {
                binding.tvSelectHousehold.text = "Select a Child to Update Urine Sample Details"
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.individual_mapping, container, false
        )
        return binding.root
    }

    private fun initArguments() {
        arguments?.let {
            viewModel.surveillanceType = it.getString(AppDefines.SURVEILLANCE_TYPE).toString()
        }
    }

    private fun initListener() {
        binding.showCompleteList.setOnClickListener {
            binding.progressBar.visible()
            binding.tvNoData.visibility = View.GONE
            binding.rvHouseholdList.visible()
            viewModel.houseHoldList.value?.let { it1 -> householdAdapter.setData(it1) }
            binding.progressBar.gone()
            householdAdapter.notifyDataSetChanged()
        }
        binding.findByLocation.setOnClickListener {
            householdAdapter.setData(mutableListOf())
            householdAdapter.notifyDataSetChanged()
            binding.tvNoData.gone()
            binding.progressBar.visible()
            currentLocation?.let {
                viewModel.getHouseHoldByLocation(
                    it.latitude,
                    it.longitude,
                    larvaViewModel.villageUuid,
                    viewModel.surveillanceType
                )
            }
        }
        binding.searchIcon.setOnClickListener {
            if (searchName.isNotEmpty()) {
                binding.tvNoData.gone()
                binding.progressBar.visible()
                householdAdapter.setData(mutableListOf())
                householdAdapter.notifyDataSetChanged()
                viewModel.getHouseholdByName(
                    larvaViewModel.villageLgdCode, searchName, viewModel.surveillanceType
                )
            }
        }
        binding.searchByName.addTextChangedListener(object : TextWatcher {
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
        binding.backButton.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvNilReport.setOnClickListener {
            customAlert(titleText = "Submit Nil Report?",
                subTitleText = "Would you like to save ‘Nil Report’ for today?",
                icon = R.drawable.ic_nil_report_question,
                continueFlow = true,
                ContinueButtonClick = {
                    customAlert(titleText = "Nil Report has been submitted for today",
                        icon = R.drawable.ic_nil_report_submit,
                        exitFlow = true,
                        exitButtonClick = {
                            findNavController().navigate(R.id.individualSurveillanceTypeFragment)
                        })
                })
        }
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
                    viewModel.getImageUrl("")
                }
            } else {
                viewModel.getImageUrl("")
            }
        } else {
            binding.progressBar.gone()
            householdAdapter.setData(houseHoldData)
            householdAdapter.notifyDataSetChanged()
        }
    }

    private fun initAdapter() {
        householdAdapter = HouseHoldAdapter(mutableListOf(), this)
        binding.rvHouseholdList.adapter = householdAdapter
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
                            viewModel.getImageUrl("")
                        }
                    } else {
                        viewModel.getImageUrl("")
                    }
                } else {
                    binding.progressBar.gone()
                    householdAdapter.setData(houseHoldData.subList(0, 6))
                    householdAdapter.notifyDataSetChanged()
                }

            }
        }
        viewModel.childrenList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            houseHoldData = it
            if (it.size > 0) {
                binding.progressBar.gone()
                householdAdapter.setData(houseHoldData)
                householdAdapter.notifyDataSetChanged()
            } else {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvHouseholdList.gone()
            }

        })
        viewModel.houseHoldList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            houseHoldData = it
            if (it.size > 0) {
                binding.progressBar.gone()
                householdAdapter.setData(houseHoldData)
                householdAdapter.notifyDataSetChanged()
            } else {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvHouseholdList.gone()
            }

        })

        viewModel.houseHoldListByName.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.gone()
            if (it.size == 0) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvHouseholdList.gone()
            } else {
                binding.tvNoData.visibility = View.GONE
                binding.rvHouseholdList.visible()
                householdAdapter.setData(it)
                householdAdapter.notifyDataSetChanged()
            }
        })

        viewModel.houseHoldListByLocation.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progressBar.gone()
            if (it.size == 0) {
                binding.tvNoData.visibility = View.VISIBLE
                binding.rvHouseholdList.gone()
            } else {
                binding.rvHouseholdList.visible()
                binding.tvNoData.visibility = View.GONE
                householdAdapter.setData(it)
                householdAdapter.notifyDataSetChanged()
            }
        })

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
        val currentLatLng = LatLng(location.latitude, location.getLongitude())
        try {
            val currentLocationBitmap = getBitmap(R.drawable.map_pin_your_location)
            currentLocationMarker = map?.addMarker(
                MarkerOptions().position(currentLatLng)
                    .icon(BitmapDescriptorFactory.fromBitmap(currentLocationBitmap))
                    .title("Current Location").zIndex(1f)
            )
        } catch (e: Exception) {
            Log.e("some error", "error occurred while getting bitmap")
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

    private fun getMapPinAsset(assetType: String): Int {
        Log.d("asdfasdaf", assetType)
        when (assetType) {
            "Temple" -> return R.drawable.map_pin_temple
            "Mosque" -> return R.drawable.map_pin_mosque
            "Church" -> return R.drawable.map_pin_church
            "Hotel" -> return R.drawable.map_pin_hotel
            "Hospital" -> return R.drawable.map_pin_hospital
            "Office" -> return R.drawable.map_pin_office
            "Construction" -> return R.drawable.map_pin_construction
            "Shop" -> return R.drawable.map_pin_shop
            "SchoolCollage" -> return R.drawable.map_pin_school
            "BusStop" -> return R.drawable.map_pin_bus_stop
            "WaterBody" -> return R.drawable.map_pin_water_body
            "Toilet" -> return R.drawable.map_pin_toilet
            "Park" -> return R.drawable.map_pin_park
        }
        Log.d("not found", assetType)
        return R.drawable.map_pin_shop
    }

    private fun getAddress(): String? {
        var strAddress: String? = null
        val gc = Geocoder(AppController.getInstance().applicationContext)
        if (Geocoder.isPresent()) {
            var list: List<Address>? = null
            try {
                list = currentLocation?.latitude?.let {
                    gc.getFromLocation(
                        it, currentLocation?.longitude!!, 1
                    )
                }
                val address: Address? = list?.get(0)
                val str = StringBuffer()
                str.append(
                    """
                    Name: ${address?.getLocality().toString()}
                    
                    """.trimIndent()
                )
                str.append(
                    """
                    Sub-Admin Ares: ${address?.getSubAdminArea().toString()}
                    
                    """.trimIndent()
                )
                str.append(
                    """
                    Admin Area: ${address?.getAdminArea().toString()}
                    
                    """.trimIndent()
                )
                str.append(
                    """
                    Country: ${address?.getCountryName().toString()}
                    
                    """.trimIndent()
                )
                str.append(
                    """
                    Country Code: ${address?.countryCode.toString()}
                    
                    """.trimIndent()
                )
                strAddress = str.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return strAddress
    }

    private fun loadMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        val task: Task<Location> = fusedLocationProviderClient?.lastLocation as Task<Location>
        task.addOnSuccessListener(OnSuccessListener<Any?> { location ->
            if (location != null) {
            }
        })
    }

    override fun onHouseHoldItemClick(data: HouseHoldDataItem) {
        val bundle = Bundle()
        if (viewModel.surveillanceType == AppDefines.URINE_SAMPLE) {
            bundle.putString(AppDefines.CITIZEN_UUID, data.uuid)
            bundle.putString(
                AppDefines.CITIZEN_INFO, Gson().toJson(data)
            )
            findNavController().navigate(R.id.urineFragment, bundle)
        } else {
            bundle.putString(AppDefines.SURVEILLANCE_TYPE, viewModel.surveillanceType)
            bundle.putString(AppDefines.HOUSE_UUID, data.uuid)
            bundle.putString(AppDefines.INDIVIDUAL_SELECTION, Gson().toJson(data))
            findNavController().navigate(R.id.individualSelectionFragment, bundle)
        }
    }


}