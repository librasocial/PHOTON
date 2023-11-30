package com.ssf.homevisit.healthWellnessSurveillance.common.extension

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun ImageView.loadImage(url: String?, cornerRadius: Int) {
    url?.let {
        Glide.with(this.context)
            .load(it)
            .transform(CenterInside(), RoundedCorners(cornerRadius))
            .into(this)
    }
}

fun ImageView.setDrawable(@DrawableRes drawableId: Int) {
    this.setImageDrawable(ContextCompat.getDrawable(context, drawableId))
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.inVisible() {
    this.visibility = View.INVISIBLE
}

fun View.inverseVisibility() {
    if (this.isVisible)
        this.inVisible()
    else
        this.visible()
}