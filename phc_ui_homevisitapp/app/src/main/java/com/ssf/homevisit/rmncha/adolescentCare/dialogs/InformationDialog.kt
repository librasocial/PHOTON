package com.ssf.homevisit.rmncha.adolescentCare.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.AdCareInformationDialogBinding
import com.ssf.homevisit.rmncha.adolescentCare.AdCareServiceTopics
import com.ssf.homevisit.rmncha.adolescentCare.AdolescentCareServiceViewModel

class InformationDialog(
    context: Context,
    onSaveData: (hasAnyData: Boolean) -> Unit
) : Dialog(context, false, null) {

    private val binding = AdCareInformationDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > Adolescent Care > "
        binding.toolbar.destination = "Enter Details on IEC "
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        setRadioListener()
        binding.btnSave.setOnClickListener {
            onSaveData(hasAnInput())
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }

    private fun hasAnInput(): Boolean {
        return AdolescentCareServiceViewModel.informationData.any { it.value }
    }

    private fun setRadioListener() {
        binding.radioGroupSBCCTablet.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q1] =
                checkedId == binding.radioBtnSBCCYes.id
        }
        binding.radioGroupNutrition.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q2] =
                checkedId == binding.radioBtnNutritionYes.id
        }
        binding.radioGroupSRH.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q3] =
                checkedId == binding.radioBtnSRHYes.id
        }
        binding.radioGroupAbortion.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q4] =
                checkedId == binding.radioBtnAbortionYes.id
        }
        binding.radioGroupMentalHealth.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q5] =
                checkedId == binding.radioBtnMentalHealthYes.id
        }
        binding.radioGroupGBV.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q6] =
                checkedId == binding.radioBtnGBVYes.id
        }
        binding.radioGroupNCD.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q7] =
                checkedId == binding.radioBtnNCDYes.id
        }
        binding.radioGroupSubstanceMisuse.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.informationData[AdCareServiceTopics.INFORMATION_Q8] =
                checkedId == binding.radioBtnSubstanceMisuseYes.id
        }
    }
}