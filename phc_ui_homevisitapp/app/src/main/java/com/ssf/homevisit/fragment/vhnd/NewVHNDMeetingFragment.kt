package com.ssf.homevisit.fragment.vhnd

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentNewVhndMeetingBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.NewMeetingFragmentDirections
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel
import java.util.*

class NewVHNDMeetingFragment : Fragment() {
    private lateinit var binding: FragmentNewVhndMeetingBinding
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
        binding = FragmentNewVhndMeetingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNewMeetingUI()
        observeData()
    }

    private fun observeData() {
        viewModel.venues.observe(viewLifecycleOwner) {
//            print(it)
        }
        viewModel.isValidData.observe(viewLifecycleOwner) {
            setupBottomBar(it)
        }
        viewModel.setAsset(4) // to select only Anganwadi
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
        binding.todayDateValue.text = DateFormat.format("dd/MM/yyyy", Date())
        binding.tvMeetingTime.setOnClickListener { pickMeetingTime() }
        binding.tvMeetingDate.setOnClickListener { selectDate() }
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            saveButton.isEnabled = enabled
            saveButton.setOnClickListener {
                viewModel.createMeeting().observe(viewLifecycleOwner) {
                    it?.let {
                        viewModel.hideLoading()
                        viewModel.meetindId.value = it.id
                        val toMeetingDetail =
                            NewVHNDMeetingFragmentDirections.newVHNDMeetingFragmentToMeetingDetail(it.id!!)
                        findNavController().navigate(toMeetingDetail)
                    }
                }
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }
}