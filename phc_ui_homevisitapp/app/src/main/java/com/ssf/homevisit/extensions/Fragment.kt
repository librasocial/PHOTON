package com.ssf.homevisit.extensions

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.CustomDialogBinding

inline fun Fragment.apiSuccessFailureDialog(
    isSuccess: Boolean = false,
    titleText: String? = null,
    buttonText: String? = null,
    crossinline successButtonClick: () -> Unit = {},
    crossinline failureButtonCLick: () -> Unit = {}
) {
    context?.let {
        val dialog = AlertDialog.Builder(it, R.style.ThemeOverlay_Material3_Dialog_Alert)
        val customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
        dialog.setView(customDialogBinding.root)
        dialog.setCancelable(false)
        val alertDialog = dialog.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (isSuccess) {
            customDialogBinding.dialogIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    it, R.drawable.ic_tick
                )
            )
            if (titleText == null) {
                customDialogBinding.title.text =
                    getString(R.string.form_has_been_saved_successfully)
            } else {
                customDialogBinding.title.text = titleText
            }
            if (buttonText == null) {
                customDialogBinding.button.text = getString(R.string.click_here_to_exit)
            } else {
                customDialogBinding.button.text = buttonText
            }

        } else {
            customDialogBinding.dialogIcon.setImageDrawable(
                ContextCompat.getDrawable(
                    it, R.drawable.exit_error
                )
            )
            if (titleText == null) {
                customDialogBinding.title.text =
                    getString(R.string.would_you_like_to_exit_without_saving)
            } else {
                customDialogBinding.title.text = titleText
            }
            if (buttonText == null) {
                customDialogBinding.button.text = getString(R.string.exit)
            } else {
                customDialogBinding.button.text = buttonText
            }
        }

        customDialogBinding.btnCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        customDialogBinding.button.setOnClickListener {
            alertDialog.dismiss()
            if (isSuccess)
                successButtonClick.invoke()
            else
                failureButtonCLick.invoke()
        }
        alertDialog.show()
    }
}