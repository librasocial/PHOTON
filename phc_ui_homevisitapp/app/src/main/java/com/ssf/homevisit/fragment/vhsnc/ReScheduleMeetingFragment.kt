package com.ssf.homevisit.fragment.vhsnc

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.VenueListAdapter
import com.ssf.homevisit.databinding.FragmentReScheduleMeetingBinding
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel
import java.util.*

class ReScheduleMeetingFragment : Fragment() {

    private lateinit var binding: FragmentReScheduleMeetingBinding
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
        viewModel.isRescheduling.value = true
        binding = FragmentReScheduleMeetingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
        observeData()
    }

    private fun observeData() {
        viewModel.isValidData.observe(viewLifecycleOwner) {
            setupBottomBar(it)
        }
        val adapter = VenueListAdapter {
            setupBottomBar(true)
            viewModel.setVenueSelection(it)
        }
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recycler.layoutManager = layoutManager
        binding.recycler.adapter = adapter
        viewModel.venues.observe(viewLifecycleOwner) { venues ->
            if (venues.isNullOrEmpty()) {
                binding.clVenuContainer.isVisible = false
                viewModel.setVenueSelection(null)
            } else {
                binding.clVenuContainer.isVisible = true
            }
            adapter.submitList(venues)
        }
    }

    private fun selectDate() {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            R.style.DatePickerDialogTheme,
            { _, yr, month, day ->
                viewModel.setMeetingDate(yr, month, day)
            }, Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH], Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun pickMeetingTime() {
        TimePickerDialog(
            requireContext(),
            { _, hour, min ->
                viewModel.setMeetingTime(hour, min)
            },
            Calendar.getInstance()[Calendar.HOUR],
            Calendar.getInstance()[Calendar.MINUTE],
            false
        ).apply { setTitle("Select Meeting Time") }.show()
    }

    private fun initNewMeetingUI() {
        if ((arguments?.getBoolean("editVenue", false) == true)) {
            binding.editOnlyVenue.isVisible = false
            binding.tvNewMeetingHeading.text = "Change the Venue for this VHSNC"
        }

        viewModel.meetindId.value = arguments?.getString("meetingId", "")
        setAssetAdapter()
        binding.tvMeetingTime.setOnClickListener { pickMeetingTime() }
        binding.tvMeetingDate.setOnClickListener { selectDate() }
        viewModel.getMeeting().observe(viewLifecycleOwner) {
            binding.spinnerAsset.setSelection(viewModel.assetForVenue.value ?: 0)
        }
    }

    private fun setAssetAdapter() {
        val options = ArrayList<String>()
        options.add("Select")
        options.add("Office")
        options.add("School/Colleges")
        options.add("Others")

        val adapterVHSNC =
            ArrayAdapter(requireContext(), R.layout.spinner_text_layout, R.id.textView, options)
        binding.spinnerAsset.adapter = adapterVHSNC
        binding.spinnerAsset.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>?,
                view: View?,
                i: Int,
                l: Long
            ) {
                viewModel.setAsset(i)
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                binding.clVenuContainer.isVisible = false
            }
        }
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            saveButton.isEnabled = enabled
            saveButton.setOnClickListener {
                viewModel.updateMeetingSchedule().observe(viewLifecycleOwner) {
                    it?.let {
                        viewModel.hideLoading()
                        if ((arguments?.getBoolean("editVenue", false) == true)) {
                            requireActivity().onBackPressed()
                        } else {
                            viewModel.meetindId.value = it.id
                            val toMeetingDetail =
                                ReScheduleMeetingFragmentDirections.reScheduleToIndividualSelection(it.id!!)
                            findNavController().navigate(toMeetingDetail)
                        }
                    }
                }
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isRescheduling.value = false
    }
}