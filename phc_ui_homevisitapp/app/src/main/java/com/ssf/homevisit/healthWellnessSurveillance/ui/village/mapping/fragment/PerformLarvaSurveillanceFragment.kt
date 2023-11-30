package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ssf.homevisit.BuildConfig
import com.ssf.homevisit.R
import com.ssf.homevisit.alerts.VillageLevelMappingAlert
import com.ssf.homevisit.alerts.VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_1
import com.ssf.homevisit.alerts.VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_1
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.FragmentPerformLarvaSurveillanceBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.*
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.MappingRawModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.SurveillanceMappingMenuItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.SurveillanceMappingModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter.MappingDynamicAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter.SurveillanceMappingAdapter
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.viewModel.SurveillanceMappingViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.PlaceItem
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.CameraUtility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class PerformLarvaSurveillanceFragment : Fragment(),
        SurveillanceMappingAdapter.OnSurveillanceItemClick,
        MappingDynamicAdapter.OnItemClick {

    private val viewModel: SurveillanceMappingViewModel by viewModels()
    private lateinit var surveillanceAdapter: SurveillanceMappingAdapter
    private var mappingList: MutableList<MappingRawModel> = mutableListOf()
    private lateinit var mappingAdapter: MappingDynamicAdapter
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private var datePosition = -1
    private var imagePosition = -1
    private var saveCounter: Int = 0
    private var totalData = 0
    private lateinit var flowType: String
    private lateinit var houseHoldDetails: HouseHoldDataItem
    private val optionList = arrayOf("Camera", "Gallery")
    private lateinit var alertDialog: AlertDialog
    private lateinit var place: PlaceItem
    private var surveillanceSelectedHashMap: MutableSet<String> = mutableSetOf()
    private var imageListHashMap: HashMap<Int, String> = hashMapOf()
    var image: File? = null
    var imageUri: Uri? = null
    private var surveillanceMappingList: MutableList<SurveillanceMappingModel> = mutableListOf()
    private var uploadPosition: Int = 0
    private lateinit var fragmentBinding: FragmentPerformLarvaSurveillanceBinding
    var calendar: Calendar = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            alertDialog = AlertDialog.Builder(it).setTitle("UploadImage From").setItems(
                    optionList
            ) { dialog, position ->
                if (position == 0) {
                    openCamera(CAMERA_REQUEST_CODE_IMAGE_1, position)
                }
                if (position == 1) {
                    openCamera(GALLERY_REQUEST_CODE_IMAGE_1, position)
                }
                dialog.dismiss()
            }.create()
        }
        initAdapter()
        initObserver()
        initArguments()
        initClick()
        setData()
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        fragmentBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_perform_larva_surveillance, container, false
        )
        return fragmentBinding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun initObserver() {
        collectSharedFlowData(viewModel.larvaSurveillance) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = sdf.format(Date())
            if (it.content.isEmpty()) {
                if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
                    viewModel.createSurveillance(
                            placeId = place.id,
                            placeName = place.name,
                            placeType = place.assetType,
                            dateOfSurveillance = currentDate,
                            villageId = larvaViewModel.villageUuid
                    )
                }
                if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
                    viewModel.createSurveillance(
                            dateOfSurveillance = currentDate,
                            villageId = larvaViewModel.villageUuid,
                            houseHoldId = larvaViewModel.houseHoldId
                    )
                }
            } else {
                viewModel.getLarvaVisits(viewModel.surveillanceId)
            }
        }

        collectSharedFlowData(viewModel.mappingList) {
            if (it.isNotEmpty()) {
                surveillanceMappingList = it as MutableList<SurveillanceMappingModel>
                surveillanceAdapter.setData(it)
            }
        }

        collectSharedFlowData(viewModel.saveLarvaSurveillance) {
            saveCounter += 1
            Toast.makeText(context, "${saveCounter}/${totalData} saved", Toast.LENGTH_SHORT).show()
            if (saveCounter == totalData) {
                fragmentBinding.progressBar.visibility = View.GONE
                viewModel.viewModelScope.launch {
                    delay(1000)
                    apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                        findNavController().popBackStack(R.id.selectSTFragment, false)
                    })
                }

            } else {
                saveSurveillance(mappingList[saveCounter])
            }
        }

        collectSharedFlowData(viewModel.preSignedUrl) {
            if (it.isNotEmpty()) {
                val stream = ByteArrayOutputStream()
                val imageInByte = stream.toByteArray()
                val body = RequestBody.create(
                        "image/jpeg".toMediaTypeOrNull(), imageInByte
                )
                viewModel.uploadToS3Bucket(it, "image/jpeg", body)
            }
        }
        collectSharedFlowData(viewModel.uploadToBucket) {
            uploadPosition += 1
            if (uploadPosition < totalData) {
                while (imageListHashMap[uploadPosition] == null) {
                    uploadPosition += 1
                    if (uploadPosition > totalData) {
                        break
                    }
                }
                if (uploadPosition <= totalData) {
                    imageListHashMap[uploadPosition]?.let {
                        getPreSignedUrl(
                                it
                        )
                    }
                } else {
                    saveSurveillance(mappingList[0])
                }
            } else {
                saveSurveillance(mappingList[0])
            }
        }

        collectSharedFlowData(viewModel.larvaVisit) {
            when (it) {
                is DataState.Error -> Timber.e(it.errorMessage)
                DataState.Loading -> Timber.e(it.toString())
                is DataState.Success -> {
                    mappingList = mutableListOf()
                    it.baseResponseData.contents.forEach {
                        mappingList.add(
                                MappingRawModel(
                                        surveillanceMappingModel = SurveillanceMappingModel(
                                                menuIconId = -1,
                                                menuItem = SurveillanceMappingMenuItem.Sump
                                        ),
                                        breedingSpot = it.breedingSpotName,
                                        breedingSite = it.breedingSite,
                                        isLarvaFound = it.isLarvaFound,
                                        solutionProvided = it.solutionProvided,
                                        image = it.imageUrl,
                                        nextInspection = it.nextInspectionDate,
                                        disabled = true
                                )
                        )
                    }
                    fragmentBinding.btnContinue.isEnabled = true
                    fragmentBinding.btnContinue.isClickable = true
                    mappingAdapter.setData(mappingList)
                    uploadPosition = mappingList.size
                    mappingAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun saveSurveillance(item: MappingRawModel) {
        val isBreedingSiteSelected = mappingList.any { it.breedingSite?.isNotEmpty() ?: false }
        val isLarvaFoundSelected = mappingList.any { it.isLarvaFound?.isNotEmpty() ?: false }
        if (isBreedingSiteSelected && isLarvaFoundSelected) {
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val visitDate = sdf.format(Date())
            val data = SaveSurveillance(
                    breedingSpotName = item.breedingSpot,
                    imageUrl = item.image,
                    isLarvaFound = item.isLarvaFound,
                    nextInspectionDate = item.nextInspection,
                    solutionProvided = item.solutionProvided,
                    larvaSurveillanceId = viewModel.surveillanceId,
                    visitDate = visitDate
            )
            viewModel.saveSurveillance(data)
        } else {
            Toast.makeText(context, "Fill All Required Fields", Toast.LENGTH_SHORT).show()
            fragmentBinding.progressBar.gone()
        }
    }

    private fun initAdapter() {
        context?.let {
            mappingAdapter = MappingDynamicAdapter(mutableListOf(), this, it)
            surveillanceAdapter = SurveillanceMappingAdapter(mutableListOf(), this, it)
        }
        fragmentBinding.breedingSpotRv.adapter = surveillanceAdapter
        fragmentBinding.breedingSpotRvRaw.adapter = mappingAdapter
    }

    private fun openCamera(requestCode: Int, position: Int) {
        if (ActivityCompat.checkSelfPermission(
                        AppController.getInstance().applicationContext, Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    AppController.getInstance().mainActivity, arrayOf(Manifest.permission.CAMERA), 2
            )
            return
        }
        if (ActivityCompat.checkSelfPermission(
                        AppController.getInstance().applicationContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    AppController.getInstance().mainActivity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    3
            )
            return
        }
        if (ActivityCompat.checkSelfPermission(
                        AppController.getInstance().applicationContext,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                    AppController.getInstance().mainActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    4
            )
            return
        }
        try {
            image = CameraUtility.createImageFile(requireContext());
            image?.name?.let { imageListHashMap.put(imagePosition, it) }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (position == 0) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (requestCode == VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_1) {
                imageUri = image?.let {
                    FileProvider.getUriForFile(
                            requireContext().applicationContext,
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                    )
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            this.startActivityForResult(intent, requestCode)
        }
        if (position == 1) {
            val intent = Intent().apply {
                this.type = "image/*"
                this.action = Intent.ACTION_GET_CONTENT
            }
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"),
                    requestCode
            )
        }
    }

    private fun initClick() {
        fragmentBinding.btnContinue.setOnClickListener {
            totalData = mappingList.size - uploadPosition
            if (totalData > 0) {
                fragmentBinding.progressBar.visibility = View.VISIBLE
                pushImagesToS3(uploadPosition)
            }
            else{
                Toast.makeText(this.requireContext(),"Please add Larva Visit",Toast.LENGTH_LONG).show()
            }
        }

        fragmentBinding.tvCancel.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun pushImagesToS3(position: Int) {
        val image = imageListHashMap[position]
        if (image != null) {
            getPreSignedUrl(
                    image
            )
        } else {
            uploadPosition += 1
            while (uploadPosition < totalData) {
                if (imageListHashMap[uploadPosition] == null) {
                    uploadPosition += 1
                } else {
                    break
                }
            }
            if (uploadPosition < totalData) {
                imageListHashMap[uploadPosition]?.let { getPreSignedUrl(it) }
            } else {
                saveSurveillance(mappingList[position])
            }
        }
    }

    private fun getPreSignedUrl(
            bucketKey: String
    ) {
        viewModel.getPreSignedUrl(bucketKey)
    }

    private fun setData() {
        viewModel.initMappingList()
    }


    private fun initArguments() {
        flowType = larvaViewModel.flowType
        arguments?.let {
            if (it.containsKey(AppDefines.PLACE_DETAILS)) {
                val type = object : TypeToken<PlaceItem>() {}.type
                place = Gson().fromJson(it.getString(AppDefines.PLACE_DETAILS), type)
            }
            if (it.containsKey(AppDefines.HOUSEHOLD_DETAILS)) {
                val type = object : TypeToken<HouseHoldDataItem>() {}.type
                houseHoldDetails = Gson().fromJson(it.getString(AppDefines.HOUSEHOLD_DETAILS), type)
                fragmentBinding.tvNumberMembers.text =
                        "No. of Members in HH : ${houseHoldDetails.memberCount}"
                if (!houseHoldDetails.imageUrl.isNullOrEmpty()) {
                    houseHoldDetails.imageUrl?.let {
                        if (it.isNotEmpty()) {
                            if (it[0].isNotEmpty()) {
                                Picasso.get().load(houseHoldDetails.imageUrl?.get(0))
                                        .into(fragmentBinding.profileImage)
                            } else {
                                fragmentBinding.tvNoImage.visible()
                                fragmentBinding.ivNoImage.visible()
                            }
                        }
                    }
                } else {
                    fragmentBinding.tvNoImage.visible()
                    fragmentBinding.ivNoImage.visible()
                }
                fragmentBinding.titleTextView.text = houseHoldDetails.nameHouseHoldHead
                fragmentBinding.dateTitleValue.text =
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                                Date()
                        )
                fragmentBinding.templeIcon.background =
                        context?.resources?.getDrawable(R.drawable.ic_house)
            }

            if (flowType == AppDefines.HNW_VILLAGE_SURVEILLANCE) {
                fragmentBinding.tvSurveyType.text = "Village Level"
                if (this::place.isInitialized) {
                    if (place.imageUrl.isNotEmpty()) {
                        Picasso.get().load(place.imageUrl)
                                .into(fragmentBinding.profileImage)
                    } else {
                        fragmentBinding.tvNoImage.visible()
                        fragmentBinding.ivNoImage.visible()
                    }
                    fragmentBinding.titleTextView.text = place.name
                    fragmentBinding.dateTitleValue.text =
                            SimpleDateFormat("yyyy--MM-dd", Locale.getDefault()).format(
                                    Date()
                            )
                    fragmentBinding.tvImageTitle.text = place.name
                }
                viewModel.getLarvaByFilter(
                        larvaViewModel.villageUuid, place.id, place.assetType
                )
            }
            if (it.containsKey(AppDefines.ASSET_TYPE)) {
                val asset = it.getString(AppDefines.ASSET_TYPE)
                if (asset != null) {
                    setImageForAsset(asset)
                }
            }
            if (flowType == AppDefines.HNW_HOUSEHOLD_SURVEILLANCE) {
                fragmentBinding.tvSurveyType.text = "Household Level"
                viewModel.getLarvaByFilter(
                        villageId = larvaViewModel.villageUuid, houseHoldId = larvaViewModel.houseHoldId
                )
                fragmentBinding.tvImageTitle.text = houseHoldDetails.nameHouseHoldHead
                fragmentBinding.tvNumberMembers.visible()
                fragmentBinding.vw.visible()

            }

        }

    }

    private fun setImageForAsset(asset: String) {
        when (asset) {
            "Shop" -> {
                setImageDrawable(R.drawable.ic_shop)
            }
            "Home" -> {
                setImageDrawable(R.drawable.ic_home)
            }
            "Hotel" -> {
                setImageDrawable(R.drawable.ic_hotel)
            }
            "Others" -> {
                setImageDrawable(R.drawable.ic_others)
            }
            "WaterBody" -> {
                setImageDrawable(R.drawable.waterbodyicon)
            }
            "School/college" -> {
                setImageDrawable(R.drawable.ic_school_college)
            }
            "Temple" -> {
                setImageDrawable(R.drawable.ic_temple)
            }
            "Mosque" -> {
                setImageDrawable(R.drawable.ic_mosque)
            }
            "Church" -> {
                setImageDrawable(R.drawable.ic_church)
            }
            "Office" -> {
                setImageDrawable(R.drawable.ic_office)
            }
            "Hospital" -> {
                setImageDrawable(R.drawable.ic_hospital)
            }
            "Construction" -> {
                setImageDrawable(R.drawable.ic_construction)
            }
            "Bus Stop" -> {
                setImageDrawable(R.drawable.ic_bus_stop)
            }
            "Toilet" -> {
                setImageDrawable(R.drawable.ic_toilet)
            }
            "Park" -> {
                setImageDrawable(R.drawable.ic_park)
            }
        }

    }

    private fun setImageDrawable(imageId: Int) {
        fragmentBinding.templeIcon.background =
                context?.resources?.getDrawable(imageId)
    }


    override fun onItemClick(
            data: MappingRawModel,
            position: Int,
            action: String,
            item: String?,
            spinnerPosition: Int
    ) {
        when (action) {
            "attachImage" -> {
                imagePosition = position
                alertDialog.show()
            }
            "deleteRow" -> {
                if (mappingList.size > 0) {
                    val iterator = surveillanceMappingList.listIterator()
                    while (iterator.hasNext()) {
                        val index = iterator.nextIndex()
                        val surveillanceMappingModel = iterator.next()
                        if (surveillanceMappingModel.menuIconId == data.surveillanceMappingModel.menuIconId) {
                            surveillanceMappingList[index].isSelected = false
                            surveillanceAdapter.notifyItemChanged(index)
                            break
                        }
                    }
                    mappingList.removeAt(position)
                    surveillanceSelectedHashMap.remove(data.surveillanceMappingModel.menuItem.menuName)
                    mappingAdapter.setData(mappingList)
                }
            }
            "datePicker" -> {
                datePosition = position
            }
            "solutionSpinner" -> {
                if (spinnerPosition >= 0) {
                    mappingList[position].solutionProvided = item.toString()
                    mappingAdapter.notifyItemChanged(position)
                }
            }
            "anyLarvaSpinner" -> {
                if (spinnerPosition >= 0) {
                    mappingList[position].isLarvaFound = item.toString()
                    if (item.toString() == "Yes") {
                        fragmentBinding.solutionProvided.setTextColor(resources.getColor(R.color.black,null))
                    }
                    else{
                        fragmentBinding.solutionProvided.setTextColor(resources.getColor(R.color.selected_grey,null))
                    }
                    mappingAdapter.notifyItemChanged(position)
                }
            }
            "breedingSiteSpinner" -> {
                if (spinnerPosition >= 0) {
                    mappingList[position].breedingSite = item.toString()
                    mappingAdapter.notifyItemChanged(position)
                }
            }
            else -> {}
        }
    }


    @SuppressLint("SimpleDateFormat")
    override fun onSurveillanceItemClick(
            data: SurveillanceMappingModel,
            position: Int
    ) {
        if (!surveillanceSelectedHashMap.contains(data.menuItem.menuName)) {
            surveillanceSelectedHashMap.add(data.menuItem.menuName)
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = Date()
            val calendar = Calendar.getInstance()
            calendar.time = currentDate
            calendar.add(Calendar.DAY_OF_MONTH, 15)
            val next15thDay = sdf.format(calendar.time)
            mappingList.add(
                    MappingRawModel(
                            surveillanceMappingModel = SurveillanceMappingModel(
                                    menuIconId = data.menuIconId,
                                    menuItem = data.menuItem
                            ),
                            breedingSpot = data.menuItem.menuName,
                            breedingSite = "",
                            isLarvaFound = "",
                            solutionProvided = "",
                            image = "",
                            nextInspection = next15thDay,
                            disabled = false
                    )
            )
            Log.d("Tag","mapping list ${mappingList.size}")
            data.isSelected = true
            fragmentBinding.btnContinue.isClickable = true
            fragmentBinding.btnContinue.isEnabled = true
            mappingAdapter.setData(mappingList)
            fragmentBinding.breedingSpotRvRaw.smoothScrollToPosition(mappingList.size - 1)

        } else {
            data.isSelected = false
            val iterator = mappingList.listIterator()
            while (iterator.hasNext()) {
                val index = iterator.nextIndex()
                val mappingRawModel = iterator.next()
                if (mappingRawModel.surveillanceMappingModel.menuIconId == data.menuIconId) {
                    mappingList.removeAt(index)
                    surveillanceSelectedHashMap.remove((data.menuItem.menuName))
                    break
                }
            }
            mappingAdapter.setData(mappingList)
        }
        surveillanceAdapter.notifyItemChanged(position)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE_IMAGE_1 -> {
                    mappingList[imagePosition].image = imageListHashMap[imagePosition].toString()
                    mappingAdapter.notifyItemChanged(imagePosition)
                }
                GALLERY_REQUEST_CODE_IMAGE_1 -> {
                    val selectedImageUri = data!!.data
                    if (selectedImageUri != null) {
                        mappingList[imagePosition].image = selectedImageUri.toString()
                        mappingAdapter.setData(mappingList)
                    }
                }
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadPosition = 0
        saveCounter = 0
        imageListHashMap = hashMapOf()
        surveillanceSelectedHashMap = mutableSetOf()
        mappingList = mutableListOf()
    }
}