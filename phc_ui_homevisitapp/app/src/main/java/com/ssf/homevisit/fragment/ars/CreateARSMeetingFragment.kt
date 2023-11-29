package com.ssf.homevisit.fragment.ars

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentCreateArsMeetingBinding
import com.ssf.homevisit.databinding.ItemTopicDropdownBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.models.DropDownData
import com.ssf.homevisit.models.MeetingTopicChip
import com.ssf.homevisit.models.TopicItem
import com.ssf.homevisit.viewmodel.ARSViewModel
import java.util.*

class CreateARSMeetingFragment : Fragment() {
    private lateinit var binding: FragmentCreateArsMeetingBinding
    private val viewModel: ARSViewModel by activityViewModels()

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
        binding = FragmentCreateArsMeetingBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBottomBar()
        initNewMeetingUI()
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

        viewModel.isValidData.observe(viewLifecycleOwner) {
            setupBottomBar(it)
        }
        binding.tvMeetingDate.setOnClickListener { selectDate() }
        val chipCheckListener = CompoundButton.OnCheckedChangeListener { chip, isChecked ->
            addDropDowns(chip, isChecked)
        }
        viewModel.meetingTopicOptions.getChipViews(requireContext()).forEach {
            it.setOnCheckedChangeListener(chipCheckListener)
            binding.topicGroup.addView(it)
        }

        viewModel.meetingTopicOptions.dropDowns.observe(viewLifecycleOwner) { dropDowns ->
            binding.detailGroup.isVisible = dropDowns.isNotEmpty()
            val flow = binding.dropDownFlow
            binding.dropDownContainer.removeAllViewsInLayout() // remove all previous views
            binding.dropDownContainer.addView(flow)
            val ids = mutableListOf<Int>()
            dropDowns.forEach {
                try {
                    val spinner = createSpinner(it)
                    binding.dropDownContainer.addView(spinner.root)
                    ids.add(spinner.root.id)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
            viewModel.checkValidation()
            binding.dropDownFlow.referencedIds = ids.toIntArray()
        }
    }

    private fun addDropDowns(chip: CompoundButton, isChecked: Boolean) {
        val chipData = (chip.tag as MeetingTopicChip)
        viewModel.meetingTopicOptions[chipData]?.let {
            if (!isChecked) {
                viewModel.meetingTopicOptions[chipData] = null
            }
        } ?: run {
            viewModel.meetingTopicOptions.attachDropDown(chipData)
        }
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            saveButton.isEnabled = enabled
            saveButton.setOnClickListener {
                viewModel.createMeeting().observe(viewLifecycleOwner) {
                    it?.let {
                        viewModel.setLoading(false)
                        viewModel.meetindId.value = it.id
                        val toMeetingDetail =
                            CreateARSMeetingFragmentDirections.toMeetingDetail()
                        findNavController().navigate(toMeetingDetail)
                    }
                }
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun createSpinner(
        dropDowndata: DropDownData
    ): ItemTopicDropdownBinding {
        val view = ItemTopicDropdownBinding.inflate(LayoutInflater.from(requireContext()))
        val title = getString(dropDowndata.chipData.label)
        val topicOptions = dropDowndata.dropDown
        val valuesToSet = topicOptions.filter { it.isSelected }
        setSpinnerText(valuesToSet, view)
        view.txtSelectedValue.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            builder.setTitle("$title topics")
            builder.setCancelable(false)
            val options = topicOptions.map { it.label }
            val selection = topicOptions.map { it.isSelected }
            builder.setMultiChoiceItems(
                options.toTypedArray(), selection.toBooleanArray()
            ) { _, index, isChecked ->
                topicOptions[index].isSelected = isChecked
                setSpinnerText(topicOptions.filter { it.isSelected }, view)
                viewModel.checkValidation()
            }
            builder.setPositiveButton(
                "OK"
            ) { dialogInterface, _ -> // Initialize string builder
                dialogInterface.dismiss()
            }
            builder.show()
        }
        view.txtTopicSubHeading.text = getString(R.string.select_topic_under, "\n$title")
        view.root.id = View.generateViewId()
        return view
    }

    private fun setSpinnerText(
        valuesToSet: List<TopicItem>,
        view: ItemTopicDropdownBinding
    ) {
        if (valuesToSet.isEmpty()) {
            view.txtSelectedValue.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.selected_grey
                )
            )
            view.txtSelectedValue.text = getString(R.string.select)
            return
        }
        view.txtSelectedValue.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.black
            )
        )
        view.txtSelectedValue.text = valuesToSet.joinToString(separator = ", ") { it.label }
    }
}