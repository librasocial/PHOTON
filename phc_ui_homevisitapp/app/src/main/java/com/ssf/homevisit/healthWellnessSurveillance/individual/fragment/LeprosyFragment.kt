package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

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
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.LeprosyRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.Past
import com.ssf.homevisit.healthWellnessSurveillance.data.Response
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.LeprosyViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class LeprosyFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: com.ssf.homevisit.databinding.FragmentLeprosyBinding
    private val viewModel: LeprosyViewModel by viewModels()
    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private var newlySuspected: Boolean? = null
    private var hasUndergoneSurgery: Boolean? = null
    private var isLeprosyConfirmed: Boolean? = null
    private var result: String = ""
    private var symptoms: MutableList<String> = mutableListOf()
    private var suspectedType: String = ""
    private var isReferredToPhc: Boolean? = null
    private var currentDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_leprosy, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        setData()
        initClick()
        initObserver()
    }

    private fun getLabReportData() {
        viewModel.getLabResultsData(individualSelectionViewModel.citizenUuid)
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

    private fun initUi(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        response.formItems.forEach {
            if (it.groupName == "Specify the suspected leprosy type") {
                val suspectedLeprosyTypeData: MutableList<String> = mutableListOf()
                suspectedLeprosyTypeData.add("Select")
                it.elements.forEach { element ->
                    suspectedLeprosyTypeData.add(element.title)
                }
                binding.spinnerSuspectedLeprosyType.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, suspectedLeprosyTypeData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerSuspectedLeprosyType.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Specify the symptoms") {
                val symptomData: MutableList<String> = mutableListOf()
                symptomData.add("Select")
                it.elements.forEach {
                    symptomData.add(it.title)
                }
                binding.spinnerSpecifySymptom.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, symptomData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerSpecifySymptom.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Leprosy test result") {
                val leprosyTestData: MutableList<String> = mutableListOf()
                leprosyTestData.add("Select")
                it.elements.forEach {
                    leprosyTestData.add(it.title)
                }
                binding.spinnerLeprosyTestResults.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, leprosyTestData
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerLeprosyTestResults.adapter = chooseDefectAdapter
                }
            }
        }
    }

    private fun initClick() {
        binding.yesNewlySuspected.setOnClickListener {
            newlySuspected = true
            binding.grpForNo.gone()
        }
        binding.noNewlySuspected.setOnClickListener {
            newlySuspected = false
            binding.grpForNo.visible()
        }
        binding.yesRadioButtonSurgery.setOnClickListener {
            hasUndergoneSurgery = true
        }
        binding.noRadioButtonSurgery.setOnClickListener {
            hasUndergoneSurgery = false
        }
        binding.yesRadioButtonLeprosyConfirmed.setOnClickListener {
            isLeprosyConfirmed = true
        }
        binding.noRadioButtonLeprosyConfirmed.setOnClickListener {
            isLeprosyConfirmed = false
        }
        binding.checkbox.setOnClickListener {
            isReferredToPhc = true
            binding.btnContinue.let {
                it.isFocusable = true
                it.isClickable = true
                it.isEnabled = true
                context?.resources?.getColor(R.color.button_dark_blue)
                    ?.let { it1 -> it.setBackgroundColor(it1) }
            }
        }
        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            viewModel.saveLeprosyData(
                requestData = LeprosyRequestData(
                    citizenId = individualSelectionViewModel.citizenUuid,
                    dateOfSurveillance = null,
                    isNewlySuspected = newlySuspected.toString(),
                    suspectedType = suspectedType,
                    symptoms = symptoms,
                    isReferredToPhc = isReferredToPhc.toString(),
                    past = Past(
                        hasUndergoneRCSSurgery = hasUndergoneSurgery.toString(),
                        result = result,
                        wasConfirmed = isLeprosyConfirmed.toString()
                    )
                )
            )
        }
        binding.etOtherSymptom.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                symptoms.add(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        binding.value5.setOnClickListener {
            findNavController().navigate(R.id.leprosyUpdateFragment)
        }
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
        individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_Leprosy)
    }


    private fun initObserver() {
        viewModel.saveDataResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progress.gone()
            if (it != null) {
                apiSuccessFailureDialog(isSuccess = true, successButtonClick = {
                    Toast.makeText(
                        context, "Response  Saved Successfully", Toast.LENGTH_SHORT
                    ).show()
                    individualSelectionViewModel.surveillanceId= it.surveillanceId.toString()
                    findNavController().navigate(R.id.leprosyFragment)
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
        individualSelectionViewModel.individualSurveillanceData.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response != null) {
                    initUi(response)
                }
            })
        viewModel.leprosyData.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            it?.let {
                if (it.data.isNotEmpty()) {
                    it.data.first().let { response ->
                        individualSelectionViewModel.surveillanceId = response.id.toString()
                        binding.tvResultStatus.visible()
                        binding.cvTestResultList.visible()
                        if (response.isNewlySuspected != null) {
                            binding.value2.text = response.isNewlySuspected
                        } else {
                            binding.value2.text = "   --    "
                        }
                        if (!response.suspectedType.isNullOrEmpty()) {
                            binding.value3.text = response.suspectedType
                        } else {
                            binding.value3.text = "        --          "
                        }
                        if (!response.past?.hasUndergoneRCSSurgery.isNullOrBlank()) {
                            binding.value4.text = response.past?.hasUndergoneRCSSurgery
                        } else {
                            binding.value4.text = "         --          "
                        }
                        if (!response.labResult?.result.isNullOrBlank()) {
                            binding.tvUpdateResult.text = response.labResult?.result
                            binding.tvUpdateResult.isClickable = false
                            binding.tvUpdateResult.setTextColor(Color.parseColor("#000000"))
                        }
                        }
                    }
                }
        })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(binding.spinnerLeprosyTestResults) == true) {
            if (data != "Select") {
                result = data
            }
        }
        if (p0?.equals(binding.spinnerSpecifySymptom) == true) {
            if (data != "Select") {
                if (data == "Others (Specify)") {
                    binding.etOtherSymptom.visible()
                } else {
                    binding.etOtherSymptom.gone()
                    symptoms.add(data)
                }
            }
        }
        if (p0?.equals(binding.spinnerSuspectedLeprosyType) == true) {
            if (data != "Select") {
                suspectedType = data
            }
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
    }

}