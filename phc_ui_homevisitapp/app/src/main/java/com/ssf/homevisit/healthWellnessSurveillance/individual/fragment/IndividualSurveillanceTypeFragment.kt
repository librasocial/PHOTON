package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.IndividualSurveillanceTypeBinding
import com.ssf.homevisit.healthWellnessSurveillance.LarvaViewModel
import com.ssf.homevisit.healthWellnessSurveillance.individual.adapter.IndividualAssetAdapter
import com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel.IndividualSurveillanceTypeViewModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndividualSurveillanceTypeFragment : Fragment(), IndividualAssetAdapter.OnItemClick {

    private val viewModel: IndividualSurveillanceTypeViewModel by viewModels()
    private val larvaViewModel: LarvaViewModel by activityViewModels()
    private lateinit var individualAssetAdapter: IndividualAssetAdapter
    private lateinit var binding: IndividualSurveillanceTypeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.individual_surveillance_type, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initClick()
        getData()
        setData()
    }

    private fun initClick() {
        binding.backButton.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setData() {
        binding.tvSelectSt.text = "Select Surveillance Type - ${larvaViewModel.villageName}"
        binding.tvSelectPlace.text = "Select Surveillance Type - ${larvaViewModel.villageName}"
    }

    private fun getData() {
        viewModel.getSurveillanceTypeData()
        individualAssetAdapter.setData(viewModel.surveillanceTypeData)
    }


    private fun initAdapter() {
        context?.let {
            individualAssetAdapter = IndividualAssetAdapter(mutableListOf(), this, it)
            binding.rvSelectSt.adapter = individualAssetAdapter
        }

    }

    override fun onItemClick(
        data: Asset
    ) {
        val bundle = Bundle()
        bundle.putString(AppDefines.SURVEILLANCE_TYPE, data.title)
        findNavController().navigate(R.id.individualMappingFragment, bundle)
    }


}