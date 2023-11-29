package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ssf.homevisit.BuildConfig
import com.ssf.homevisit.R
import com.ssf.homevisit.alerts.VillageLevelMappingAlert
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.FragmentWaterSampleReportBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.AcfRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.LabResult
import com.ssf.homevisit.healthWellnessSurveillance.data.TestResult
import com.ssf.homevisit.healthWellnessSurveillance.data.WaterSample
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.viewModel.WaterSampleViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.PlaceItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.TestResultDataItem
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.CameraUtility
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class UpdateWaterSampleReportFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private var frontImageClicked = false
    private var secondImageClicked = false
    private lateinit var alertDialog: AlertDialog
    private val optionList = arrayOf("Camera", "Gallery")
    private var CAMERA_REQUEST_CODE_IMAGE_1 = 102
    private var CAMERA_REQUEST_CODE_IMAGE_2 = 103
    private var image1Uri: Uri? = null
    private var placeDetails: PlaceItem? = null
    private lateinit var testResultDataItem: TestResultDataItem
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private val viewModel: WaterSampleViewModel by activityViewModels()
    var image2Uri: Uri? = null
    var image1: File? = null
    var image2: File? = null
    var reportImageUrl: MutableList<String> = mutableListOf()
    private var labTechnicianName: String = ""
    private var h2sResultSample: String = ""
    private lateinit var binding: FragmentWaterSampleReportBinding
    private lateinit var currentDate: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWaterSampleReportBinding.inflate(layoutInflater, container, false)
        this.isCancelable = false
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            alertDialog = AlertDialog.Builder(it).setTitle("UploadImage From").setItems(
                    optionList
            ) { dialog, position ->
                if (frontImageClicked) {
                    if (position == 0) {
                        openCamera(VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_1, position)
                    } else {
                        openCamera(VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_1, position)
                    }

                }
                if (secondImageClicked) {
                    if (position == 0) {
                        openCamera(VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_2, position)
                    } else {
                        openCamera(VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_2, position)
                    }
                }
                dialog.dismiss()
            }.create()
        }
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
        if (position == 0) {
            try {
                if (frontImageClicked) {
                    image1 = CameraUtility.createImageFile(requireContext());
                    reportImageUrl.add(image1?.name.toString())
                }
                if (secondImageClicked) {
                    image2 = CameraUtility.createImageFile(requireContext());
                    reportImageUrl.add(image2?.name.toString())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (requestCode == VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_1) {
                image1Uri = image1?.let {
                    FileProvider.getUriForFile(
                            requireContext().applicationContext,
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                    )
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image1Uri)
            } else if (requestCode == VillageLevelMappingAlert.CAMERA_REQUEST_CODE_IMAGE_2) {
                image2Uri = image2?.let {
                    FileProvider.getUriForFile(
                            requireContext().applicationContext,
                            BuildConfig.APPLICATION_ID + ".provider",
                            it
                    )
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, image2Uri)
            }
            this.startActivityForResult(intent, requestCode)
        } else {
            try {
                if (frontImageClicked) {
                    image1 = CameraUtility.createImageFile(requireContext())
                    reportImageUrl.add(image1?.name.toString())
                }
                if (secondImageClicked) {
                    image2 = CameraUtility.createImageFile(requireContext())
                    reportImageUrl.add(image2?.name.toString())
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val intent = Intent().apply {
                this.type = "image/*"
                this.action = Intent.ACTION_GET_CONTENT
            }
            if (frontImageClicked) {
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_1
                )
            }
            if (secondImageClicked) {
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_2
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setData()
        initClick()
        initObserver()
        initArguments()
    }

    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.PLACE_DETAILS)) {
                val type = object : TypeToken<PlaceItem>() {}.type
                placeDetails = Gson().fromJson(it.getString(AppDefines.PLACE_DETAILS), type)
                if (placeDetails?.imageUrl?.isNotEmpty() == true) {
                    Picasso.get().load(placeDetails?.imageUrl)
                            .into(binding.ivPlace)
                    binding.tvImageTitle.text=placeDetails?.name
                } else {
                    binding.tvError.visible()
                    binding.ivError.visible()
                }
            }
            if (it.containsKey(AppDefines.ASSET_TYPE)) {
                val asset = it.getString(AppDefines.ASSET_TYPE).toString()
                setImageForAsset(asset)
            }
            if (it.containsKey(AppDefines.TEST_RESULT_DATA)) {
                val type = object : TypeToken<TestResultDataItem>() {}.type
                testResultDataItem = Gson().fromJson(it.getString(AppDefines.TEST_RESULT_DATA), type)
            }
        }
    }

    private fun setData() {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate
    }

    private fun initClick() {
        binding.etLabTechnician.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                labTechnicianName = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        binding.SpinnerH2sSample.onItemSelectedListener = this
        val h2sResult = listOf("Select", "SPP (Satisfactory for Portable Purpose)", "NSPP (Not Satisfactory for Portable Purpose)")
        context?.let { context ->
            val chooseDefectAdapter = ArrayAdapter(
                    context, android.R.layout.simple_spinner_item, h2sResult
            )
            chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.SpinnerH2sSample.adapter = chooseDefectAdapter
        }

        binding.frontImage.setOnClickListener {
            frontImageClicked = true
            secondImageClicked = false
            alertDialog.show()
        }
        binding.secondImage.setOnClickListener {
            frontImageClicked = false
            secondImageClicked = true
            alertDialog.show()
        }
        binding.btnSave.setOnClickListener {
            binding.progress.visible()
            placeDetails?.let { placeItem ->
                if(h2sResultSample=="Select"){
                    h2sResultSample=""
                }
                viewModel.updateWaterSampleReport(
                        villageId = larvaViewModel.villageUuid,
                        userId = larvaViewModel.userId,
                        placeId = placeItem.id,
                        placeType = placeItem.assetType,
                        placeName = placeItem.name,
                        testResult = TestResult(
                                reportImageUrl = reportImageUrl,
                                labTechName = labTechnicianName,
                                h2sResult = h2sResultSample,
                                resultDate = currentDate
                        ),
                        sampleId = testResultDataItem.sampleId
                )
            }
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initObserver() {
        collectSharedFlowData(viewModel.isWaterSampleUpdated) {
            if (it) {
                apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                    Toast.makeText(
                            context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                })
            } else {
                apiSuccessFailureDialog(isSuccess = false, failureButtonCLick = {
                    Toast.makeText(
                            context, "Response  Saved Failed", Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                })
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE_IMAGE_1 -> {
                    binding.frontImage.setImageURI(image1Uri)
                }
                CAMERA_REQUEST_CODE_IMAGE_2 -> {
                    binding.secondImage.setImageURI(image2Uri)
                }
                VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_1 -> {
                    val selectedImageUri = data!!.data
                    if (selectedImageUri != null) {
                        binding.frontImage.setImageURI(selectedImageUri)
                    }
                }
                VillageLevelMappingAlert.GALLERY_REQUEST_CODE_IMAGE_2 -> {
                    val selectedImageUri = data!!.data
                    if (selectedImageUri != null) {
                        binding.secondImage.setImageURI(selectedImageUri)
                    }
                }
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(binding.SpinnerH2sSample) == true) {
            h2sResultSample = data
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

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
        binding.ivSelectHousehold.background =
                context?.resources?.getDrawable(imageId)
    }

}