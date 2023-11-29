package com.ssf.homevisit.utils

import android.graphics.Bitmap

fun resizeBitmap(image: Bitmap, maxHeight: Int, maxWidth: Int): Bitmap {

    if (maxHeight > 0 && maxWidth > 0) {

        val sourceWidth: Int = image.width
        val sourceHeight: Int = image.height

        var targetWidth = maxWidth
        var targetHeight = maxHeight

        val sourceRatio = sourceWidth.toFloat() / sourceHeight.toFloat()
        val targetRatio = maxWidth.toFloat() / maxHeight.toFloat()

        if (targetRatio > sourceRatio) {
            targetWidth = (maxHeight.toFloat() * sourceRatio).toInt()
        } else {
            targetHeight = (maxWidth.toFloat() / sourceRatio).toInt()
        }

        return Bitmap.createScaledBitmap(
            image, targetWidth, targetHeight, true
        )
    } else {

        return image
    }
}