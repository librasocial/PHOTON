package com.ssf.homevisit.rmncha.childCare.mapping

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.FragmentChildSelectionBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.models.*
import com.ssf.homevisit.rmncha.childCare.immunization.ChildImmunizationActivity
import com.ssf.homevisit.rmncha.childCare.registration.ChildCareRegistrationActivity


class ChildSelectionFragment : Fragment() {

    private var _binding: FragmentChildSelectionBinding? = null
    private val viewModel: ChildCareViewModel by activityViewModels()
    private val binding get() = _binding!!
    private lateinit var adapter: HouseholdMemberAdapter
    private lateinit var childUuidList: MutableList<String>
    private lateinit var childInHouseContent: MutableList<ChildInHouseContent>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildSelectionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.getChildInHouseHold()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClick()
        setListAdapter()
        initObserver()
    }

    private fun initClick() {
        binding.tvBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun initObserver() {
        viewModel.childInHouseHoldLiveData.observe(viewLifecycleOwner) {
            it?.let {
                if (it.content.isNotEmpty()) {
                    childUuidList = mutableListOf()
                    childInHouseContent = it.content as MutableList<ChildInHouseContent>
                    it.content.forEach {
                        childUuidList.add(it.sourceNode.properties.uuid)
                    }
                    viewModel.getChildMotherDetail(childUuidList)
                } else {
                    binding.emptyState.root.visible()
                    binding.childrenList.gone()
                }
            }
        }
        viewModel.childMotherDetailsResponse.observe(viewLifecycleOwner) { childMotherDetailResponseContent ->
            childMotherDetailResponseContent?.let {childMotherDetailResponse ->
            childMotherDetailResponse.content.forEach { childMotherDetailContent ->
                val x =
                    childInHouseContent.filter { it.sourceNode.properties.uuid == childMotherDetailContent.sourceNode.properties.uuid }
                if (x.isNotEmpty()) {
                    Log.d("TAG", "VALUE OF X $x")
                    x[0].sourceNode.properties.motherName =
                        childMotherDetailContent.targetNode.properties.firstName
                }
            }
            binding.emptyState.root.gone()
            viewModel.setLoading(false)
            adapter.submitList(childInHouseContent)
        }
    }}

    private fun setListAdapter() {
        adapter = HouseholdMemberAdapter {
            it.sourceNode.properties.rmnchaServiceStatus.let { serviceStatus ->
                if (serviceStatus == RMNCHAServiceStatus.PNC_ONGOING.name) {
                    val intent = ChildCareRegistrationActivity.getNewIntent(
                            requireContext(),
                            viewModel.selectedVillage.value!!,
                            viewModel.selectedSubCenter.value!!,
                            viewModel.selectedPhc.value!!,
                            viewModel.selectedHousehold.value!!,
                            it
                    )
                    startActivity(intent)
                }
                if (serviceStatus == RMNCHAServiceStatus.CHILDCARE_REGISTERED.name || serviceStatus == RMNCHAServiceStatus.CHILDCARE_ONGOING.name) {
                    val childDetails = CcChildListContent(
                            relationship = CcChildListContentRelationship(
                                    id = it.relationship.id,
                                    properties = CcChildListContentRelationshipProperties(),
                                    type = it.relationship.type
                            ),
                            targetNode = CcChildListContentTargetNode(
                                    id = it.targetNode.id,
                                    properties = null,
                                    labels = it.targetNode.labels
                            ),
                            sourceNode = CcChildListContentSourceNode(
                                    id = it.sourceNode.id,
                                    properties = CcChildListContentSourceNodeProperties(
                                            age = it.sourceNode.properties.age,
                                            createdBy = it.sourceNode.properties.createdBy,
                                            dateCreated = it.sourceNode.properties.dateCreated,
                                            dateModified = it.sourceNode.properties.dateModified,
                                            dateOfBirth = it.sourceNode.properties.dateOfBirth,
                                            gender = it.sourceNode.properties.gender,
                                            isAdult = it.sourceNode.properties.isAdult,
                                            modifiedBy = it.sourceNode.properties.modifiedBy,
                                            rmnchaServiceStatus = it.sourceNode.properties.rmnchaServiceStatus,
                                            type = it.sourceNode.properties.type,
                                            uuid = it.sourceNode.properties.uuid,
                                            firstName = it.sourceNode.properties.firstName),
                                    labels = it.sourceNode.labels
                            )
                    )
                    val intent = ChildImmunizationActivity.getNewIntent(
                            requireContext(),
                            viewModel.selectedVillage.value!!,
                            viewModel.selectedSubCenter.value!!,
                            viewModel.selectedPhc.value!!,
                            childDetails
                    )
                    startActivity(intent)
                }
            }
        }
        binding.childrenList.layoutManager =
                GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        binding.childrenList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}