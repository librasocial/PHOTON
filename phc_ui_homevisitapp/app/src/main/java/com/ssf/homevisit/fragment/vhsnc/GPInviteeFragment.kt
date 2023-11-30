package com.ssf.homevisit.fragment.vhsnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.AllMeetingParticipantAdapter
import com.ssf.homevisit.adapters.GPMemberListAdapter
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.FragmentAllInviteeListBinding
import com.ssf.homevisit.databinding.FragmentGpInviteeBinding
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel

class GPInviteeFragment: Fragment() {
    private lateinit var binding: FragmentGpInviteeBinding
    private val viewModel: CreateMeetingViewModel by activityViewModels()
    private lateinit var invitationSentDialog: InvitationSentDialog

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
        binding = FragmentGpInviteeBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        invitationSentDialog = InvitationSentDialog(requireContext()) {
            requireActivity().onBackPressed()
        }
        setBottomBar()
        setParticipantList()
    }

    private fun setBottomBar() {
        binding.bottomBar.apply {
            mode = BottomBarMode.SEND_INVITE
            backText = getString(R.string.back)
            txtBack.setOnClickListener { requireActivity().onBackPressed() }
            sendInvite.setOnClickListener {
                viewModel.updateMeeting().observe(viewLifecycleOwner) {
                    it?.let {
                        viewModel.hideLoading()
                        if(!invitationSentDialog.isShowing) {
                            invitationSentDialog.show()
                        }
                    } ?: UIController.getInstance().displayToastMessage(requireContext(), "Something went wrong!")
                }
            }
        }
    }

    private fun setParticipantList() {
        val adapter = GPMemberListAdapter {
            viewModel.toggleGPMemberInvite(it)
        }
        binding.recycler.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.adapter = adapter
        viewModel.gpMembers.observe(viewLifecycleOwner) {
            binding.emptyState.root.isVisible = it.content.isEmpty()
            it.content.forEach {
                it.isSelected = viewModel.invitedGpMembers.containsKey(it.targetNode.id)
            }
            adapter.submitList(it.content)
        }
    }
}