package com.ssf.homevisit.fragment.vhnd

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.PostNatalCareTopicsDialogBinding

class PNCDialog(
    context: Context,
    selection: Map<String, Int>,
    onSaveData: (map: HashMap<String, Int>) -> Unit
) : Dialog(context, false, null) {
    private val binding = PostNatalCareTopicsDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > VHND > "
        binding.toolbar.destination = "Enter Details for Post Natal Care"
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        binding.apply {
            etNoOfPNC.setText(selection[VHNDTopics.PNC_Q1]?.toString() ?: "0")
            etNoOfInfants.setText(selection[VHNDTopics.PNC_Q2]?.toString() ?: "0")
            etTTInjection.setText(selection[VHNDTopics.PNC_Q3]?.toString() ?: "0")
            etNoOfBloodPressure.setText(selection[VHNDTopics.PNC_Q4]?.toString() ?: "0")
            etBloodTests.setText(selection[VHNDTopics.PNC_Q5]?.toString() ?: "0")
            etAbnormalExams.setText(selection[VHNDTopics.PNC_Q6]?.toString() ?: "0")
            etCounsellingDietRest.setText(selection[VHNDTopics.PNC_Q7]?.toString() ?: "0")
            etInfantsRef.setText(selection[VHNDTopics.PNC_Q8]?.toString() ?: "0")
        }
        binding.btnSave.setOnClickListener {
            val map = hashMapOf<String, Int>()
            map[VHNDTopics.PNC_Q1] = binding.etNoOfPNC.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q2] = binding.etNoOfInfants.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q3] = binding.etTTInjection.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q4] = binding.etNoOfBloodPressure.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q5] = binding.etBloodTests.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q6] = binding.etAbnormalExams.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q7] =
                binding.etCounsellingDietRest.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.PNC_Q8] =
                binding.etInfantsRef.text.toString().toIntOrNull() ?: 0
            onSaveData(map)
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}