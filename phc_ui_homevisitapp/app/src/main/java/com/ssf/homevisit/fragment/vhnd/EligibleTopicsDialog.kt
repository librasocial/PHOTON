package com.ssf.homevisit.fragment.vhnd

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.DialogEligibleCoupleTopicsBinding

class EligibleTopicsDialog(context: Context,selection: Map<String, Int>, onSaveData: (map: HashMap<String, Int>)->Unit): Dialog(context, false, null) {
    private val binding = DialogEligibleCoupleTopicsBinding.inflate(LayoutInflater.from(context))
    init {
        binding.toolbar.path = "Programs > VHND > "
        binding.toolbar.destination = "Enter Details for Eligible Couple"
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        binding.apply {
            etNoOfCouples.setText(selection[VHNDTopics.ELIGIBLE_COUPLE_Q1]?.toString() ?: "0")
            etFamilyPlanning.setText(selection[VHNDTopics.ELIGIBLE_COUPLE_Q2]?.toString() ?: "0")
            etECs.setText(selection[VHNDTopics.ELIGIBLE_COUPLE_Q3]?.toString() ?: "0")
            etECsReceivedCC.setText(selection[VHNDTopics.ELIGIBLE_COUPLE_Q4]?.toString() ?: "0")
            etIUCD.setText(selection[VHNDTopics.ELIGIBLE_COUPLE_Q5]?.toString() ?: "0")
        }
        binding.btnSave.setOnClickListener {
            val map = hashMapOf<String, Int>()
            map[VHNDTopics.ELIGIBLE_COUPLE_Q1] = binding.etNoOfCouples.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ELIGIBLE_COUPLE_Q2] = binding.etFamilyPlanning.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ELIGIBLE_COUPLE_Q3] = binding.etECs.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ELIGIBLE_COUPLE_Q4] = binding.etECsReceivedCC.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ELIGIBLE_COUPLE_Q5] = binding.etIUCD.text.toString().toIntOrNull() ?: 0
            onSaveData(map)
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}