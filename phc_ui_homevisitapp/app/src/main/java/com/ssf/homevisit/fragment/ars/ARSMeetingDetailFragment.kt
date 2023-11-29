package com.ssf.homevisit.fragment.ars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.controller.UIController
import com.ssf.homevisit.databinding.FragmentArsMeetingDetailBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.viewmodel.ARSViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [ARSMeetingDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ARSMeetingDetailFragment : Fragment() {

    private lateinit var binding: FragmentArsMeetingDetailBinding
    private val viewModel: ARSViewModel by activityViewModels()
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
        binding = FragmentArsMeetingDetailBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.meetingData = viewModel.getMeetingData()
        invitationSentDialog = InvitationSentDialog(
            requireContext(),
            R.drawable.ic_phone,
            "ARS meeting has been scheduled!"
        ) {
            findNavController().popBackStack(R.id.ARSMeetingListFragment, false)
        }
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
    }

    private fun initNewMeetingUI() {
        setupBottomBar()
        viewModel.setMsgForARS()
        binding.btnEditMeeting.setOnClickListener {
            requireActivity().onBackPressed()
        }
        binding.btnInviteParticipant.setOnClickListener {
            viewModel.setSpecialInviteeMode(false)
            val target = ARSMeetingDetailFragmentDirections.toAddSpecialInvitee()
            findNavController().navigate(target)
        }
        binding.btnSpecialInvitee.setOnClickListener {
            viewModel.setSpecialInviteeMode(true)
            val target = ARSMeetingDetailFragmentDirections.toAddSpecialInvitee()
            findNavController().navigate(target)
        }
    }

    private fun setupBottomBar() {
        binding.bottomBar.apply {
            mode = BottomBarMode.FINISH
            finishBtn.setOnClickListener {
                viewModel.updateMeeting().observe(viewLifecycleOwner) {
                    it?.let {
                        viewModel.setLoading(false)
                        if (!invitationSentDialog.isShowing) {
                            invitationSentDialog.show()
                        }
                    } ?: UIController.getInstance()
                        .displayToastMessage(requireContext(), "Something went wrong!")
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ARSMeetingDetailFragment()
    }
}