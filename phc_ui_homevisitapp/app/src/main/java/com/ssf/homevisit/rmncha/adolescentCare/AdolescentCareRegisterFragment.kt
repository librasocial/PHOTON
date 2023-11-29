package com.ssf.homevisit.rmncha.adolescentCare

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentRegisterAdolescentCareBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.rmncha.base.RMNCHAUtils

class AdolescentCareRegisterFragment : Fragment() {
    private var _binding: FragmentRegisterAdolescentCareBinding? = null
    private val viewModel: AdolescentCareViewModel by activityViewModels()
    private val spinnerItemSelector = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when (p0?.id) {
                binding.spinnerSelectAshaName.id -> {
                    val selectedAshaWorkerName=binding.spinnerSelectAshaName.adapter.getItem(p2) as String
                    if( selectedAshaWorkerName !="Select"){
                        viewModel.selectedAsha.value =selectedAshaWorkerName
                    }
                }
                binding.spinnerWhoseMobile.id -> {
                    viewModel.whoseMobile.value =
                        if (p2 > 0) requireActivity().resources.getStringArray(R.array.child_care_whose_mobile)[p2] else ""
                }
                binding.spinnerCovidTested.id -> {
                    viewModel.covidTested.value =
                        if (p2 > 0) requireActivity().resources.getStringArray(R.array.child_care_covid_test)[p2] else ""
                }

                binding.spinnerCovidResult.id -> {
                    viewModel.covidTestResult.value =
                        if (p2 > 0) requireActivity().resources.getStringArray(R.array.child_care_covid_test_result)[p2] else ""
                }
                binding.spinnerIncluenza.id -> {
                    viewModel.hasILI.value =
                        if (p2 > 0) requireActivity().resources.getStringArray(R.array.array_yes_no)[p2] else ""
                }
                binding.spinnerCovidContact.id -> {
                    viewModel.hadContactWithCovid.value =
                        if (p2 > 0) requireActivity().resources.getStringArray(R.array.array_yes_no)[p2] else ""
                }
            }
            viewModel.checkValidation()
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }

    private val binding get() = _binding!!
    private lateinit var invitationSentDialog: InvitationSentDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterAdolescentCareBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListAdapter()
        setupBottomBar()
        createDialogInstance()
    }

    private fun createDialogInstance() {
        invitationSentDialog = InvitationSentDialog(
            requireContext(),
            R.drawable.ic_phone,
            "Form has been saved successfully!"
        ) {
            requireActivity().onBackPressed()
        }
    }

    private fun setListAdapter() {
        viewModel.ashaWorkers.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
            RMNCHAUtils.getAshaWorkerNamesList(it))
            binding.spinnerSelectAshaName.adapter = adapter
            binding.spinnerSelectAshaName.onItemSelectedListener = spinnerItemSelector
        }
    }

    private fun setupBottomBar() {
        binding.bottomBar.apply {
            mode = BottomBarMode.SAVE_CONTINUE
            txtCancel.setOnClickListener { requireActivity().onBackPressed() }
            saveButton.setOnClickListener {
                viewModel.registerAdolescent().observe(viewLifecycleOwner) {
                    if (!invitationSentDialog.isShowing) {
                        invitationSentDialog.show()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}