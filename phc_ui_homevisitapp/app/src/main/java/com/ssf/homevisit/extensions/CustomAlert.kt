package com.ssf.homevisit.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.IcAlertDialogBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible

inline fun Fragment.customAlert(
    titleText: String? = null,
    subTitleText: String? = null,
    icon: Int,
    buttonText: String? = null,
    exitFlow: Boolean = false,
    continueFlow: Boolean = false,
    crossinline ContinueButtonClick: () -> Unit = {},
    crossinline exitButtonClick: () -> Unit = {},
) {
    context?.let {
        val dialog = AlertDialog.Builder(it, R.style.ThemeOverlay_Material3_Dialog_Alert)
        val customDialogBinding = IcAlertDialogBinding.inflate(layoutInflater)
        dialog.setView(customDialogBinding.root)
        dialog.setCancelable(false)
        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        customDialogBinding.ivIcon.setImageDrawable(
            ContextCompat.getDrawable(
                it, icon
            )
        )
        if(buttonText != null){
            customDialogBinding.save.text = buttonText
        }
        if (titleText != null) {
            customDialogBinding.tvTitle.text = titleText
        } else {
            customDialogBinding.tvTitle.gone()
        }
        if (subTitleText != null) {
            customDialogBinding.tvSubTitile.text = subTitleText
        } else {
            customDialogBinding.tvSubTitile.gone()
        }
        if (exitFlow) {
            customDialogBinding.tvTitle.gravity = Gravity.CENTER
            customDialogBinding.exit.visible()
            customDialogBinding.save.gone()
            customDialogBinding.cancel.visible()
        }
        if (continueFlow) {
            customDialogBinding.exit.gone()
            customDialogBinding.save.visible()
            customDialogBinding.tvCancel.visible()
        }
        customDialogBinding.cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        customDialogBinding.save.setOnClickListener {
            alertDialog.dismiss()
            ContinueButtonClick.invoke()
        }
        customDialogBinding.exit.setOnClickListener {
            alertDialog.dismiss()
            exitButtonClick.invoke()
        }

        customDialogBinding.tvCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}