package com.ssf.homevisit.rmncha.childCare.registration

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentChildRegistrationBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import com.ssf.homevisit.rmncha.base.RMNCHAUtils.getAshaWorkerNamesList

class ChildRegistrationFragment : Fragment() {

    private var _binding: FragmentChildRegistrationBinding? = null
    private val viewModel: ChildCareRegistrationViewModel by activityViewModels()
    private lateinit var invitationSentDialog: InvitationSentDialog
    private val binding get() = _binding!!

    private val spinnerItemSelector = object : OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when(p0?.id) {
                binding.spinnerWhoseMobile.id -> {
                    viewModel.whoseMobile.value = if (p2 > 0) requireActivity().resources.getStringArray(R.array.child_care_whose_mobile)[p2] else ""
                }
                binding.spinnerCovidTested.id -> {
                    viewModel.covidTested.value = if (p2 > 0) requireActivity().resources.getStringArray(R.array.child_care_covid_test)[p2] else ""
                }
                binding.spinnerCovidResult.id -> {
                    viewModel.covidTestResult.value = if (p2 > 0) requireActivity().resources.getStringArray(R.array.child_care_covid_test_result)[p2] else ""
                }
                binding.spinnerIncluenza.id -> {
                    viewModel.hasILI.value = if (p2 > 0) requireActivity().resources.getStringArray(R.array.array_yes_no)[p2] else ""
                }
                binding.spinnerCovidContact.id -> {
                    viewModel.hadContactWithCovid.value = if (p2 > 0) requireActivity().resources.getStringArray(R.array.array_yes_no)[p2] else ""
                }
                binding.spinnerSelectAshaName.id -> {
                    val selectedAshaWorkerName=binding.spinnerSelectAshaName.adapter.getItem(p2) as String
                    if( selectedAshaWorkerName !="Select"){
                        viewModel.selectedAsha.value =selectedAshaWorkerName
                    }
                }
            }
            viewModel.checkValidation()
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChildRegistrationBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        createDialogInstance()
        return binding.root
    }

    private fun createDialogInstance() {
        invitationSentDialog = InvitationSentDialog(requireContext(), R.drawable.ic_phone, "Form has been saved successfully!") {
            requireActivity().onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {
        val text="Sl.no of child in RCH register*"
        val spannableString = SpannableString(text)
        val lastCharIndex: Int = text.length - 1
        val colorSpan = ForegroundColorSpan(Color.RED)
        spannableString.setSpan(
            colorSpan,
            lastCharIndex,
            lastCharIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvSIInRCH.text = spannableString
        val influenzaText="Is the child experiencing\nILI (Influenza Like Illness)*"
        val influenzaSpannableString = SpannableString(influenzaText)
        val influenzaLastCharIndex: Int = influenzaText.length - 1
        val influenzaColorSpan = ForegroundColorSpan(Color.RED)
        influenzaSpannableString.setSpan(
            influenzaColorSpan,
            influenzaLastCharIndex,
            influenzaLastCharIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvInfluenzaLabel.text=influenzaSpannableString

        val covidText="Did the child have\nany contact with Covid-19\n positive patients(mother\n included) in the last 14 days?*"
        val covidSpannableString = SpannableString(covidText)
        val covidLastCharIndex: Int = covidText.length - 1
        val covidColorSpan = ForegroundColorSpan(Color.RED)
        covidSpannableString.setSpan(
            covidColorSpan,
            covidLastCharIndex,
            covidLastCharIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvContactWithCovid.text = covidSpannableString

        val whoseMobileNumber = "Whose mobile number?*"
        val mobileSpannableString = SpannableString(whoseMobileNumber)
        val mobileLastCharIndex: Int = whoseMobileNumber.length - 1
        val mobileColorSpan = ForegroundColorSpan(Color.RED)
        mobileSpannableString.setSpan(
            mobileColorSpan,
            mobileLastCharIndex,
            mobileLastCharIndex + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvWhoseMobileNumber.text = mobileSpannableString

        binding.spinnerWhoseMobile.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCovidTested.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCovidResult.onItemSelectedListener = spinnerItemSelector
        binding.spinnerIncluenza.onItemSelectedListener = spinnerItemSelector
        binding.spinnerCovidContact.onItemSelectedListener = spinnerItemSelector
        binding.checkboxOtherState.setOnCheckedChangeListener { _, b ->
            viewModel.bornInOtherCity.value = b
        }
        viewModel.coupleDetail.observe(viewLifecycleOwner) {
        }
        viewModel.isValidData.observe(viewLifecycleOwner) {
            setupBottomBar(it)
        }
        viewModel.ashaWorkers.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, getAshaWorkerNamesList(it))
            binding.spinnerSelectAshaName.adapter = adapter
            binding.spinnerSelectAshaName.onItemSelectedListener = spinnerItemSelector
        }
        viewModel.childMotherDetail.observe(viewLifecycleOwner) {
            val age = RMNCHAUtils.getBabyAgeFromDOB(it.content[0].sourceNode.properties.dateOfBirth)
            binding.tvAge.text = RMNCHAUtils.getBabyAgeInWeeks(age)
            viewModel.getPncRegistrationData()?.observe(viewLifecycleOwner) {
                it.let {
                    binding.tvPlaceOfBirthValue.text=it.deliveryDetails.place
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupBottomBar(enabled: Boolean = false) {
        binding.bottomBar.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            saveButton.isEnabled = enabled
            saveButton.text = "Submit"
            saveButton.setOnClickListener {
                viewModel.createChild().observe(viewLifecycleOwner) {
                    it?.let {
                        if(!invitationSentDialog.isShowing) {
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