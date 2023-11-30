package com.ssf.homevisit.fragment.vhsnc

import GridSpacingItemDecoration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.HouseholdMemberAdapter
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.FragmentIndividualSelectionBinding
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel

class IndividualSelectionFragment : Fragment() {
    private lateinit var binding: FragmentIndividualSelectionBinding
    private val viewModel: CreateMeetingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializerComponents(inflater, container)
        return binding.root
    }

    private fun initializerComponents(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentIndividualSelectionBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        viewModel.houseHoldId.value = arguments?.getString("householdID", "")
        binding.emptyState.txtMsg.text = getString(R.string.no_data_found)
        setupBottomBar(enabled = false)
        val layoutManager = GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false)
        val adapter = HouseholdMemberAdapter(viewModel.getInvitesOfHousehold()?.map { it.properties.uuid }) {
            setupBottomBar(viewModel.members.value?.any { (it.checked ?: false) } ?: false)
        }
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        if (binding.recycler.itemDecorationCount == 0) {
            binding.recycler.addItemDecoration(GridSpacingItemDecoration(3, 24, false))
        }
        setupBottomBar(viewModel.members.value?.any {
            (it.checked ?: false) && (viewModel.invitesSent.contains(it.target.properties.uuid))
        } ?: false)
        viewModel.members.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            binding.emptyState.root.isVisible = it.isNullOrEmpty()
            binding.recycler.scheduleLayoutAnimation()
        }
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            backText = getString(R.string.select_other_hh)
            mode = BottomBarMode.SEND_INVITE
            sendInvite.isEnabled = enabled
            sendInvite.setOnClickListener {
                    if (viewModel.sendInvites()) {
                        InvitationSentDialog(requireContext()) {
                            viewModel.houseHoldId.value = ""
                            requireActivity().onBackPressed()
                        }.show()
                    } else {
                        UIController.getInstance()
                            .displayToastMessage(requireContext(), "Something went wrong!")
                    }
            }
            txtBack.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}