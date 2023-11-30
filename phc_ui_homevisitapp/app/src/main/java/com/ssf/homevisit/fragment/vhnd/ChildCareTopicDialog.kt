package com.ssf.homevisit.fragment.vhnd

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.ChildCareTopicsDialogBinding

class ChildCareTopicDialog(
    context: Context,
    selection: Map<String, Int>,
    onSaveData: (map: HashMap<String, Int>) -> Unit
) : Dialog(context, false, null) {
    private val binding = ChildCareTopicsDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > VHND > "
        binding.toolbar.destination = "Enter Details for Child Care"
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        binding.apply {
            etNoOfPNC.setText(selection[VHNDTopics.CHILD_CARE_Q1]?.toString() ?: "0")
            etBelow1Yr.setText(selection[VHNDTopics.CHILD_CARE_Q2]?.toString() ?: "0")
            et12Yr.setText(selection[VHNDTopics.CHILD_CARE_Q3]?.toString() ?: "0")
            et23Yr.setText(selection[VHNDTopics.CHILD_CARE_Q4]?.toString() ?: "0")
            et34Yr.setText(selection[VHNDTopics.CHILD_CARE_Q5]?.toString() ?: "0")
            et45Yr.setText(selection[VHNDTopics.CHILD_CARE_Q6]?.toString() ?: "0")
            et56Yr.setText(selection[VHNDTopics.CHILD_CARE_Q7]?.toString() ?: "0")
            etReferralBelow2.setText(selection[VHNDTopics.CHILD_CARE_Q8]?.toString() ?: "0")
            etMedicationBelow2.setText(selection[VHNDTopics.CHILD_CARE_Q9]?.toString() ?: "0")
            etNoOfORS.setText(selection[VHNDTopics.CHILD_CARE_Q10]?.toString() ?: "0")
        }
        binding.btnSave.setOnClickListener {
            val map = hashMapOf<String, Int>()
            map[VHNDTopics.CHILD_CARE_Q1] = binding.etNoOfPNC.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q2] = binding.etBelow1Yr.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q3] = binding.et12Yr.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q4] = binding.et23Yr.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q5] = binding.et34Yr.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q6] = binding.et45Yr.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q7] = binding.et56Yr.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q8] =
                binding.etReferralBelow2.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q9] =
                binding.etMedicationBelow2.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.CHILD_CARE_Q10] = binding.etNoOfORS.text.toString().toIntOrNull() ?: 0
            onSaveData(map)
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}