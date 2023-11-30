package com.ssf.homevisit.fragment

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.NaturalResourceVSEAdapter
import com.ssf.homevisit.databinding.FragmentNaturalResourceBinding
import com.ssf.homevisit.extensions.apiSuccessFailureDialog
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.common.BaseFragment
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.collectSharedFlowData
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.NaturalResourceDataItem
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.viewmodel.NaturalResourceViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NaturalResourceVSEFragment:
        BaseFragment<FragmentNaturalResourceBinding>(R.layout.fragment_natural_resource),
        NaturalResourceVSEAdapter.OnItemClick, AdapterView.OnItemSelectedListener {

    private lateinit var adapter: NaturalResourceVSEAdapter
    private val viewModel: NaturalResourceViewModel by activityViewModels()
    private val mainViewModel: LarvaViewModel by activityViewModels()
    private var districtList = mutableListOf<com.ssf.homevisit.healthWellnessSurveillance.data.Target>()
    private var stateList = mutableListOf<com.ssf.homevisit.healthWellnessSurveillance.data.Target>()
    private var gramList = mutableListOf<com.ssf.homevisit.healthWellnessSurveillance.data.Target>()
    private var talukList = mutableListOf<com.ssf.homevisit.healthWellnessSurveillance.data.Target>()

    override fun setDataCollector() {
        collectSharedFlowData(viewModel.districtByStateData) {
            when (it) {
                is DataState.Error -> {}
                DataState.Loading -> {}
                is DataState.Success -> {
                    fragmentBinding.spinnerDistrict.onItemSelectedListener = this
                    context?.let { context ->
                        districtList = mutableListOf()
                        it.baseResponseData.data.forEach {
                            districtList.add(it)
                        }
                        val districtAdapter = ArrayAdapter(
                                context, android.R.layout.simple_spinner_item, districtList.map { it.properties.name }
                        )
                        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        fragmentBinding.spinnerDistrict.adapter = districtAdapter
                    }
                    fragmentBinding.spinnerDistrict.setSelection(viewModel.selectedDistrictPosition)
                }
            }
        }
        collectSharedFlowData(viewModel.gramByTalukData) {
            when (it) {
                is DataState.Error -> {}
                DataState.Loading -> {}
                is DataState.Success -> {
                    fragmentBinding.spinnerGram.onItemSelectedListener = this
                    context?.let { context ->
                        gramList = mutableListOf()
                        it.baseResponseData.data.forEach {
                            gramList.add(it)
                        }
                        val gramAdapter = ArrayAdapter(
                                context, android.R.layout.simple_spinner_item, gramList.map { it.properties.name }
                        )
                        gramAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        fragmentBinding.spinnerGram.adapter = gramAdapter
                    }
                    fragmentBinding.spinnerGram.setSelection(viewModel.selectedGramPanchayatPosition)
                }
            }
        }
        collectSharedFlowData(viewModel.stateByCountryData) {
            when (it) {
                is DataState.Error -> {}
                DataState.Loading -> {}
                is DataState.Success -> {
                    fragmentBinding.spinnerState.onItemSelectedListener = this
                    stateList = mutableListOf()
                    context?.let { context ->
                        it.baseResponseData.data.forEach {
                            stateList.add(it)
                        }
                        val stateAdapter = ArrayAdapter(
                                context, android.R.layout.simple_spinner_item, stateList.map { it.properties.name }
                        )
                        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        fragmentBinding.spinnerState.adapter = stateAdapter
                    }
                    fragmentBinding.spinnerState.setSelection(viewModel.selectedStatePosition)
                }
            }
        }
        collectSharedFlowData(viewModel.talukByDistrictData) {
            when (it) {
                is DataState.Error -> {}
                DataState.Loading -> {}
                is DataState.Success -> {
                    fragmentBinding.spinnerTaluk.onItemSelectedListener = this
                    context?.let { context ->
                        talukList = mutableListOf()
                        it.baseResponseData.data.forEach {
                            talukList.add(it)
                        }
                        val talukAdapter = ArrayAdapter(
                                context, android.R.layout.simple_spinner_item, talukList.map { it.properties.name }
                        )
                        talukAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        fragmentBinding.spinnerTaluk.adapter = talukAdapter
                    }
                    fragmentBinding.spinnerTaluk.setSelection(viewModel.selectedTalukPosition)
                }
            }
        }
    }

    override fun setUpBindingVariables() {
        initAdapter()
        setData()
        fetchData()
        initArguments()
        initObserver()
        initClick()
    }

    private fun initClick() {
        fragmentBinding.llBtnSave.setOnClickListener {
            apiSuccessFailureDialog(titleText = "Form has been saved successfully!",
                buttonText = "Click here to Exit",
                isSuccess = true,
                successButtonClick = {
                    requireActivity().finish()
                })
        }
        fragmentBinding.btnCancel.setOnClickListener {
            apiSuccessFailureDialog(titleText = "Would you like to exit without saving?",
                buttonText = "Exit",
                isSuccess = false,
                failureButtonCLick = {
                    requireActivity().finish()
                })
        }
    }

    private fun initObserver() {
        viewModel.naturalResourceListLiveData.observe(viewLifecycleOwner, Observer {
            if (it) {
                adapter.data = viewModel.naturalResourceList
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.RETURN_FLOW)) {
                when (it.getString(AppDefines.RETURN_FLOW)) {
                    AppDefines.PUBLIC_FACILITY -> {
                        context?.resources?.getColor(R.color.button_dark_blue)
                                ?.let { it1 -> fragmentBinding.llBtnSave.setBackgroundColor(it1) }
                        fragmentBinding.llBtnSave.isClickable = true
                    }

                }
            }
        }
    }

    private fun setData() {
        if (viewModel.selectedSurveyTypeHashMap.contains(4)) {
            context?.resources?.getColor(R.color.button_dark_blue)
                    ?.let { it1 -> fragmentBinding.llBtnSave.setBackgroundColor(it1) }
            fragmentBinding.llBtnSave.isClickable = true
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val currentDate = sdf.format(Date())
        fragmentBinding.tvCurrentDate.text = currentDate
        fragmentBinding.tvVillageName.text = mainViewModel.villageName
        fragmentBinding.tvVillageLgdCode.text = "|  (LGD code - ${mainViewModel.villageLgdCode})"
        viewModel.villageUuid = mainViewModel.villageUuid
    }

    private fun fetchData() {
        viewModel.fetchData()
        viewModel.fetchStateByCountry()
    }

    private fun initAdapter() {
        context?.let {
            adapter = NaturalResourceVSEAdapter(this, it)
            fragmentBinding.rvNaturalResourceList.adapter = adapter
        }
    }

    override fun setClickListener() {
    }

    override fun onItemClick(position: Int, naturalResourceDataItem: NaturalResourceDataItem) {
        viewModel.naturalResourceList
        viewModel.selectedStatePosition=fragmentBinding.spinnerState.selectedItemPosition
        viewModel.selectedDistrictPosition=fragmentBinding.spinnerDistrict.selectedItemPosition
        viewModel.selectedTalukPosition=fragmentBinding.spinnerTaluk.selectedItemPosition
        viewModel.selectedGramPanchayatPosition=fragmentBinding.spinnerGram.selectedItemPosition

        when (position) {
            0 -> {
                findNavController().navigate(R.id.environmentQualityFragment)
            }
            1 -> {
                findNavController().navigate(R.id.waterSourcesFragment)
            }
            2 -> {
                findNavController().navigate(R.id.villageInfraFragment)
            }
            3 -> {
                findNavController().navigate(R.id.transportModeFragment)
            }
            4 -> {
                findNavController().navigate(R.id.publicFacilityFragment)
            }
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val data = p0?.getItemAtPosition(p2).toString()
        if (p0?.equals(fragmentBinding.spinnerState) == true) {
            val selectedStateList = stateList.filter {
                it.properties.name == data
            }
            viewModel.fetchDistrictByState(selectedStateList.first().properties.uuid)
        }
        if (p0?.equals(fragmentBinding.spinnerDistrict) == true) {
            val selectedDistrictList = districtList.filter {
                it.properties.name == data
            }
            viewModel.fetchTalukByDistrict(selectedDistrictList.first().properties.uuid)
        }
        if (p0?.equals(fragmentBinding.spinnerTaluk) == true) {
            val selectedTalukList = talukList.filter {
                it.properties.name == data
            }
            viewModel.fetchGramByTaluk(selectedTalukList.first().properties.uuid)
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {}
}