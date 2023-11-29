package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

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
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentAcfBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.AcfRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.AcfSample
import com.ssf.homevisit.healthWellnessSurveillance.data.Response
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.AcfViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AcfFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var binding: FragmentAcfBinding
    private val viewModel: AcfViewModel by viewModels()
    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    var calendar: Calendar = Calendar.getInstance()
    private var hadTreatedForTb: String = ""
    private var hadTbSymptoms: String = ""
    private var tbSymptom: String = ""
    private var isReferredToPhc: String = ""
    private var hasDiabetes: String = ""
    private var hasSputumCollected: String = ""
    private var centerName: String = ""
    private var remarks: String = ""
    private lateinit var currentDate:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_acf, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initClick()
        initObserver()
        setData()
    }

    private fun getLabReport() {
        viewModel.getData(individualSelectionViewModel.citizenUuid)
    }

    private fun setData() {
        individualSelectionViewModel.individualDetailPropertyData?.let { data ->
            individualSelectionViewModel.citizenUuid = data.uuid.toString()
            getLabReport()
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
                val x = "0${dayOfMonth}"
                updateDateInView(year, monthOfYear, x)
            } else {
                updateDateInView(year, monthOfYear, dayOfMonth.toString())
            }
        }
    }

    private fun updateDateInView(year: Int, monthOfYear: Int, dayOfMonth: String) {
        val date = "$year-$monthOfYear-$dayOfMonth"
        binding.tvDateOfSample.text = date
    }


    private fun initClick() {
        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            val acfSample: MutableList<AcfSample> = mutableListOf()
            acfSample.add(
                AcfSample(
                    remarks = remarks,
                )
            )
            viewModel.saveAcfData(
                requestData = AcfRequestData(
                    citizenId = individualSelectionViewModel.citizenUuid,
                    dateOfSurveillance = currentDate,
                    hasTBSymptoms = hadTbSymptoms,
                    tbSymptom = tbSymptom,
                    isReferredToPhc = isReferredToPhc,
                    wasTreatedForTBInPast = hadTreatedForTb,
                    sample = acfSample,
                    hasDiabetes = hasDiabetes
                )
            )
        }
        binding.value8.setOnClickListener {
            findNavController().navigate(R.id.acfUpdateFragment)
        }
        binding.etRemarks.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                remarks = p0.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })


        binding.tvCancel.setOnClickListener {
            apiSuccessFailureDialog(titleText = "Would you like to exit without saving?",
                buttonText = "Exit",
                isSuccess = false,
                failureButtonCLick = {
                    findNavController().popBackStack(R.id.individualSelectionFragment,false)
                })
        }
    }

    private fun getData() {
        individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_ActiveCaseFinding)
    }

    private fun initUi(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        binding.tvCurrentDateSample.text = currentDate.split("T").first()

        response.formItems.forEach {
            if (it.groupName == "Does this person have Diabetes") {
                val diabetesData: MutableList<String> = mutableListOf()
                diabetesData.add("Select")
                it.elements.forEach {
                    diabetesData.add(it.title)
                }
                binding.spinnerDiabetes.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, diabetesData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerDiabetes.adapter = chooseDefectAdapter

                }

            }
            if (it.groupName == "Has the sputum been collected for TB testing?") {
                val sputumCollectedData: MutableList<String> = mutableListOf()
                sputumCollectedData.add("Select")
                it.elements.forEach {
                    sputumCollectedData.add(it.title)
                }
                binding.spinnerSputumCollected.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, sputumCollectedData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerSputumCollected.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Select the center where the sputum was sent for testing") {
                val spinnerCenter: MutableList<String> = mutableListOf()
                spinnerCenter.add("Select")
                it.elements.forEach {
                    spinnerCenter.add(it.title)
                }
                binding.spinnerCenter.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, spinnerCenter
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerCenter.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Specify the symptoms") {
                val symptomList = mutableListOf<String>()
                symptomList.add("Select")
                it.elements.forEach {
                    symptomList.add(it.title)
                }
                binding.spinnerSymptom.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, symptomList
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerSymptom.adapter = chooseDefectAdapter

                }
            }

            binding.tvCurrentDateSample.text = currentDate.split("T").first()
            binding.llSampleSubmission.setOnClickListener {
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

            binding.yesRadioButtonTbSymptoms.setOnClickListener {
                hadTbSymptoms = true.toString()
                binding.ll7.visible()
            }
            binding.noRadioButtonTbSymptoms.setOnClickListener {
                hadTbSymptoms = false.toString()
            }
            binding.yesRadioButtonTbTreated.setOnClickListener {
                hadTreatedForTb = true.toString()
            }
            binding.noRadioButtonTbTreated.setOnClickListener {
                hadTreatedForTb = false.toString()
            }
            binding.checkbox.setOnClickListener {
                isReferredToPhc = true.toString()
            }
        }
    }

    private fun initObserver() {
        viewModel.saveDataResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progress.gone()
            if (it !=null) {
                apiSuccessFailureDialog(
                    isSuccess = true,
                    successButtonClick = {
                        individualSelectionViewModel.surveillanceId = it.surveillanceId.toString()
                        findNavController().navigate(R.id.AcfFragment)
                    },
                    titleText = "Form has been saved successfully!",
                    buttonText = "Click here to Continue"
                )
            } else {
                apiSuccessFailureDialog(isSuccess = false, failureButtonCLick = {
                    activity?.finish()
                })
            }
        })
        individualSelectionViewModel.individualSurveillanceData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response != null) {
                    initUi(response)
                }
            })

        viewModel.acfData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (it.data.isNotEmpty()) {
                    it.data.first().let { response ->
                        individualSelectionViewModel.surveillanceId= response.id.toString()
                        binding.tvResultStatus.visible()
                        binding.grpTb.gone()
                        binding.cvTestResultList.visible()
                        if (response.wasTreatedForTBInPast != null) {
                            binding.value2.text = response.wasTreatedForTBInPast
                        } else {
                            binding.value2.text = "--"
                        }
                        if (!response.sample.isNullOrEmpty()) {
                            if (response.sample.first().sampleCollectedDate?.isNotEmpty() == true) {
                                binding.value3.text = response.sample.first().sampleCollectedDate
                            } else {
                                binding.value3.text = "--"
                            }
                        }
                        if (!response.sample.isNullOrEmpty()) {
                            if (response.sample.first().sampleSubmittedDate?.isNotEmpty() == true) {
                                binding.value4.text = response.sample.first().sampleSubmittedDate
                            } else {
                                binding.value4.text = "--"
                            }
                        }
                        if (!response.labResult?.dmcTestResult.isNullOrBlank()) {
                            binding.value5.text = response.labResult?.dmcTestResult
                        } else {
                            binding.value5.text = "--"
                        }
                        if (!response.labResult?.naatTestResult.isNullOrBlank()) {
                            binding.value6.text = response.labResult?.naatTestResult
                        } else {
                            binding.value6.text = "--"
                        }
                        if (!response.labResult?.chestXRayTestResult.isNullOrBlank()) {
                            binding.value7.text = response.labResult?.chestXRayTestResult
                        } else {
                            binding.value7.text = "--"
                        }
                        if (response.labResult?.result?.isNotBlank() == true) {
                            binding.value8.text = response.labResult?.result
                            binding.value8.isClickable = false
                            binding.value8.setTextColor(Color.parseColor("#000000"))
                        }
                    }
                }
                }

        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(binding.spinnerDiabetes) == true) {
            hasDiabetes = data
            if (data == "Yes") {
                hasDiabetes = "true"
            } else {
                hasDiabetes = "false"
            }
            if (hasSputumCollected.isNotEmpty() && centerName.isNotEmpty()) {
                binding.btnContinue.let {
                    it.isFocusable = true
                    it.isClickable = true
                    it.isEnabled = true
                    context?.resources?.getColor(R.color.button_dark_blue)
                        ?.let { it1 -> it.setBackgroundColor(it1) }
                }
            }
        }
        if (p0?.equals(binding.spinnerSputumCollected) == true) {
            hasSputumCollected = data
            if (hasDiabetes.isNotEmpty() && centerName.isNotEmpty()) {
                binding.btnContinue.let {
                    it.isFocusable = true
                    it.isClickable = true
                    it.isEnabled = true
                    context?.resources?.getColor(R.color.button_dark_blue)
                        ?.let { it1 -> it.setBackgroundColor(it1) }
                }
            }
        }
        if (p0?.equals(binding.spinnerCenter) == true) {
            centerName = data
            if (hasSputumCollected.isNotEmpty() && hasDiabetes.isNotEmpty()) {
                binding.btnContinue.let {
                    it.isFocusable = true
                    it.isClickable = true
                    it.isEnabled = true
                    context?.resources?.getColor(R.color.button_dark_blue)
                        ?.let { it1 -> it.setBackgroundColor(it1) }
                }
            }
        }
        if (p0?.equals(binding.spinnerSymptom) == true) {
            tbSymptom = data
        }

    }
}