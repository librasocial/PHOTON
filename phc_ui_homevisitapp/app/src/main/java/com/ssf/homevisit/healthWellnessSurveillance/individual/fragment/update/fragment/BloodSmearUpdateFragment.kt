package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment.update.fragment

import android.Manifest
import android.R
import android.app.DatePickerDialog
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.ssf.homevisit.BuildConfig
import com.ssf.homevisit.alerts.VillageLevelMappingAlert
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.databinding.FragmentBloodsmearUpdateBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.BloodSmearLabResult
import com.ssf.homevisit.healthWellnessSurveillance.data.BloodSmearUpdateLabResult
import com.ssf.homevisit.healthWellnessSurveillance.data.BloodSmearUpdateRequestData
import com.ssf.homevisit.healthWellnessSurveillance.individual.fragment.update.viewmodel.BloodSmearUpdateViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.utils.CameraUtility
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BloodSmearUpdateFragment : DialogFragment(), AdapterView.OnItemSelectedListener {

    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private var frontImageClicked = false
    private var secondImageClicked = false
    private val viewModel: BloodSmearUpdateViewModel by viewModels()
    private lateinit var alertDialog: AlertDialog
    private val optionList = arrayOf("Camera", "Gallery")
    private var CAMERA_REQUEST_CODE_IMAGE_1 = 102
    private var CAMERA_REQUEST_CODE_IMAGE_2 = 103
    private var image1Uri: Uri? = null
    var image2Uri: Uri? = null
    var image1: File? = null
    var image2: File? = null
    private var bloodSampleResult: String = ""
    private var sampleDetected: String = ""
    var calendar: Calendar = Calendar.getInstance()
    var reportImageUrl: MutableList<String> = mutableListOf()
    private var labTechnicianName:String =""
    private lateinit var binding: FragmentBloodsmearUpdateBinding
    private lateinit var citizenUuid: String
    private lateinit var surveillanceId: String
    private lateinit var currentDate:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBloodsmearUpdateBinding.inflate(layoutInflater, container, false)
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
                    image1 = CameraUtility.createImageFile(requireContext())
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
    }

    private fun setData() {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        individualSelectionViewModel.individualDetailPropertyData?.let { data ->
            citizenUuid = data.uuid.toString()
            surveillanceId = individualSelectionViewModel.surveillanceId
            data.imageUrls?.let {
                if (it.isEmpty()) {
                    binding.ecPhoto.background =
                        context?.resources?.getDrawable(com.ssf.homevisit.R.drawable.ic_image_place_holder)
                } else {
                    Picasso.get().load(it[0]).centerInside().resize(50, 50).into(binding.ecPhoto)
                }
            }
            if (data.healthID?.isNotEmpty() == true) {
                binding.ecHealthId.text = "Health ID number : " + data.healthID
            }
            binding.ecName.text = data.firstName
            if (data.contact?.isNotEmpty() == true) {
                binding.ecPhoneNo.text = "Ph :" + data.contact
            }
            if (data.dateOfBirth?.isNotEmpty() == true) {
                binding.ecDOB.text = "DOB :" + data.dateOfBirth
            }
            if (data.age == null) {
                if (data.gender?.isNotEmpty() == true) {
                    binding.ecAge.text = data.gender
                } else {
                    binding.ecAge.gone()
                }
            } else {
                if (data.gender?.isNotEmpty() == true) {
                    binding.ecAge.text = data.age.toInt().toString() + "years" + " - " + data.gender
                } else {
                    binding.ecAge.text = data.age.toInt().toString()
                }
            }
        }
        individualSelectionViewModel.individualSurveillanceData.value?.formItems?.forEach {
            if (it.groupName == "Result of the blood sample") {
                val bloodSampleResultData = mutableListOf<String>()
                bloodSampleResultData.add("Select")
                it.elements.forEach {
                    bloodSampleResultData.add(it.title)
                }
                binding.SpinnerBloodSample.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.simple_spinner_item, bloodSampleResultData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.SpinnerBloodSample.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Type of symptom detected") {
                val symptomTypeDetectedData = mutableListOf<String>()
                symptomTypeDetectedData.add("Select")
                it.elements.forEach {
                    symptomTypeDetectedData.add(it.title)
                }
                binding.SpinnerSampleDetected.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.simple_spinner_item, symptomTypeDetectedData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.SpinnerSampleDetected.adapter = chooseDefectAdapter

                }
            }
        }
    }

    private fun initClick() {
        binding.etTechnicianName.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                labTechnicianName=p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        binding.btnCancel.setOnClickListener {
            dismiss()
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

        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            viewModel.updateBloodSmear(
                requestData = BloodSmearUpdateRequestData(
                    type = "LABRESULT", properties = BloodSmearUpdateLabResult(
                        labResult = BloodSmearLabResult(
                            result = bloodSampleResult,
                            technicianName = labTechnicianName,
                            resultDate = currentDate,
                            reportImages = reportImageUrl
                        )
                    )
                ), surveillanceId
            )
        }

        binding.llSampleCollected.setOnClickListener {
            context?.let {
                DatePickerDialog(
                    it,
                    dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            if (dayOfMonth < 10) {
                val x = "0${dayOfMonth.toString()}"
                updateDateInView(year, monthOfYear, x)
            } else {
                updateDateInView(year, monthOfYear, dayOfMonth.toString())
            }
        }

    private fun updateDateInView(year: Int, monthOfYear: Int, dayOfMonth: String) {
        val date = "$year-$monthOfYear-$dayOfMonth"
        binding.tvCalendar.text = date
    }

    private fun initObserver() {
        viewModel.saveDataResponse.observe(viewLifecycleOwner, Observer {
            binding.progress.gone()
            if (it) {
                apiSuccessFailureDialog(titleText = "Lab result has been updated successfully!",
                    buttonText = "Click here to Continue",
                    isSuccess = true,
                    successButtonClick = {
                        findNavController().popBackStack(
                            com.ssf.homevisit.R.id.individualSelectionFragment, false
                        )
                    })
            } else {
                apiSuccessFailureDialog(isSuccess = false, failureButtonCLick = {
                    Toast.makeText(
                        context, "Response  Saved Failed", Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                })
            }
        })
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
        if (p0?.equals(binding.SpinnerBloodSample) == true) {
            bloodSampleResult = data
            binding.btnContinue.let {
                it.isFocusable = true
                it.isClickable = true
                it.isEnabled = true
                context?.resources?.getColor(com.ssf.homevisit.R.color.button_dark_blue)
                    ?.let { it1 -> it.setBackgroundColor(it1) }
            }
            if (data == "Positive") {
                binding.tvSampleDetected.visible()
                binding.llSampleDetected.visible()
            } else {
                binding.tvSampleDetected.gone()
                binding.llSampleDetected.gone()
            }

        }
        if (p0?.equals(binding.SpinnerSampleDetected) == true) {
            sampleDetected = data
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}