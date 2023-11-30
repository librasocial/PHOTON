package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.app.DatePickerDialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentBloodSmearBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.BloodSmearRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.BloodSmearSample
import com.ssf.homevisit.healthWellnessSurveillance.data.Response
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.BloodSmearViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class BloodSmearFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentBloodSmearBinding
    private val viewModel: BloodSmearViewModel by viewModels()
    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private var slideNumber: String? = ""
    private var sampleHOur:String=""
    private var sampleMin:String=""
    private var permanentAddress: String? = ""
    var calendar: Calendar = Calendar.getInstance()
    var referralToPhc: String? = null
    private var tabletCount: String = ""
    private var bloodSampleCollectionDuration: String? = ""
    private var feverDuration: String = ""
    private var rtdResult: String? = ""
    private var sampleDetails: String = ""
    lateinit var flowType: String
    private var noonData:String=""
    private lateinit var currentDate: String
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_blood_smear, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
        initObserver()
        setData()
    }

    private fun getLabReportData() {
        if (individualSelectionViewModel.bloodSmearType != null) {
            if (individualSelectionViewModel.bloodSmearType == AppDefines.IndividualLevel_BloodSmear_Filaria) {
                individualSelectionViewModel.bloodSmearType = null
                context?.let {
                    binding.chipFilaria.chipBackgroundColor =
                        ColorStateList.valueOf(ContextCompat.getColor(it, R.color.orange_dark))
                }
                binding.chipFilaria.isEnabled = false
                flowType = AppDefines.IndividualLevel_BloodSmear_Filaria
                getData(flowType)
                individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_BloodSmear_Malaria)
                binding.tvSelectHousehold.text = "Enter Details for Filaria"
                binding.cvMalaria.gone()
                binding.cvFilaria.visible()
                binding.tvSelectPlace.text = "Enter Details for Filaria"
                binding.chipMalaria.gone()
                viewModel.getLabResultsData(individualSelectionViewModel.citizenUuid)
            } else {
                individualSelectionViewModel.bloodSmearType = null
                context?.let {
                    binding.chipMalaria.chipBackgroundColor =
                        ColorStateList.valueOf(ContextCompat.getColor(it, R.color.orange_dark))
                }
                binding.chipMalaria.isEnabled = false
                flowType = AppDefines.IndividualLevel_BloodSmear_Malaria
                getData(flowType)
                individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_BloodSmear_Malaria)
                binding.tvSelectHousehold.text = "Enter Details for Malaria"
                binding.cvMalaria.visible()
                binding.cvFilaria.gone()
                binding.tvSelectPlace.text = "Enter Details for Malaria"
                binding.chipFilaria.gone()
                viewModel.getLabResultsData(individualSelectionViewModel.citizenUuid)
            }
        }
    }

    private fun setData() {
        individualSelectionViewModel.individualDetailPropertyData?.let { data ->
            individualSelectionViewModel.citizenUuid = data.uuid.toString()
            getLabReportData()
            data.imageUrls?.let {
                if (it.isEmpty()) {
                    binding.ecPhoto.background =
                        context?.resources?.getDrawable(R.drawable.ic_image_place_holder)
                } else {
                    Picasso.get().load(it[0]).resize(100, 100).into(binding.ecPhoto)
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
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(binding.spinnerBloodCollectedMalaria) == true) {
            if (data != "Select") {
                bloodSampleCollectionDuration = data
                tabletCount = data
            }
        }
        if (p0?.equals(binding.spinnerDuration) == true) {
            if (data != "Select") {
                noonData = data
            }
        }
        if (p0?.equals(binding.spinnerIllnessDuration) == true) {
            if (data != "Select") {
                feverDuration = data
            }
        }
        if (p0?.equals(binding.spinnerResultMalaria) == true) {
            if (data != "Select") {
                rtdResult=data
            }
        }
        if (p0?.equals(binding.spinnerSampleDetailsMalaria) == true) {
            if (data != "Select") {
                sampleDetails=data
            }
        }
        if (p0?.equals(binding.spinnerSampleCollectedFilaria) == true) {
            if (data != "Select") {
                sampleDetails = data
            }
        }
        if (p0?.equals(binding.spinnerLongFilaria) == true) {
            if (data != "Select") {
                feverDuration = data
            }
        }
        if (p0?.equals(binding.spinnerTabletCountMalaria) == true) {
            if (data != "Select") {
                tabletCount = data
            }
        }

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private val dateSetListener = object : DatePickerDialog.OnDateSetListener {
        override fun onDateSet(
                view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int
        ) {
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
    }

    private fun updateDateInView(year: Int, monthOfYear: Int, dayOfMonth: String) {
        val date = "$year-$monthOfYear-$dayOfMonth"
        binding.tvDateOfSample.text = date
        binding.tvDateOfSampleFilaria.text = date
    }

    private fun initClick() {
        binding.chipMalaria.setOnClickListener {
            context?.let {
                binding.chipMalaria.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(it, R.color.orange_dark))
            }
            flowType = AppDefines.IndividualLevel_BloodSmear_Malaria
            getData(flowType)
            individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_BloodSmear_Malaria)
            binding.tvSelectHousehold.text = "Enter Details for Malaria"
            binding.cvMalaria.visible()
            binding.cvFilaria.gone()
            binding.tvSelectPlace.text = "Enter Details for Malaria"
            binding.chipFilaria.gone()
        }
        binding.tvCancel.setOnClickListener {
            apiSuccessFailureDialog(titleText = "Would you like to exit without saving?",
                buttonText = "Exit",
                isSuccess = false,
                failureButtonCLick = {
                    findNavController().popBackStack(R.id.individualSelectionFragment,false)
                })
        }
        binding.chipFilaria.setOnClickListener {
            context?.let { context ->
                binding.chipFilaria.chipBackgroundColor =
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.orange_dark))
            }
            binding.tvSelectPlace.text = "Enter Details for Filaria"
            flowType = AppDefines.HealthAndWellness_IndividualLevel_BloodSmear_Filaria
            getData(flowType)
            individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_BloodSmear_Filaria)
            binding.tvSelectHousehold.text = "Enter Details for Filaria"
            binding.cvMalaria.gone()
            binding.cvFilaria.visible()
            binding.chipMalaria.gone()
        }
        binding.value7.setOnClickListener {
            findNavController().navigate(R.id.bloodSmearUpdateFragment)
        }
        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            viewModel.saveBloodSmearData(
                requestData = BloodSmearRequestData(
                    citizenId = individualSelectionViewModel.citizenUuid,
                    dateOfSurveillance = null,
                    permanentAddress = permanentAddress,
                    isReferredToPhc = referralToPhc,
                    sample = BloodSmearSample(
                        slideNo = slideNumber,
                        isSampleCollected = "",
                        sampleCollectedDate = currentDate,
                        feverStartDuration = feverDuration,
                        numberOf4AQProvided = tabletCount,
                        sampleSubmittedDate = null,
                        sampleType = "",
                        labReceivedDate = null,
                        rdtResult = rtdResult,
                        labResult = null
                    )
                )
            )
        }
    }

    private fun getData(flowType:String) {
        individualSelectionViewModel.getIndividualSurveillanceData(flowType)
    }

    private fun initUiFilaria(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDateFilaria.text = currentDate.split("T").first()
        val hourFilter = InputFilter { source, start, end, dest, dstart, dend ->
            try {
                val input = (dest.toString() + source.toString()).toInt()

                if (input in 0..12)
                    return@InputFilter null
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            ""
        }
        binding.tvHourFilaria.filters= arrayOf(hourFilter)
        binding.tvHourFilaria.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sampleHOur = s.toString()
            }

            override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        val minFilter = InputFilter { source, start, end, dest, dstart, dend ->
            try {
                val input = (dest.toString() + source.toString()).toInt()

                if (input in 0..60)
                    return@InputFilter null
            } catch (e: NumberFormatException) {
                e.printStackTrace()
            }

            ""
        }
        binding.tvMinFilaria.filters= arrayOf(minFilter)
        binding.tvMinFilaria.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                sampleMin = s.toString()
            }

            override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.etPermanentAddress.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                permanentAddress = s.toString()
            }

            override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.checkboxReferralFilaria.setOnClickListener {
            referralToPhc = true.toString()
        }
        binding.tvCurrentDate.text = currentDate.split("T").first()
        binding.tvCurrentDateSampleFilaria.text = currentDate.split("T").first()

        response.formItems.forEach {
            if (it.groupName == "Has the blood sample been collected for testing?") {
                val sampleCollectedFilariaData = mutableListOf<String>()
                sampleCollectedFilariaData.add("Select")
                it.elements.forEach {
                    sampleCollectedFilariaData.add(it.title)
                }
                binding.spinnerSampleCollectedFilaria.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, sampleCollectedFilariaData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerSampleCollectedFilaria.adapter = chooseDefectAdapter

                }
            }
            if(it.groupName== "Time when the sample was collected"){
                val noonTime = mutableListOf<String>()
                noonTime.add("Select")
                it.elements.forEach {
                    noonTime.add(it.title)
                }
                binding.spinnerDuration.onItemSelectedListener = this
                context?.let {
                    val chooseDefectAdapter = ArrayAdapter(
                            it, R.layout.spinner_single_item, noonTime
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerDuration.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "How long has it been since the onset of fever?") {
                val durationFilaria = mutableListOf<String>()
                durationFilaria.add("Select")
                it.elements.forEach {
                    durationFilaria.add(it.title)
                }
                binding.spinnerLongFilaria.onItemSelectedListener = this
                context?.let {
                    val chooseDefectAdapter = ArrayAdapter(
                        it, R.layout.spinner_single_item, durationFilaria
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerLongFilaria.adapter = chooseDefectAdapter

                }
            }
            }

        binding.llSampleSubmissionFilaria.setOnClickListener {
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

    private fun initUiMalaria(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        binding.etBloodSmear.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                slideNumber = s.toString()
            }

            override fun beforeTextChanged(
                s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        binding.checkboxReferralMalaria.setOnClickListener {
            referralToPhc = true.toString()
        }

        response.formItems.forEach {
            if (it.groupName == "How long has it been since the onset of fever?") {

                val malariaIllnessDuration = mutableListOf<String>()
                malariaIllnessDuration.add("Select")

                it.elements.forEach {
                    malariaIllnessDuration.add(it.title)
                }
                binding.spinnerIllnessDuration.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                            context, android.R.layout.simple_spinner_item, malariaIllnessDuration
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerIllnessDuration.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Has the blood sample been collected for testing?") {
                val bloodCollectedMalaria = mutableListOf<String>()
                bloodCollectedMalaria.add("Select")
                it.elements.forEach {
                    bloodCollectedMalaria.add(it.title)
                }
                binding.spinnerBloodCollectedMalaria.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                            context, R.layout.spinner_single_item, bloodCollectedMalaria
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerBloodCollectedMalaria.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Number of 4AQ tablets provided") {
                val tabletCountMalaria = mutableListOf<String>()
                tabletCountMalaria.add("Select")
                it.elements.forEach {
                    tabletCountMalaria.add(it.title)
                }
                binding.spinnerTabletCountMalaria.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                            context,R.layout.spinner_single_item, tabletCountMalaria
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerTabletCountMalaria.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Sample details") {
                val sampleDetailsMalaria = mutableListOf<String>()
                sampleDetailsMalaria.add("Select")
                it.elements.forEach {
                    sampleDetailsMalaria.add(it.title)
                }
                binding.spinnerSampleDetailsMalaria.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                            context, R.layout.spinner_single_item, sampleDetailsMalaria
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerSampleDetailsMalaria.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Result of RDT (if done)") {
                val resultsMalariaData = mutableListOf<String>()
                resultsMalariaData.add("Select")
                it.elements.forEach {
                    resultsMalariaData.add(it.title)
                }
                binding.spinnerResultMalaria.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                            context, R.layout.spinner_single_item, resultsMalariaData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerResultMalaria.adapter = chooseDefectAdapter

                }
            }
        }

        binding.tvCurrentDateSample.text = currentDate.split("T").first()
        binding.llSampleSubmissionDate.setOnClickListener {
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

    private fun initObserver() {
        individualSelectionViewModel.individualSurveillanceData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response != null) {
                    if (response.name == AppDefines.IndividualLevel_BloodSmear_Malaria) {
                        initUiMalaria(response)
                    }
                    if (response.name == AppDefines.IndividualLevel_BloodSmear_Filaria) {
                        initUiFilaria(response)
                    }
                }
            })
        viewModel.saveDataResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progress.gone()
            if (it != null) {
                apiSuccessFailureDialog(
                    isSuccess = true,
                    successButtonClick = {
                        if (flowType == AppDefines.IndividualLevel_BloodSmear_Malaria) {
                            individualSelectionViewModel.bloodSmearType =
                                AppDefines.IndividualLevel_BloodSmear_Malaria
                        } else {
                            individualSelectionViewModel.bloodSmearType =
                                AppDefines.IndividualLevel_BloodSmear_Filaria
                        }
                        individualSelectionViewModel.surveillanceId = it.surveillanceId.toString()
                        findNavController().navigate(R.id.bloodSmearFragment)
                    },
                    titleText = "Form has been saved successfully!",
                    buttonText = "Click here to Continue"
                )
            } else {
                apiSuccessFailureDialog(isSuccess = false, failureButtonCLick = {
                    Toast.makeText(
                        context, "Response  Saved Failed", Toast.LENGTH_SHORT
                    ).show()
                    activity?.finish()
                })
            }
        })
        viewModel.bloodSmearData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (it.data.isNotEmpty()) {
                    it.data.first().let { response ->
                        individualSelectionViewModel.surveillanceId = response.id.toString()
                        binding.tvResultStatus.visible()
                        binding.cvTestResultList.visible()
                        if (flowType == AppDefines.IndividualLevel_BloodSmear_Malaria) {
                            binding.grpMalaria.gone()
                        }
                        if (flowType == AppDefines.IndividualLevel_BloodSmear_Filaria) {
                            binding.grpFilaria.gone()
                        }
                        if (response.sample?.slideNo != null && response.sample.slideNo.isNotEmpty()) {
                            binding.value2.text = response.sample.slideNo
                        } else {
                            binding.value2.text = "    --    "
                        }
                        if (!response.sample?.sampleCollectedDate.isNullOrEmpty()) {
                            binding.value3.text = response.sample?.sampleCollectedDate?.split("T")?.first()
                        } else {
                            binding.value3.text = "    --    "
                        }
                        if (!response.sample?.sampleSubmittedDate.isNullOrBlank()) {
                            binding.value4.text = response.sample?.sampleSubmittedDate?.split("T")?.first()
                        } else {
                            binding.value4.text = "    --     "
                        }
                        if (!response.sample?.rdtResult.isNullOrBlank()) {
                            binding.value5.text = response.sample?.rdtResult
                        } else {
                            binding.value5.text = "    --     "
                        }
                        if (!response.sample?.sampleCollectedDate.isNullOrBlank()) {
                            binding.value6.text = response.sample?.sampleCollectedDate?.split("T")?.first()
                        } else {
                            binding.value6.text = "    --    "
                        }
                        if (!response.sample?.labResult?.result.isNullOrBlank()) {
                            binding.value7.text = response.sample?.labResult?.result
                            binding.value7.isClickable = false
                            binding.value7.setTextColor(Color.parseColor("#000000"))
                        }
                    }

                }
            }
        })
    }

}


