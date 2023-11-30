package com.ssf.homevisit.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentWaterSourcesCountSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.ResourceCount


class WaterResourceCountAdapter(private val listener:OnCountCheck) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data: MutableList<ResourceCount> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ResourceCountViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fragment_water_sources_count_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ResourceCountViewHolder).bind(data[position], position)
        holder.setIsRecyclable(false)
    }

    private inner class ResourceCountViewHolder(val binding: FragmentWaterSourcesCountSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ResourceCount, position: Int) {
            binding.tvResourcesName.text = "No. of " + data.menuItem + "s"

            binding.etResourceCount.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                if(s.toString() != ""){
                    listener.onCountCheck(s.toString(), data.menuItem, position,data.propertyName)
                }
            }
            override fun beforeTextChanged(
                    s: CharSequence?, start: Int, count: Int, after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
            binding.etResourceCount.setText(data.data)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnCountCheck{
        fun onCountCheck(count: String, optionName: String?, position: Int, propertyName: String?)
    }
}

