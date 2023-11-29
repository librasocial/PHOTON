package com.ssf.homevisit.fragment.vhsnc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.MeetingClickEvent
import com.ssf.homevisit.adapters.MeetingsAdapter
import com.ssf.homevisit.adapters.WrapperLayoutManager
import com.ssf.homevisit.databinding.FragmentCreateMeetingBinding
import com.ssf.homevisit.utils.VHSNC
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel


class CreateMeetingFragment : Fragment() {

    private lateinit var binding: FragmentCreateMeetingBinding
    private val commonViewModel: CreateMeetingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreateMeetingBinding.inflate(inflater, container, false)
        binding.viewModel = commonViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commonViewModel.clearData() // remove when VHSNCOptionsFragment is active
        setupBottomBar()
        initViews()
        observeData()
    }

    private fun observeData() {
        commonViewModel.refreshMeetings()
        val adapter = MeetingsAdapter(commonViewModel.isVHND) { meeting, event ->
            when(event) {
                MeetingClickEvent.VIEW_MEETING -> {
                    if(commonViewModel.isVHND) {
                        val toProceedVHNDMeetingFragment = CreateMeetingFragmentDirections.toProcessVHNDMeeting(meeting.id!!)
                        findNavController().navigate(toProceedVHNDMeetingFragment)
                    } else {
                        val toProceedMeetingFragment = CreateMeetingFragmentDirections.toProcessMeetingFragment(meeting.id!!)
                        findNavController().navigate(toProceedMeetingFragment)
                    }
                }
                MeetingClickEvent.RE_SCHEDULE_MEETING -> {
                    if (commonViewModel.isVHND) {

                    } else {
                        val toReScheduleMeetingFragment = CreateMeetingFragmentDirections.toReScheduleMeetingFragment(meeting.id!!)
                        findNavController().navigate(toReScheduleMeetingFragment)
                    }
                }
                MeetingClickEvent.CANCEL_MEETING -> {
                    commonViewModel.cancelMeeting(meeting.id!!).observe(viewLifecycleOwner) {
                        commonViewModel.hideLoading()
                        it?.let {
                            commonViewModel.refreshMeetings()
                        }
                    }
                }
            }
        }
        val layoutManager = WrapperLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager
        if(commonViewModel.isVHND) {
            binding.tvTopics.visibility = View.INVISIBLE
        }
        binding.recycler.adapter = adapter
        commonViewModel.isLoading.value = true
        commonViewModel.meetings.observe(viewLifecycleOwner) {
            val filteredList = it?.data?.filter { /*!it.schedule?.status.isNullOrEmpty() &&*/ !it.schedule?.status.equals("cancelled", true) }
            binding.groupList.isVisible = !filteredList.isNullOrEmpty()
            binding.emptyState.root.isVisible = filteredList.isNullOrEmpty()
            commonViewModel.isLoading.value = false
            adapter.submitList(filteredList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        binding.btnScheduleNewMeeting.setOnClickListener {
            if (commonViewModel.isVHND) {
                val navigate = CreateMeetingFragmentDirections.createMeetingToNewVHNDMeeting()
                findNavController().navigate(navigate)
            } else {
                val navigate = CreateMeetingFragmentDirections.createMeetingToNewMeeting()
                findNavController().navigate(navigate)
            }
        }
    }

    private fun setupBottomBar() {
        binding.emptyState.txtMsg.text = "No meetings have been scheduled"
        binding.bottomBar.apply {
            backText = getString(R.string.back)
            mode = BottomBarMode.ONLY_BACK
            txtBack.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}