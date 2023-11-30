package com.ssf.homevisit.rmncha.adolescentCare

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ssf.homevisit.databinding.FragmentAdolescentCareServiceBinding
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.InvitationSentDialog
import com.ssf.homevisit.rmncha.adolescentCare.dialogs.CommodotiesDialog
import com.ssf.homevisit.rmncha.adolescentCare.dialogs.FollowUpDialog
import com.ssf.homevisit.rmncha.adolescentCare.dialogs.InformationDialog
import com.ssf.homevisit.rmncha.base.RMNCHAUtils

class AdolescentCareServiceFragment : Fragment() {
    private var _binding: FragmentAdolescentCareServiceBinding? = null
    private val viewModel: AdolescentCareServiceViewModel by activityViewModels()

    private val binding get() = _binding!!
    private lateinit var invitationSentDialog: InvitationSentDialog

    private val spinnerItemSelector = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            when (p0?.id) {
                binding.spinnerSelectAshaName.id -> {
                    val selectedAshaWorkerName=binding.spinnerSelectAshaName.adapter.getItem(p2) as String
                    if( selectedAshaWorkerName !="Select"){
                        viewModel.selectedAsha.value =selectedAshaWorkerName
                    }
                }
            }
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdolescentCareServiceBinding.inflate(inflater, container, false)
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
            com.ssf.homevisit.R.drawable.ic_phone,
            "Form has been saved successfully!"
        ) {
            requireActivity().onBackPressed()
        }
    }

    private fun setListAdapter() {
        binding.btnCommodities.setOnClickListener {
            CommodotiesDialog(requireContext()) {
                binding.btnCommodities.setChecked(it)
            }.show()
        }
        binding.btnInformation.setOnClickListener {
            InformationDialog(requireContext()) {
                binding.btnInformation.setChecked(it)
            }.show()
        }
        binding.tvFinYearLabel.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.isReferralUpToPHC = isChecked
        }
        binding.btnFollowUps.setOnClickListener {
            FollowUpDialog(requireContext()) {
                binding.btnFollowUps.setChecked(it)
            }.show()
        }
        viewModel.ashaWorkers.observe(viewLifecycleOwner) {
            val adapter = ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_item,
                    RMNCHAUtils.getAshaWorkerNamesList(it))
            binding.spinnerSelectAshaName.adapter = adapter
            binding.spinnerSelectAshaName.onItemSelectedListener = spinnerItemSelector
        }
    }

    private fun setupBottomBar() {
        binding.bottomBar.mode = BottomBarMode.SAVE_CONTINUE
        binding.bottomBar.txtCancel.setOnClickListener { requireActivity().onBackPressed() }
        binding.bottomBar.saveButton.setOnClickListener {
            viewModel.isLoading.value = true
            viewModel.createAdCareService().observe(viewLifecycleOwner) {
                viewModel.isLoading.value = false
                if (!invitationSentDialog.isShowing) {
                    invitationSentDialog.show()
                }
                viewModel.clearData()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}