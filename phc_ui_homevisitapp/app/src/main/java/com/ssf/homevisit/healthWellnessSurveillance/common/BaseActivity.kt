package com.ssf.homevisit.healthWellnessSurveillance.common

import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.setContentView

abstract class BaseActivity<V : ViewDataBinding>(@LayoutRes private val layOutId: Int) :
    AppCompatActivity() {
    lateinit var activityBinding: V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = layOutId.setContentView(this)
        setContentView(activityBinding.root)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val v = currentFocus
        try {
            if (v != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
                v is EditText &&
                !v.javaClass.name.startsWith("android.webkit.")
            ) {
                val sourceCoordinates = IntArray(2)
                v.getLocationOnScreen(sourceCoordinates)
                val x = ev.rawX + v.getLeft() - sourceCoordinates[0]
                val y = ev.rawY + v.getTop() - sourceCoordinates[1]
                if (x < v.getLeft() || x > v.getRight() || y < v.getTop() || y > v.getBottom()) {
                    hideKeyboard()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard() {
        if (window != null) {
            window.decorView
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
        }
    }
}