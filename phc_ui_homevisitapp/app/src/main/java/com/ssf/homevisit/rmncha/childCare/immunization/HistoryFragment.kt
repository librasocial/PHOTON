package com.ssf.homevisit.rmncha.childCare.immunization

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ssf.homevisit.databinding.ImmunizationHistoryBinding
import com.ssf.homevisit.models.ChildMotherDetailResponse
import com.ssf.homevisit.requestmanager.AppDefines

class HistoryFragment : DialogFragment() {

    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var vaccinationStatusAdapter: VaccinationStatusAdapter
    private lateinit var binding: ImmunizationHistoryBinding
    private lateinit var childId: String
    private var vaccinationStatusList = mutableListOf<VaccinationStatus>()
    private val viewModel: HistoryViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = ImmunizationHistoryBinding.inflate(layoutInflater, container, false)
        this.isCancelable = false
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        getData()
        initAdapter()
        initClick()
        initUi()
    }

    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.CHILD_MOTHER_DETAILS)) {
                val type = object : TypeToken<ChildMotherDetailResponse>() {}.type
                viewModel.childMotherDetail =
                    Gson().fromJson(it.getString(AppDefines.CHILD_MOTHER_DETAILS), type)
                childId = viewModel.childMotherDetail.content[0].sourceNode.properties.uuid
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getData() {
        viewModel.getHistory(childId).observe(viewLifecycleOwner) {
            historyAdapter.data = it
            historyAdapter.notifyDataSetChanged()
            vaccinationStatusList = mutableListOf()
            it.let {
                if (it.containsKey("Month 9-12")) {
                    val vaccineList = it["Month 9-12"]
                    var isVaccinationDateForYear1: Boolean = true
                    vaccineList?.forEachIndexed { index, immunization ->
                        if (immunization.immunizationSubGroup.vaccinatedDate == null) {
                            isVaccinationDateForYear1 = false
                        }
                        if (index == vaccineList.lastIndex) {
                            if (isVaccinationDateForYear1) {
                                vaccinationStatusList.add(
                                    VaccinationStatus(
                                        year = "Year 1", status = true
                                    )
                                )
                            }
                        }
                    }
                    if (!isVaccinationDateForYear1) {
                        vaccinationStatusList.add(
                            VaccinationStatus(
                                year = "Year 1", status = false
                            )
                        )
                    }
                }
                if (it.containsKey("Month 16-24")) {
                    val vaccineList = it["Month 16-24"]
                    var isVaccinationDateforYearTwo: Boolean = true
                    vaccineList?.forEachIndexed { index, immunization ->
                        if (immunization.immunizationSubGroup.vaccinatedDate == null) {
                            isVaccinationDateforYearTwo = false
                        }
                        if (index == vaccineList.lastIndex) {
                            if (isVaccinationDateforYearTwo) {
                                vaccinationStatusList.add(
                                    VaccinationStatus(
                                        year = "Year 2", status = true
                                    )
                                )
                            }
                        }
                    }
                    if (!isVaccinationDateforYearTwo) {
                        vaccinationStatusList.add(
                            VaccinationStatus(
                                year = "Year 2", status = false
                            )
                        )
                    }
                }
                vaccinationStatusAdapter.vaccinationStatusList = vaccinationStatusList
                vaccinationStatusAdapter.notifyDataSetChanged()
            }
        }
        viewModel.getRMNCHACoupleDetailsLiveData(viewModel.childMotherDetail.content[0].targetNode.properties.uuid)
            .observe(viewLifecycleOwner) {
                binding.fatherName.txtHHName.text = it?.get(0)?.target?.properties?.firstName
            }
    }


    private fun initAdapter() {
        context?.let {
            historyAdapter = HistoryAdapter(it)
            binding.rvHistory.adapter = historyAdapter
            vaccinationStatusAdapter = VaccinationStatusAdapter(it)
            binding.rvYearStatus.adapter = vaccinationStatusAdapter
        }

    }

    private fun initUi() {
        binding.childName.txtHHName.text =
            viewModel.childMotherDetail.content[0].sourceNode.properties.firstName
        binding.headingMotherName.txtHHName.text =
            viewModel.childMotherDetail.content[0].targetNode.properties.firstName
    }

    private fun initClick() {
        binding.btnCancel.setOnClickListener {
            dismiss()
        }
    }


}