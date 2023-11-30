package com.ssf.homevisit.fragment.vhsnc

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Window
import com.ssf.homevisit.databinding.DialogPickImageBinding

class PickImageDialog(context: Context, onSelectOption: (pickFromGallery: Boolean) -> Unit) :
    Dialog(context) {
    private val binding = DialogPickImageBinding.inflate(LayoutInflater.from(context))

    init {
        binding.txtCamera.setOnClickListener {
            dismiss()
            onSelectOption(false)
        }
        binding.txtGallery.setOnClickListener {
            dismiss()
            onSelectOption(true)
        }
        binding.icClose.setOnClickListener { dismiss() }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setGravity(Gravity.CENTER)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}