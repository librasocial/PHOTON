package com.ssf.homevisit.fragment.vhsnc

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import androidx.annotation.DrawableRes
import com.ssf.homevisit.databinding.DialogInvitationSentBinding

class InvitationSentDialog(context: Context,  @DrawableRes icon: Int? = null, msg: String?=null, onDismiss:()->Unit): Dialog(context) {
    private val binding = DialogInvitationSentBinding.inflate(LayoutInflater.from(context))
    init {
        icon?.let {
            binding.img.setImageResource(icon)
        }
        msg?.let {
            binding.txtMsg.text = it
        }
        binding.close.setOnClickListener {
            dismiss()
            onDismiss()
        }
        binding.txtBtn.setOnClickListener {
            dismiss()
            onDismiss()
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setContentView(binding.root)
    }
}