package com.ssf.homevisit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.ssf.homevisit.R


class MultiSelectAdapter<T> constructor(
    val context: Context,
    private val headerText: String,
    private val options: List<SpinnerItem<T>>,
    private val selectedOptions: MutableSet<T>
) : BaseAdapter() {
    class SpinnerItem<T>(val item: T, val txt: String)

    override fun getCount(): Int {
        return options.size + 1
    }

    override fun getItem(position: Int): SpinnerItem<T>? {
        return if (position < 1) {
            null
        } else {
            options[position - 1]
        }
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var mConvertView = convertView
        var holder = ViewHolder(null, null)
        mConvertView?.let {
            holder = it.tag as ViewHolder
        } ?: run {
            val layoutInflator = LayoutInflater.from(context)
            mConvertView = layoutInflator.inflate(R.layout.spinner_text_layout, parent, false)
            holder = ViewHolder()
            holder.mTextView = convertView?.findViewById(R.id.text)
            holder.mCheckBox = convertView?.findViewById(R.id.checkbox)
            convertView?.tag = holder
        }
        if (position < 1) {
            holder.mCheckBox?.visibility = View.GONE
            holder.mTextView?.text = headerText
        } else {
            val listPos = position - 1
            holder.mCheckBox?.visibility = View.VISIBLE
            holder.mTextView?.text = options[listPos].txt
            val item = options[listPos].item
            val isSel = selectedOptions.contains(item)
            holder.mCheckBox?.setOnCheckedChangeListener(null)
            holder.mCheckBox?.isChecked = isSel
            holder.mCheckBox?.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    selectedOptions.add(item)
                } else {
                    selectedOptions.remove(item)
                }
            }
            holder.mTextView?.setOnClickListener { holder.mCheckBox?.toggle() }
        }
        return convertView
    }

    private data class ViewHolder(
        var mTextView: TextView? = null,
        var mCheckBox: CheckBox? = null
    )
} 