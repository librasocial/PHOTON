package com.ssf.homevisit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.EnvironmentQualitySingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.data.Options

class EnvironmentQualityAdapter(
    private val context: Context, private val listener: EnvironmentQualityItemSelected?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), AdapterView.OnItemSelectedListener {

    var data: List<Options> = mutableListOf()
    var adapterPosition:Int=-1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return EnvironmentQualityViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.environment_quality_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapterPosition=position
        (holder as EnvironmentQualityViewHolder).bind(data[position])
    }

    private inner class EnvironmentQualityViewHolder(val binding: EnvironmentQualitySingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Options) {
            binding.tvHeading.text = data.question
            binding.spinner.onItemSelectedListener = this@EnvironmentQualityAdapter
            context.let { context ->
                val choicesList = data.choices as MutableList<String>
                if (!choicesList.contains("Select")) {
                    choicesList.add(0, "Select")
                }
                val arrayAdapter = ArrayAdapter(
                    context, android.R.layout.simple_spinner_item, choicesList
                )
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.spinner.adapter = arrayAdapter
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        listener?.onItemSelect(p0?.getItemAtPosition(p2).toString(),adapterPosition,data[adapterPosition].propertyName)
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    interface EnvironmentQualityItemSelected {
        fun onItemSelect(item: String, position: Int, propertyName: String)
    }
}