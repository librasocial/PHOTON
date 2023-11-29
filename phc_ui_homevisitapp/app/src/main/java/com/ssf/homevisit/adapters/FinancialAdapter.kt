package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.PhysicalInfraSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.data.Options


class FinancialAdapter(private val listener: OnFinanceItemClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Options> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return FinancialViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.physical_infra_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as FinancialViewHolder).bind(data[position], position)
    }

    private inner class FinancialViewHolder(val binding: PhysicalInfraSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Options, position: Int) {
            binding.tvFacilityName.text = data.question
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                listener.onFinanceSelect(
                    position, data.question, isYesSelected = checkedId == R.id.radio_yes,data.propertyName
                )
            }

        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


    interface OnFinanceItemClick {
        fun onFinanceSelect(
            position: Int,
            item: String,
            isYesSelected: Boolean = false,
            propertyName: String
        )
    }
}
