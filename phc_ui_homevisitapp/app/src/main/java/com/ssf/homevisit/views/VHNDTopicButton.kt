package com.ssf.homevisit.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.VhndTopicButtonBinding

class VHNDTopicButton : FrameLayout {
    private lateinit var binding: VhndTopicButtonBinding
    private lateinit var btnIcon: Drawable
    private var label = ""
    private var checked = false

    constructor(context: Context) : super(context) {
        binding = VhndTopicButtonBinding.inflate(LayoutInflater.from(context), this, true)
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        binding = VhndTopicButtonBinding.inflate(LayoutInflater.from(context), this, true)
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        binding = VhndTopicButtonBinding.inflate(LayoutInflater.from(context), this, true)
        init(attrs, defStyle)
    }

    constructor(context: Context, @DrawableRes drawable: Int, label: String) : super(context) {
        try {
            btnIcon = AppCompatResources.getDrawable(context, drawable)!!
            this.label = label
            binding = VhndTopicButtonBinding.inflate(LayoutInflater.from(context), this, true)
            bindUI()
        } catch (e: Exception) {
            println("Invalid drawable resource")
        }
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VHNDTopicButton, 0, 0)
        try {
            checked = typedArray.getBoolean(R.styleable.VHNDTopicButton_BtnChecked, false)
            label = typedArray.getString(R.styleable.VHNDTopicButton_BtnLabel) ?: ""
            btnIcon = typedArray.getDrawable(R.styleable.VHNDTopicButton_BtnIcon)!!
        } catch (e: Exception) {
            // ignore
        } finally {
            typedArray.recycle()
        }
        bindUI()
    }

    fun setChecked(checked: Boolean) {
        this.checked = checked
        binding.isChecked = this.checked
    }

    fun getChecked() = checked

    private fun bindUI() {
        binding.btnText.text = label
        binding.imageIcon.setImageDrawable(btnIcon)
        binding.isChecked = checked
    }
}
