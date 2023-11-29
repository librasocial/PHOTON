package com.ssf.homevisit.fragment.vhsnc

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CompoundButton
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssf.homevisit.R
import com.ssf.homevisit.adapters.VenueListAdapter
import com.ssf.homevisit.databinding.FragmentNewMeetingBinding
import com.ssf.homevisit.databinding.ItemTopicDropdownBinding
import com.ssf.homevisit.models.DropDownData
import com.ssf.homevisit.models.MeetingTopicChip
import com.ssf.homevisit.models.TopicItem
import com.ssf.homevisit.viewmodel.CreateMeetingViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [NewMeetingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewMeetingFragment : Fragment() {

    private lateinit var binding: FragmentNewMeetingBinding
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
        binding = FragmentNewMeetingBinding.inflate(inflater, container, false)
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
        binding.todayDateValue.text = DateFormat.format("dd/MM/yyyy", Date())
        setAssetAdapter()
        binding.tvMeetingTime.setOnClickListener { pickMeetingTime() }

        binding.tvMeetingDate.setOnClickListener { selectDate() }
        val chipCheckListener = CompoundButton.OnCheckedChangeListener { chip, isChecked ->
            addDropDowns(chip, isChecked)
        }
        viewModel.chipMap.getChipViews(requireContext()).forEach {
            it.setOnCheckedChangeListener(chipCheckListener)
            binding.topicGroup.addView(it)
        }

        viewModel.chipMap.dropDowns.observe(viewLifecycleOwner) { dropDowns ->
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
//                if (i > 0) {
//                    binding.clVenuContainer.isVisible = true
//                    viewModel.setAsset(options[i])
//                } else {
//                    viewModel.setVenueSelection(null)
//                    binding.clVenuContainer.isVisible = false
//                    viewModel.setAsset("")
//                }
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {
                binding.clVenuContainer.isVisible = false
            }
        }

        binding.spinnerAsset.setSelection(viewModel.assetForVenue.value?:0)
    }

    private fun addDropDowns(chip: CompoundButton, isChecked: Boolean) {
        val chipData = (chip.tag as MeetingTopicChip)
        viewModel.chipMap[chipData]?.let {
            if (!isChecked) {
                viewModel.chipMap[chipData] = null
            }
        } ?: run {
            viewModel.chipMap.attachDropDown(chipData)
        }
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
                            NewMeetingFragmentDirections.newMeetingToMeetingDetail(it.id!!)
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

    companion object {
        @JvmStatic
        fun newInstance() = NewMeetingFragment()
    }
}