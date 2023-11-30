package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment.update.fragment

import android.Manifest
import android.R
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
import com.ssf.homevisit.databinding.FragmentUrineUpdateBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.UrineLabResult
import com.ssf.homevisit.healthWellnessSurveillance.data.UrineRequestData
import com.ssf.homevisit.healthWellnessSurveillance.individual.fragment.update.viewmodel.UrineUpdateViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualMappingViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.utils.CameraUtility
import com.ssf.homevisit.utils.CameraUtility.createImageFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UrineUpdateFragment : DialogFragment(), AdapterView.OnItemSelectedListener {


    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private lateinit var binding: FragmentUrineUpdateBinding
    private var frontImageClicked = false
    private val viewModel: UrineUpdateViewModel by viewModels()
    private val individualMappingViewModel: IndividualMappingViewModel by activityViewModels()
    private var secondImageClicked = false
    private lateinit var alertDialog: AlertDialog
    private val optionList = arrayOf("Camera", "Gallery")
    private var CAMERA_REQUEST_CODE_IMAGE_1 = 102
    private var CAMERA_REQUEST_CODE_IMAGE_2 = 103
    private var image1Uri: Uri? = null
    var image2Uri: Uri? = null
    var image1: File? = null
    var image2: File? = null
    private var urineSampleResult:String = ""
    var reportImageUrl: MutableList<String> = mutableListOf()
    private var dentalFlurosisData: String = ""
    private lateinit var citizenUuid: String
    private lateinit var currentDate:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentUrineUpdateBinding.inflate(layoutInflater, container, false)
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
                    image1 = createImageFile(requireContext())
                    reportImageUrl.add(image1?.name.toString())
                }
                if (secondImageClicked) {
                    image2 = createImageFile(requireContext())
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
        individualMappingViewModel.citizenInfo.let { data ->
            citizenUuid = data.uuid.toString()
            binding.ecName.text = data.name
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
            if (data.phoneNumber?.isNotEmpty() == true) {
                binding.ecPhoneNo.text = "Ph :" + data.phoneNumber
            }
            if (data.dob?.isNotEmpty() == true) {
                binding.ecDOB.text = "DOB :" + data.dob
            }
            data.imageUrl?.let {
                if (it.isEmpty()) {
                    binding.ecPhoto.background =
                        context?.resources?.getDrawable(com.ssf.homevisit.R.drawable.ic_image_place_holder)
                } else {
                    if (it.first().isNotEmpty()) {
                        viewModel.getImageUrl(it.first())
                    } else {
                        binding.ecPhoto.background =
                            context?.resources?.getDrawable(com.ssf.homevisit.R.drawable.ic_image_place_holder)
                    }
                }
            }
        }
        individualSelectionViewModel.individualSurveillanceData.value?.formItems?.forEach {
            if (it.groupName == "Dental Fluorosis") {
                val dentalFlurosisData = mutableListOf<String>()
                dentalFlurosisData.add("Select")
                it.elements.forEach {
                    dentalFlurosisData.add(it.title)
                }
                binding.SpinnerDentalFluorsis.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.simple_spinner_item, dentalFlurosisData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.SpinnerDentalFluorsis.adapter = chooseDefectAdapter

                }
            }
        }
    }

    private fun initClick() {
        binding.etUrineSampleResult.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                urineSampleResult = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
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
            viewModel.updateUrine(
                requestData = UrineRequestData(
                    labResult = UrineLabResult(
                        isDentalFlurosisFound = dentalFlurosisData,
                        reportImages = reportImageUrl,
                        result = urineSampleResult,
                        resultDate = currentDate
                    ),
                    citizenId = citizenUuid,
                ), surveillanceId = individualSelectionViewModel.surveillanceId
            )
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }

    private fun initObserver() {
        viewModel.saveDataResponse.observe(viewLifecycleOwner, Observer {
            binding.progress.gone()
            if (it) {
                apiSuccessFailureDialog(titleText = "Lab result has been updated successfully!",
                    buttonText = "Click here to Continue",
                    isSuccess = true,
                    successButtonClick = {
                        Toast.makeText(
                            context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                        ).show()
                        findNavController().popBackStack(
                            com.ssf.homevisit.R.id.individualMappingFragment, false
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
        viewModel.presignedUrl.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer {
                Picasso.get().load(it).resize(100, 100).into(binding.ecPhoto)
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
        if (p0?.equals(binding.SpinnerDentalFluorsis) == true) {
            dentalFlurosisData = data
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}