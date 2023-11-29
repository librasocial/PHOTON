package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.Response
import com.ssf.homevisit.healthWellnessSurveillance.data.UrineRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.UrineSample
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualMappingViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.UrineViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class UrineFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: com.ssf.homevisit.databinding.FragmentUrineBinding
    private val viewModel: UrineViewModel by viewModels()
    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private var fatherName: String = ""
    private var schoolName: String = ""
    private var sampleCount: Int = 0
    private val individualMappingViewModel: IndividualMappingViewModel by activityViewModels()
    var calendar: Calendar = Calendar.getInstance()
    private var dateOfSubmission: String? = ""
    var submissionBoolean: Boolean? = false
    var collectionBoolean: Boolean? = false
    private var dateOfCollection: String? = ""
    private lateinit var currentDate:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_urine, container, false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        getData()
        initClick()
        initObserver()
        setData()
    }

    private fun fetchLabReport() {
        viewModel.getLabReportData(individualMappingViewModel.citizenInfo.uuid.toString())
    }

    private val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, monthOfYear)
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        val month = monthOfYear + 1
        var monthString = ""
        if (month < 10) {
            monthString = "0$month"
        } else {
            monthString = month.toString()
        }
        if (dayOfMonth < 10) {
            val x = "0$dayOfMonth"
            updateDateInView(year, monthString, x)
        } else {
            updateDateInView(year, monthString, dayOfMonth.toString())
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun updateDateInView(year: Int, monthString: String, dayOfMonth: String) {
        val date = "$year-$monthString-$dayOfMonth"
        val convertedDateTime = GregorianCalendar(year, monthString.toInt() - 1, dayOfMonth.toInt()).time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val convertedDate = dateFormat.format(convertedDateTime)
        if (submissionBoolean == true) {
            dateOfSubmission = convertedDate
            binding.tvDateOfSampleSubmitted.text = date
        }
        if (collectionBoolean == true) {
            dateOfCollection = convertedDate
            binding.tvDateOfSampleCollected.text = date
        }
    }

    private fun setData() {
        individualMappingViewModel.citizenInfo.let { data ->
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
            fetchLabReport()
            data.imageUrl?.let {
                if (it.isEmpty()) {
                    binding.ecPhoto.background =
                        context?.resources?.getDrawable(R.drawable.ic_image_place_holder)
                } else {
                    if (it.first().isNotEmpty()) {
                        viewModel.getImageUrl(it.first())
                    } else {
                        binding.ecPhoto.background =
                            context?.resources?.getDrawable(R.drawable.ic_image_place_holder)
                    }
                }
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun initUi(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        response.formItems.forEach {
            if (it.groupName == "Show Complete list of Children") {
                val schoolData: MutableList<String> = mutableListOf()
                schoolData.add("Type")
                it.elements.forEach {
                    schoolData.add(it.title)
                }
            }
            if (it.groupName == " No of Sample Collected ") {
                val sampleCountData: MutableList<String> = mutableListOf()
                sampleCountData.add("Select")
                it.elements.forEach {
                    sampleCountData.add(it.title)
                }
                binding.sampleCountSpinner.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, sampleCountData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.sampleCountSpinner.adapter = chooseDefectAdapter
                }
            }
        }
    }

    private fun initClick() {
        binding.value7.setOnClickListener {
            findNavController().navigate(R.id.urineUpdateFragment)
        }
        binding.llSampleSubmission.setOnClickListener {
            submissionBoolean = true
            collectionBoolean = false
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
        binding.llSampleCollected.setOnClickListener {
            collectionBoolean = true
            submissionBoolean = false
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
        binding.etFathersName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                fatherName = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        binding.etSchoolName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                schoolName = p0.toString()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            viewModel.saveUrineData(
                requestData = UrineRequestData(
                    citizenId = individualMappingViewModel.citizenInfo.uuid.toString(),
                    dateOfSurveillance = currentDate,
                    fatherName = fatherName,
                    schoolAttending = schoolName,
                    labResult = null,
                    sample = UrineSample(
                        labReceivedDate = dateOfCollection,
                        isSampleCollected = "",
                        sampleSubmittedDate = dateOfSubmission,
                        noOfSampleCollected = sampleCount
                    )
                )
            )
        }
        binding.tvCancel.setOnClickListener {
            apiSuccessFailureDialog(titleText = "Would you like to exit without saving?",
                buttonText = "Exit",
                isSuccess = false,
                failureButtonCLick = {
                    findNavController().popBackStack(R.id.individualMappingFragment, false)
                })
        }
    }

    private fun getData() {
        individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_UrineSample)
    }

    private fun initObserver() {
        viewModel.saveDataResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progress.gone()
            if (it != null) {
                apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                    Toast.makeText(
                        context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                    ).show()
                    individualSelectionViewModel.surveillanceId=it.surveillanceId.toString()
                    findNavController().navigate(R.id.urineFragment)
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
        viewModel.presignedUrl.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            Picasso.get().load(it).resize(100, 100).into(binding.ecPhoto)
        })
        individualSelectionViewModel.individualSurveillanceData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response != null) {
                    initUi(response)
                }
            })
        viewModel.urineData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (it.data.isNotEmpty()) {
                    it.data.first().let { response ->
                        individualSelectionViewModel.surveillanceId = response.id.toString()
                        binding.tvResultStatus.visible()
                        binding.updateGroup.gone()
                        binding.cvTestResultList.visible()
                        if (response.sample?.noOfSampleCollected != null) {
                            binding.value2.text =
                                response.sample.noOfSampleCollected.toString()
                        } else {
                            binding.value2.text = "------"
                        }
                        if (!response.sample?.sampleSubmittedDate.isNullOrEmpty()) {
                            binding.value3.text = response.sample?.sampleSubmittedDate
                        } else {
                            binding.value3.text = "------"
                        }
                        if (!response.sample?.sampleSubmittedDate.isNullOrEmpty()) {
                            binding.value4.text = response.sample?.sampleSubmittedDate
                        } else {
                            binding.value4.text = "------"
                        }
                        if (!response.labResult?.isDentalFlurosisFound.isNullOrBlank()) {
                            binding.value5.text = response.labResult?.isDentalFlurosisFound
                        } else {
                            binding.value5.text = "------"
                        }
                        if (!response.sample?.isSampleCollected.isNullOrBlank()) {
                            binding.value6.text = response.sample?.isSampleCollected
                        } else {
                            binding.value6.text = "------"
                        }
                        if (!response.labResult?.result.isNullOrBlank()) {
                            binding.value7.text = response.labResult?.result
                            binding.value7.isClickable = false
                            binding.value7.setTextColor(Color.parseColor("#000000"))
                        }
                        }
                    }
                }
        })
    }


    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.CITIZEN_INFO)) {
                val type = object : TypeToken<HouseHoldDataItem>() {}.type
                individualMappingViewModel.citizenInfo =
                    Gson().fromJson(it.getString(AppDefines.CITIZEN_INFO), type)
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(binding.sampleCountSpinner) == true) {
            binding.btnContinue.let {
                it.isFocusable = true
                it.isClickable = true
                it.isEnabled = true
                context?.resources?.getColor(R.color.button_dark_blue)
                    ?.let { it1 -> it.setBackgroundColor(it1) }
            }
            if (data != "Select") {
                sampleCount = data.toInt()
            }

        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }


}