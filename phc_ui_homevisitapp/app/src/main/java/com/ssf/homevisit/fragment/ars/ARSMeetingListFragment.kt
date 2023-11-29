package com.ssf.homevisit.fragment.ars

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
import com.ssf.homevisit.databinding.FragmentArsMeetingListBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.viewmodel.ARSViewModel

class ARSMeetingListFragment : Fragment() {
    private lateinit var binding: FragmentArsMeetingListBinding
    private val viewModel: ARSViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.clearCaches()
        initializerComponents(inflater, container)
        return binding.root
    }

    private fun initializerComponents(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentArsMeetingListBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBottomBar()
        observeData()
        binding.btnScheduleNewMeeting.setOnClickListener {
            val toNewMeeting = ARSMeetingListFragmentDirections.arsMeetingListToNewMeeting()
            findNavController().navigate(toNewMeeting)
        }
    }

    private fun observeData() {
        viewModel.refreshMeetings()
        val adapter = MeetingsAdapter(false) { meeting, event ->
            when (event) {
                MeetingClickEvent.VIEW_MEETING -> {
                    val toProcessMeeting =
                        ARSMeetingListFragmentDirections.arsMeetingListToProcessMeeting(meeting.id!!)
                    findNavController().navigate(toProcessMeeting)
                }
                MeetingClickEvent.RE_SCHEDULE_MEETING -> {

                }
                MeetingClickEvent.CANCEL_MEETING -> {
                    viewModel.cancelMeeting(meeting.id!!).observe(viewLifecycleOwner) {
                        viewModel.setLoading(false)
                        it?.let {
                            viewModel.refreshMeetings()
                        }
                    }
                }
            }
        }
        val layoutManager = WrapperLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recycler.layoutManager = layoutManager

        binding.recycler.adapter = adapter
        viewModel.setLoading(true)
        viewModel.meetings.observe(viewLifecycleOwner) {
            val filteredList =
                it?.data?.filter { /*!it.schedule?.status.isNullOrEmpty() &&*/ !it.schedule?.status.equals(
                    "cancelled",
                    true
                )
                }
            binding.groupList.isVisible = !filteredList.isNullOrEmpty()
            binding.emptyState.root.isVisible = filteredList.isNullOrEmpty()
            viewModel.setLoading(false)
            adapter.submitList(filteredList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun setBottomBar() {
        binding.bottomBar.apply {
            mode = BottomBarMode.ONLY_BACK
            backText = getString(R.string.back)
            txtBack.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

}