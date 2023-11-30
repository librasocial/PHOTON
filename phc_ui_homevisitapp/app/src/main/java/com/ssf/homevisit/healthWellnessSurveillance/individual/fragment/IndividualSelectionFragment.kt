package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.IndividualDetailProperty
import com.ssf.homevisit.healthWellnessSurveillance.individual.adapter.CitizenAdapter
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSelectionViewModel
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndividualSelectionFragment : Fragment(), CitizenAdapter.OnCitizenClick {

    private lateinit var binding: com.ssf.homevisit.databinding.IndividualSelectionBinding
    private val viewModel: IndividualSelectionViewModel by activityViewModels()
    private var currentIndex = 0
    private var houseHoldData: MutableList<IndividualDetailProperty> = mutableListOf()
    private lateinit var citizenAdapter: CitizenAdapter
    private var citizenUuidList: MutableList<String> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.individual_selection, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initAdapter()
        getData()
        initClick()
        initObserver()
        setData()
    }

    private fun initClick() {
        binding.tvBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun getLabResultStatus(citizenUuidList: MutableList<String>) {
        when (viewModel.surveillanceType) {
            AppDefines.LEPROSY -> {
                viewModel.getLabResultStatus(AppDefines.LEPROSY_LAB_RESULT_END_URL, citizenUuidList)
            }
            AppDefines.BLOOD_SMEAR -> {
                viewModel.getLabResultStatus(
                    AppDefines.BLOOD_SMEAR_LAB_RESULT_END_URL, citizenUuidList
                )
            }
            AppDefines.ACTIVE_CASE_FINDING -> {
                viewModel.getLabResultStatus(AppDefines.ACF_LAB_RESULT_END_URL, citizenUuidList)
            }
            AppDefines.URINE_SAMPLE -> {
                viewModel.getLabResultStatus(AppDefines.URINE_LAB_RESULT_END_URL, citizenUuidList)
            }
        }
    }

    private fun initObserver() {
        viewModel.citizensInHousehold.observe(viewLifecycleOwner, Observer{ response ->
            houseHoldData = response
            citizenUuidList = mutableListOf()
            response.forEach {
                it.uuid?.let { it1 -> citizenUuidList.add(it1) }
            }
            if (response.size > 0) {
                currentIndex = 0
                getImageUrl()
            }
        })
        viewModel.labResultStatus.observe(viewLifecycleOwner,Observer{ response ->
            binding.progress.gone()
            if (response?.isNotEmpty() == true) {
                var citizenIdFound: Boolean = false
                houseHoldData.forEach { individualDetail ->
                    response.forEach { labResultResponse ->
                        if (labResultResponse?.citizenId == individualDetail.uuid) {
                            citizenIdFound = true
                            if (labResultResponse?.result == null) {
                                individualDetail.labStatus = AppDefines.PENDING_STATUS
                            } else {
                                individualDetail.labStatus = labResultResponse.result
                                individualDetail.labStatusDate = labResultResponse.dateModified
                            }

                        }
                    }
                    if (!citizenIdFound) {
                        individualDetail.labStatus = null
                    }
                    citizenIdFound = false
                }
            }
            citizenAdapter.setData(houseHoldData)
            citizenAdapter.notifyDataSetChanged()
        })
        viewModel._placeDetails.observe(viewLifecycleOwner,Observer{
            if (currentIndex < houseHoldData.size) {
                var imageUrl: MutableList<String> = mutableListOf()
                if (houseHoldData[currentIndex].imageUrls?.isNotEmpty() == true) {
                    imageUrl = houseHoldData[currentIndex].imageUrls as MutableList<String>
                    imageUrl[0] = it
                } else {
                    imageUrl.add(0, "")
                }
                currentIndex += 1
                if (currentIndex < houseHoldData.size) {
                    getImageUrl()
                } else {
                    if (viewModel.surveillanceType == AppDefines.IDSP_S_Form || viewModel.surveillanceType == AppDefines.COVID) {
                        binding.progress.gone()
                        citizenAdapter.setData(houseHoldData)
                        citizenAdapter.notifyDataSetChanged()
                    } else {
                        getLabResultStatus(citizenUuidList)
                    }
                }
            }

        })
    }

    private fun getImageUrl() {
        while (houseHoldData[currentIndex].imageUrls.isNullOrEmpty()) {
            houseHoldData[currentIndex].imageUrls = emptyList()
            currentIndex += 1
            if (currentIndex >= houseHoldData.size) {
                break
            }
        }
        if (currentIndex < houseHoldData.size) {
            if (houseHoldData[currentIndex].imageUrls?.isNotEmpty() == true) {
                for (url in houseHoldData[currentIndex].imageUrls!!) {
                    if (url.isNotEmpty()) {
                        viewModel.getImageUrl(url)
                        break
                    }
                }
            }
        } else {
            if (viewModel.surveillanceType == AppDefines.IDSP_S_Form || viewModel.surveillanceType == AppDefines.COVID) {
                binding.progress.gone()
                citizenAdapter.setData(houseHoldData)
                citizenAdapter.notifyDataSetChanged()
            } else {
                getLabResultStatus(citizenUuidList)
            }
        }
    }

    private fun setData() {
        when (viewModel.surveillanceType) {
            AppDefines.IDSP_S_Form -> {
                binding.tvSelectPlace.text = "Select An Individual for IDSP S-Form"
                binding.tvSelectHousehold.text = "Select An Individual for IDSP S-Form"
            }
            AppDefines.LEPROSY -> {
                binding.tvSelectPlace.text = "Select An Individual for Leprosy Surveillance"
                binding.tvSelectHousehold.text = "Select An Individual for Leprosy Surveillance"
            }
            AppDefines.COVID -> {
                binding.tvSelectPlace.text = "Select An Individual for COVID Surveillance"
                binding.tvSelectHousehold.text = "Select An Individual for COVID Surveillance"
            }
            AppDefines.BLOOD_SMEAR -> {
                binding.tvSelectPlace.text = "Select An Individual for blood Smear Testing"
                binding.tvSelectHousehold.text = "Select An Individual for blood Smear Testing"
            }
            AppDefines.ACTIVE_CASE_FINDING -> {
                binding.tvSelectPlace.text = "Select an Individual for Active Case Finding"
                binding.tvSelectHousehold.text = "Select an Individual for Active Case Finding"
            }
            AppDefines.URINE_SAMPLE -> {
                binding.tvSelectPlace.text = "Select An Individual for Urine Sample Details"
                binding.tvSelectHousehold.text = "Select An Individual for Urine Sample Details"
            }
        }
    }

    private fun getData() {
        binding.progress.visible()
        viewModel.getCitizensInHouse(viewModel.houseUuid)
    }

    private fun initAdapter() {
        context?.let {
            citizenAdapter = CitizenAdapter(mutableListOf(), this, it)
        }
        binding.rvIndividual.adapter = citizenAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        currentIndex=0
        houseHoldData= mutableListOf()
    }
    private fun initArguments() {
        arguments?.let {
            if (it.containsKey(AppDefines.SURVEILLANCE_TYPE)) {
                viewModel.surveillanceType = it.getString(AppDefines.SURVEILLANCE_TYPE).toString()
            }
            if (it.containsKey(AppDefines.HOUSE_UUID)) {
                viewModel.houseUuid = it.getString(AppDefines.HOUSE_UUID).toString()
            }
        }
    }

    override fun onCitizenClick(data: IndividualDetailProperty) {
        val bundle = Bundle()
        bundle.putString(AppDefines.SURVEILLANCE_TYPE, viewModel.surveillanceType)
        viewModel.individualDetailPropertyData=data
        when (viewModel.surveillanceType) {
            AppDefines.ACTIVE_CASE_FINDING -> {
                findNavController().navigate(R.id.AcfFragment, bundle)
            }
            AppDefines.LEPROSY -> {
                findNavController().navigate(R.id.leprosyFragment, bundle)
            }
            AppDefines.COVID -> {
                findNavController().navigate(R.id.covidFragment, bundle)
            }
            AppDefines.IDSP_S_Form -> {
                findNavController().navigate(R.id.idspFragment, bundle)
            }
            AppDefines.BLOOD_SMEAR -> {
                findNavController().navigate(R.id.bloodSmearFragment, bundle)
            }
            AppDefines.URINE_SAMPLE -> {
                findNavController().navigate(R.id.urineFragment, bundle)
            }
        }
    }

}