package com.ssf.homevisit.rmncha.adolescentCare.dialogs

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.AdCareCommoditiesDialogBinding
import com.ssf.homevisit.rmncha.adolescentCare.AdCareServiceTopics
import com.ssf.homevisit.rmncha.adolescentCare.AdolescentCareServiceViewModel

class CommodotiesDialog(
    context: Context,
    onSaveData: (hasAnyData: Boolean) -> Unit
) : Dialog(context, false, null) {

    private val binding = AdCareCommoditiesDialogBinding.inflate(LayoutInflater.from(context))

    init {
        binding.toolbar.path = "Programs > Adolescent Care > "
        binding.toolbar.destination = "Enter Details on Commodities Provided"
        binding.toolbar.imgCross.setOnClickListener {
            dismiss()
        }
        setRadioListener()
        binding.apply {
            with(AdolescentCareServiceViewModel.commoditiesData) {
                val selected =
                    this@CommodotiesDialog.context.resources.getStringArray(R.array.type_of_contraception)
                        .indexOfFirst { it == get(AdCareServiceTopics.COMMODITIES_Q5) as String }
                val typeOfContraceptive = if (selected > -1) {
                    this@CommodotiesDialog.context.resources.getStringArray(R.array.type_of_contraception)
                        .indexOfFirst { it == get(AdCareServiceTopics.COMMODITIES_Q5) as String }
                } else 0
                spinnerTypeOfContraceptive.setSelection(typeOfContraceptive)
                etNoOfContraceptive.setText(get(AdCareServiceTopics.COMMODITIES_Q6) as String)
            }
        }
        binding.btnSave.setOnClickListener {
            if (isValidData()) {
                onSaveData(hasAnInput())
                dismiss()
            }
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }

    private fun hasAnInput(): Boolean {
        val isIFB =
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q1] as Boolean
        val isAblendazole =
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q2] as Boolean
        val isSanitaryNapkin =
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q3] as Boolean
        val isContraceptive =
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q4] as Boolean
        return isIFB || isAblendazole || isSanitaryNapkin || isContraceptive
    }

    private fun isValidData(): Boolean {
        val isContraceptive =
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q4] as Boolean
        val typeOfContraceptive =
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q5] as? String
        if (isContraceptive && typeOfContraceptive.isNullOrEmpty()) {
            Toast.makeText(context, "Select type of contraceptive", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun setRadioListener() {
        binding.radioGroupIFBTablet.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q1] =
                checkedId == binding.radioBtnIFBYes.id
        }
        binding.radioGroupAlbendazole.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q2] =
                checkedId == binding.radioBtnAlbendazoleYes.id
        }
        binding.radioGroupSanitaryNapkin.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q3] =
                checkedId == binding.radioBtnSanitaryNapkinYes.id
        }
        binding.radioGroupContraceptive.setOnCheckedChangeListener { group, checkedId ->
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q4] =
                checkedId == binding.radioBtnContraceptiveYes.id
            binding.contraceptiveGroup.isVisible =
                AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q4] as Boolean
        }

        binding.etNoOfContraceptive.doAfterTextChanged {
            AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q6] =
                it.toString()
        }

        binding.spinnerTypeOfContraceptive.onItemSelectedListener =
            object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val list =
                        this@CommodotiesDialog.context.resources.getStringArray(R.array.type_of_contraception)
                    AdolescentCareServiceViewModel.commoditiesData[AdCareServiceTopics.COMMODITIES_Q5] =
                        list[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }
    }
}