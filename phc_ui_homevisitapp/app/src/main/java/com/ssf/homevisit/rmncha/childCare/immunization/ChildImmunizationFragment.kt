package com.ssf.homevisit.rmncha.childCare.immunization

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentChildImmunizationBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.models.ChildMotherDetailResponse
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import java.util.*

class ChildImmunizationFragment : Fragment() {

    private var _binding: FragmentChildImmunizationBinding? = null
    private val viewModel: ChildImmunizationViewModel by activityViewModels()
    private lateinit var invitationSentDialog: InvitationSentDialog
    private lateinit var childMotherDetails:ChildMotherDetailResponse

    private val spinnerItemSelector = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when (p0?.id) {
                binding.spinnerSelectAshaName.id -> {
                    val selectedAshaWorkerName=binding.spinnerSelectAshaName.adapter.getItem(p2) as String
                    if( selectedAshaWorkerName !="Select"){
                        viewModel.selectedAsha.value =selectedAshaWorkerName
                    }
                }
                binding.spinnerVaccinationStatus.id -> {
                    viewModel.vaccinationStatus.value =
                        binding.spinnerVaccinationStatus.adapter.getItem(p2) as String
                }
                binding.spinnerCaseCloserReason.id -> {
                    viewModel.setCaseClosureReason(
                        binding.spinnerCaseCloserReason.adapter.getItem(
                            p2
                        ) as String
                    )
                }
                binding.spinnerReason.id -> {
                    viewModel.setVaccineNotDoneReason(
                        binding.spinnerReason.adapter.getItem(p2) as String
                    )
                }
                binding.spinnerCovidTestDone.id -> {
                    val value = binding.spinnerCovidTestDone.adapter.getItem(p2) as String
                    viewModel.setCovidTestDone(value)
                }
                binding.spinnerAEFIStatus.id -> {
                    val value = binding.spinnerAEFIStatus.adapter.getItem(p2) as String
                    viewModel.setAEFIStatus(value)
                }
                binding.spinnerVaccineName.id -> {
                    val value = binding.spinnerVaccineName.adapter.getItem(p2) as String
                    viewModel.setVaccineName(value)
                }
                binding.spinnerDeathPlace.id -> {
                    val value = binding.spinnerDeathPlace.adapter.getItem(p2) as String
                    viewModel.setDeathPlace(value)
                }
                binding.spinnerCauseOfDeath.id -> {
                    val value = binding.spinnerCauseOfDeath.adapter.getItem(p2) as String
                    viewModel.setDeathCause(value)
                }
                binding.spinnerCovidTestResult.id -> {
                    val value = binding.spinnerCovidTestResult.adapter.getItem(p2) as String
                    viewModel.setDeathTestResult(value)
                }
                binding.spinnerIsFeelingILI.id -> {
                    val value = binding.spinnerIsFeelingILI.adapter.getItem(p2) as String
                    viewModel.setIsFeelingILI(value)
                }
                binding.spinnerIsCovidContact.id -> {
                    val value = binding.spinnerIsCovidContact.adapter.getItem(p2) as String
                    viewModel.setCovidContactValue(value)
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildImmunizationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        invitationSentDialog = InvitationSentDialog(
            requireContext(),
            com.ssf.homevisit.R.drawable.ic_phone,
            "Form has been saved successfully"
        ) {
            requireActivity().onBackPressed()
        }
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUi()
        initClick()
        setListAdapter()
        setupBottomBar()
    }

    private fun setUi() {
        val text="Is the child\\nexperiencing ILI\\n(Influenza Like Illness)*"
        val spannableString = SpannableString(text)
        val lastCharIndex: Int = text.length - 1
        val colorSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(
            colorSpan,
            lastCharIndex,
            lastCharIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvIsFeelingILI.text = spannableString

        val covidText="Did the child have any\\ncontact with Covid-19\\npositive patients\\n(mother included)\\nin the last 14 days?*"
        val covidSpannableString = SpannableString(covidText)
        val covidLastCharIndex: Int = covidText.length - 1
        val covidColorSpan = ForegroundColorSpan(Color.RED)
        covidSpannableString.setSpan(
            covidColorSpan,
            covidLastCharIndex,
            covidLastCharIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvIsCovidContact.text=covidSpannableString
    }

    private fun initClick(){
        binding.tvChildImmunization.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(
                AppDefines.CHILD_MOTHER_DETAILS, Gson().toJson(childMotherDetails)
            )
            findNavController().navigate(R.id.childHistoryFragment,bundle)
        }
    }

    private fun setListAdapter() {
        viewModel.ashaWorkers.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, RMNCHAUtils.getAshaWorkerNamesList(it))
            binding.spinnerSelectAshaName.adapter = adapter
            binding.spinnerSelectAshaName.onItemSelectedListener = spinnerItemSelector
        }
        binding.spinnerVaccinationStatus.onItemSelectedListener = spinnerItemSelector
        binding.spinnerReason.onItemSelectedListener = spinnerItemSelector
        binding.spinnerAEFIStatus.onItemSelectedListener = spinnerItemSelector
        binding.spinnerVaccineName.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCaseCloserReason.onItemSelectedListener = spinnerItemSelector
        binding.spinnerDeathPlace.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCauseOfDeath.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCovidTestDone.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCovidTestResult.onItemSelectedListener = spinnerItemSelector
        binding.spinnerIsFeelingILI.onItemSelectedListener = spinnerItemSelector
        binding.spinnerIsCovidContact.onItemSelectedListener = spinnerItemSelector
        viewModel.childMotherDetail.observe(viewLifecycleOwner) {
            childMotherDetails=it
            viewModel.getPncServiceData()?.observe(viewLifecycleOwner) {
                binding.tvBabyWeight.txtHHName.text = it.birthWeight.toString()
            }
        }

        binding.etOPV1DoseDate.setOnClickListener {
            selectDate { d, m, y ->
                viewModel.setOPV1DoseDate(d, m, y)
            }
        }
        binding.etPanavalentDoseDate.setOnClickListener {
            selectDate { d, m, y ->
                viewModel.setPanavalentDoseDate(d, m, y)
            }
        }
        binding.etRota1Dose.setOnClickListener {
            selectDate { d, m, y ->
                viewModel.setRota1DoseDate(d, m, y)
            }
        }
        binding.etPCV1Dose.setOnClickListener {
            selectDate { d, m, y ->
                viewModel.setPCV1DoseDate(d, m, y)
            }
        }
        binding.etICV1Dose.setOnClickListener {
            selectDate { d, m, y ->
                viewModel.setICV1DoseDate(d, m, y)
            }
        }
        binding.tvVaccinationDetails.setOnClickListener {
            val options = resources.getStringArray(com.ssf.homevisit.R.array.vaccine_name)
            val selected = options.map {
                viewModel.vaccinationNames.value?.any { vaccineItemData ->
                    vaccineItemData.name.value?.contains(
                        it,
                        true
                    ) ?: false
                } ?: false
            }
            AlertDialog.Builder(requireContext(), com.ssf.homevisit.R.style.AlertDialog)
                .setMultiChoiceItems(
                    options,
                    selected.toBooleanArray()
                ) { _, index, checked ->
                    if (checked) {
                        viewModel.addVaccineName(options[index])
                    } else {
                        viewModel.removeVaccineName(options[index])
                    }
                }.setPositiveButton("Done") { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.show()
        }

        viewModel.vaccinationNames.observe(viewLifecycleOwner) { vaccineList ->
            binding.llVaccines.removeAllViewsInLayout()
            if (vaccineList.isEmpty()) {
                binding.llVaccines.requestLayout()
            }
            vaccineList.forEach { item ->
                val data = VaccineItemView(requireContext()).apply {
                    setData(item)
                    id = View.generateViewId()
                }
                data.onExpiryDateSelection = { day, month, year ->
                    viewModel.setVaccineDate(item, day, month, year)
                }

                binding.llVaccines.addView(data)
            }
        }

        binding.radioGroupCaseClosure.setOnCheckedChangeListener { _, i ->
            val isClosure = (i == binding.radioYes.id)
            viewModel.setIsCaseClosure(isClosure)
        }
        binding.tvDeathDateValue.setOnClickListener {
            selectDate { d, m, y ->
                viewModel.setDateOfDeath(d, m, y)
            }
        }
    }

    private fun selectDate(onSelection: (day: Int, month: Int, year: Int) -> Unit) {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            com.ssf.homevisit.R.style.DatePickerDialogTheme,
            { _, year, month, day ->
                onSelection.invoke(day, month, year)
            }, Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH], Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBarInc.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            saveButton.isEnabled = true
            saveButton.setOnClickListener {
                viewModel.createImmunization().observe(viewLifecycleOwner) {
                    it?.let {
                        if (!invitationSentDialog.isShowing) {
                            invitationSentDialog.show()
                        }
                    }
                }
            }
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}