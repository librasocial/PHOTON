package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.os.Bundle
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
import com.ssf.homevisit.healthWellnessSurveillance.data.CovidRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.Response
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.CovidViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CovidFragment : Fragment(), AdapterView.OnItemSelectedListener {


    private lateinit var binding: com.ssf.homevisit.databinding.FragmentCovidBinding
    private val viewModel: CovidViewModel by viewModels()
    private val individualSelectionViewModel: IndividualSelectionViewModel by activityViewModels()
    private var wasPreviouslyDiagnosed: Boolean? = null
    private var vaccineType: String = ""
    private var noOfDoses: String = ""
    private lateinit var currentDate: String
    private lateinit var citizenUuid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_covid, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        initObserver()
        initClick()
        setData()
    }

    private fun setData() {
        individualSelectionViewModel.individualDetailPropertyData?.let { data ->
            citizenUuid = data.uuid.toString()
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
            } else {
                binding.ecHealthId.gone()
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

    private fun initClick() {
        binding.btnContinue.setOnClickListener {
            binding.progress.visible()
            viewModel.saveCovidData(
                requestData = CovidRequestData(
                    citizenId = citizenUuid,
                    noOfDoses = noOfDoses,
                    vaccineType = vaccineType,
                    wasPreviouslyDiagnosed = wasPreviouslyDiagnosed.toString()
                )
            )
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

    private fun initUi(response: Response) {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.US)
        currentDate = sdf.format(Date())
        binding.tvCurrentDate.text = currentDate.split("T").first()
        response.formItems.forEach {
            if (it.groupName == "Select the vaccine type") {
                val dataVaccineType: MutableList<String> = mutableListOf()
                dataVaccineType.add("Select")
                it.elements.forEach {
                    dataVaccineType.add(it.title)
                }
                binding.spinnerVaccine.onItemSelectedListener = this
                context?.let { context ->
                    val chooseDefectAdapter = ArrayAdapter(
                        context, R.layout.spinner_single_item, dataVaccineType
                    )
                    chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerVaccine.adapter = chooseDefectAdapter

                }
            }
            if (it.groupName == "Select the number of doses completed") {
                val dataNumberOfDose: MutableList<String> = mutableListOf()
                dataNumberOfDose.add("Select")
                it.elements.forEach {
                    dataNumberOfDose.add(it.title)
                }
                    binding.spinnerNumberOfDoses.onItemSelectedListener = this
                    context?.let { context ->
                        val chooseDefectAdapter = ArrayAdapter(
                            context, R.layout.spinner_single_item, dataNumberOfDose
                        )
                        chooseDefectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        binding.spinnerNumberOfDoses.adapter = chooseDefectAdapter
                    }
                }
            }

        binding.yesRadioButtonLeprosyConfirmed.setOnClickListener {
            wasPreviouslyDiagnosed = true
            if (noOfDoses != "" && vaccineType != "") {
                binding.btnContinue.let {
                    it.isFocusable = true
                    it.isClickable = true
                    it.isEnabled = true
                    context?.resources?.getColor(R.color.button_dark_blue)
                        ?.let { it1 -> it.setBackgroundColor(it1) }
                }
            }
            }
            binding.noRadioButtonLeprosyConfirmed.setOnClickListener {
                wasPreviouslyDiagnosed = false
                if (noOfDoses != "" && vaccineType != "") {
                    binding.btnContinue.let {
                        it.isFocusable = true
                        it.isClickable = true
                        it.isEnabled = true
                        context?.resources?.getColor(R.color.button_dark_blue)
                            ?.let { it1 -> it.setBackgroundColor(it1) }
                    }
                }
            }
        }

    private fun getData() {
        individualSelectionViewModel.getIndividualSurveillanceData(AppDefines.HealthAndWellness_IndividualLevel_Covid)
    }

    private fun initObserver() {
        viewModel.saveDataResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            binding.progress.gone()
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
        })
        individualSelectionViewModel.individualSurveillanceData.observe(viewLifecycleOwner,
            androidx.lifecycle.Observer { response ->
                if (response != null) {
                    initUi(response)
                }
            })
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(binding.spinnerVaccine) == true) {
            if (data != "Select") {
                vaccineType = data
            }
        }
        if (p0?.equals(binding.spinnerNumberOfDoses) == true) {
            if (data != "Select") {
                noOfDoses = data
            }
            if (wasPreviouslyDiagnosed != null) {
                binding.btnContinue.let {
                    it.isFocusable = true
                    it.isClickable = true
                    it.isEnabled = true
                    context?.resources?.getColor(R.color.button_dark_blue)
                        ?.let { it1 -> it.setBackgroundColor(it1) }
                }
            }
        }
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
    }
}