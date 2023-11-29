package com.ssf.homevisit.rmncha.adolescentCare.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.AdCareFollowUpsDialogBinding
import com.ssf.homevisit.rmncha.adolescentCare.AdCareServiceTopics
import com.ssf.homevisit.rmncha.adolescentCare.AdolescentCareServiceViewModel

class FollowUpDialog(
    context: Context,
    onSaveData: (hasAnyData: Boolean) -> Unit
) : Dialog(context, false, null) {

    private val binding = AdCareFollowUpsDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > Adolescent Care > "
        binding.toolbar.destination = "Enter Follow-up / Counselling Details"
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
        return AdolescentCareServiceViewModel.followUpData.any { it.value }
    }

    private fun setRadioListener() {
        binding.radioGroupNutrition.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q1] =
                checkedId == binding.radioBtnNutritionYes.id
        }
        binding.radioGroupSkin.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q2] =
                checkedId == binding.radioBtnSkinYes.id
        }
        binding.radioGroupPreMeritalCounselling.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q3] =
                checkedId == binding.radioBtnPreMeritalCounsellingYes.id
        }
        binding.radioGroupSexualProblem.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q4] =
                checkedId == binding.radioBtnSexualProblemYes.id
        }
        binding.radioGroupIsAbortion.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q5] =
                checkedId == binding.radioBtnIsAbortionYes.id
        }
        binding.radioGroupRtiSTI.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q6] =
                checkedId == binding.radioBtnRtiSTIYes.id
        }
        binding.radioGroupSubstanceMisuse.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q7] =
                checkedId == binding.radioBtnSubstanceMisuseYes.id
        }
        binding.radioGroupLearningProblems.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q8] =
                checkedId == binding.radioBtnLearningProblemsYes.id
        }

        binding.radioGroupStress.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q9] =
                checkedId == binding.radioBtnStressYes.id
        }

        binding.radioGroupDepression.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.followUpData[AdCareServiceTopics.FOLLOW_UPS_Q10] =
                checkedId == binding.radioBtnDepressionYes.id
        }
    }
}