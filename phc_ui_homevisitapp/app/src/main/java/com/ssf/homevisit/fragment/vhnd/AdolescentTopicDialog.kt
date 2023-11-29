package com.ssf.homevisit.fragment.vhnd

import android.app.ActionBar.LayoutParams
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.AdolescentCareTopicDialogBinding

class AdolescentTopicDialog(
    context: Context,
    selection: Map<String, Int>,
    onSaveData: (map: HashMap<String, Int>) -> Unit
) : Dialog(context, false, null) {
    private val binding = AdolescentCareTopicDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > VHND > "
        binding.toolbar.destination = "Enter Details for Adolescent Care"
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        binding.apply {
            etAdolescentMale.setText(selection[VHNDTopics.ADS_CARE_Q1]?.toString() ?: "0")
            etAdolescentFemale.setText(selection[VHNDTopics.ADS_CARE_Q2]?.toString() ?: "0")
            etNoOfCounselling.setText(selection[VHNDTopics.ADS_CARE_Q3]?.toString() ?: "0")
            etCounsellingMale.setText(selection[VHNDTopics.ADS_CARE_Q4]?.toString() ?: "0")
            etCounsellingFemale.setText(selection[VHNDTopics.ADS_CARE_Q5]?.toString() ?: "0")
            etCalciumMale.setText(selection[VHNDTopics.ADS_CARE_Q6]?.toString() ?: "0")
            etCalciumFemale.setText(selection[VHNDTopics.ADS_CARE_Q7]?.toString() ?: "0")
            etFamilyPlanning.setText(selection[VHNDTopics.ADS_CARE_Q8]?.toString() ?: "0")
            etNoOfOCP.setText(selection[VHNDTopics.ADS_CARE_Q9]?.toString() ?: "0")
            etNoOfCC.setText(selection[VHNDTopics.ADS_CARE_Q10]?.toString() ?: "0")
        }
        binding.btnSave.setOnClickListener {
            val map = hashMapOf<String, Int>()
            map[VHNDTopics.ADS_CARE_Q1] =
                binding.etAdolescentMale.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q2] =
                binding.etAdolescentFemale.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q3] =
                binding.etNoOfCounselling.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q4] =
                binding.etCounsellingMale.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q5] =
                binding.etCounsellingFemale.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q6] = binding.etCalciumMale.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q7] = binding.etCalciumFemale.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q8] =
                binding.etFamilyPlanning.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q9] = binding.etNoOfOCP.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ADS_CARE_Q10] = binding.etNoOfCC.text.toString().toIntOrNull() ?: 0
            onSaveData(map)
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}