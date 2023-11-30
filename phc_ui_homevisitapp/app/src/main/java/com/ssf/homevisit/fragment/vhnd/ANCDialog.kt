package com.ssf.homevisit.fragment.vhnd

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.AncTopicsDialogBinding

class ANCDialog(
    context: Context,
    selection: Map<String, Int>,
    onSaveData: (map: HashMap<String, Int>) -> Unit
) : Dialog(context, false, null) {
    private val binding = AncTopicsDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > VHND > "
        binding.toolbar.destination = "Enter Details for Ante Natal Care"
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        binding.apply {
            etNoOfANC.setText(selection[VHNDTopics.ANC_Q1]?.toString() ?: "0")
            etTTInjection.setText(selection[VHNDTopics.ANC_Q2]?.toString() ?: "0")
            etNoOfBloodPressure.setText(selection[VHNDTopics.ANC_Q3]?.toString() ?: "0")
            etBloodTests.setText(selection[VHNDTopics.ANC_Q4]?.toString() ?: "0")
            etAbnormalExams.setText(selection[VHNDTopics.ANC_Q5]?.toString() ?: "0")
            etCounsellingDietRest.setText(selection[VHNDTopics.ANC_Q6]?.toString() ?: "0")
            etIFBTablets.setText(selection[VHNDTopics.ANC_Q7]?.toString() ?: "0")
            etCalciumTablet.setText(selection[VHNDTopics.ANC_Q8]?.toString() ?: "0")
            etInstDelivery.setText(selection[VHNDTopics.ANC_Q9]?.toString() ?: "0")
            etRefferedDueToSign.setText(selection[VHNDTopics.ANC_Q10]?.toString() ?: "0")
        }
        binding.btnSave.setOnClickListener {
            val map = hashMapOf<String, Int>()
            map[VHNDTopics.ANC_Q1] = binding.etNoOfANC.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q2] = binding.etTTInjection.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q3] = binding.etNoOfBloodPressure.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q4] = binding.etBloodTests.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q5] = binding.etAbnormalExams.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q6] =
                binding.etCounsellingDietRest.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q7] = binding.etIFBTablets.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q8] = binding.etCalciumTablet.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q9] = binding.etInstDelivery.text.toString().toIntOrNull() ?: 0
            map[VHNDTopics.ANC_Q10] = binding.etRefferedDueToSign.text.toString().toIntOrNull() ?: 0
            onSaveData(map)
            dismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}