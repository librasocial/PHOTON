package com.ssf.homevisit.fragment.vhsnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.FragmentMeetingDetailBinding
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [MeetingDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MeetingDetailFragment : Fragment() {

    private lateinit var binding: FragmentMeetingDetailBinding
    private val viewModel: CreateMeetingViewModel by activityViewModels()
    private lateinit var invitationSentDialog: InvitationSentDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initializeComponent(inflater, container)
        return binding.root
    }

    private fun initializeComponent(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentMeetingDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.meetingData = viewModel.getMeetingData()
        invitationSentDialog = InvitationSentDialog(requireContext(), R.drawable.ic_phone, viewModel.vhsncOrVhd.value + " meeting has been scheduled!") {
            findNavController().popBackStack(R.id.createMeetingFragment, false)
        }
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
    }

    private fun initNewMeetingUI() {
        setupBottomBar()
        if (viewModel.isVHND) {
            viewModel.setMsgForVHND()
        } else {
            viewModel.setMsgForVHSNC()
        }
        binding.btnEditMeeting.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnInviteParticipant.setOnClickListener {
            val meetingId = viewModel.meetindId.value ?: arguments?.getString("meetingId", "") ?: ""
            val target = MeetingDetailFragmentDirections.meetingDetailToParticipantSelection(meetingId)
            findNavController().navigate(target)
        }
        binding.btnSpecialInvitee.setOnClickListener {
            val meetingId = viewModel.meetindId.value ?: arguments?.getString("meetingId", "") ?: ""
            val target = MeetingDetailFragmentDirections.toSpecialInvitees(meetingId)
            findNavController().navigate(target)
        }
        binding.btnInviteStaff.apply {
            isVisible = viewModel.isVHND
            setOnClickListener {
                val toPhcList = MeetingDetailFragmentDirections.toPhcStaffList(viewModel.meetindId.value ?: "")
                findNavController().navigate(toPhcList)
            }
        }
        binding.btnGPInvitee.apply {
            setOnClickListener {
                val toGPInvitee = MeetingDetailFragmentDirections.meetingDetailToAllGPInviteeFragment(viewModel.meetindId.value ?: "")
                findNavController().navigate(toGPInvitee)
            }
        }
    }

    private fun setupBottomBar() {
        binding.btnShowAllInvitee.setOnClickListener {
            val toAllInvitee = MeetingDetailFragmentDirections.toAllInviteeList(viewModel.meetindId.value ?: "")
            findNavController().navigate(toAllInvitee)
        }
        binding.bottomBar.apply {
            mode = BottomBarMode.FINISH
            finishBtn.setOnClickListener {
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
    companion object {
        @JvmStatic
        fun newInstance() = MeetingDetailFragment()
    }
}