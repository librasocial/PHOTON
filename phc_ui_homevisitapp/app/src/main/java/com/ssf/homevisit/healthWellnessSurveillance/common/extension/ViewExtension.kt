package com.ssf.homevisit.healthWellnessSurveillance.common.extension

import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.ssf.homevisit.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Button.enableButton() {
    isEnabled = true
    isClickable = true
    context?.getColor(R.color.button_dark_blue)
        ?.let { setBackgroundColor(it) }
}

fun Button.disableButton() {
    isEnabled = false
    isClickable = false
    context?.getColor(R.color.light_blue)
        ?.let { setBackgroundColor(it) }
}

fun ImageView.loadAuthImage(url: String, onSuccess: () -> Unit = {}, onFailed: () -> Unit = {}) {
    CoroutineScope(Dispatchers.Main).launch {
        Glide.with(this@loadAuthImage)
            .load(url)
            .timeout(60000)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    onSuccess.invoke()
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    onFailed.invoke()
                    return false
                }
            })
    }
}