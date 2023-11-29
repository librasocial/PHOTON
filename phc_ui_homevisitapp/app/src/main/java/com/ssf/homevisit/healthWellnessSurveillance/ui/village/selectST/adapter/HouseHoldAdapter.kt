package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.HouseholdDataSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.HouseHoldDiffUtils

class HouseHoldAdapter(
    private val oldList: MutableList<HouseHoldDataItem>, private val listener: OnHouseHoldItemClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun setData(newList: MutableList<HouseHoldDataItem>) {
        val diffCallback = HouseHoldDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return HouseHoldViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.household_data_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HouseHoldViewHolder).bind(oldList[position])
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    private inner class HouseHoldViewHolder(val binding: HouseholdDataSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HouseHoldDataItem) {
            if (data.action == "Select to Update Sample Details") {
                binding.tvHouseholdNumber.text = data.name
                binding.tvHouseholdName.text = data.age.toString()
                if (data.nameHouseHoldHead.isNullOrEmpty()) {
                    binding.tvNumberMembers.text = "-------"
                } else {
                    binding.tvNumberMembers.text = data.nameHouseHoldHead
                }
                binding.tvAction.text = data.action

            } else {
                if (!data.nameHouseHoldHead.isNullOrEmpty()) {
                    binding.tvHouseholdName.text = data.nameHouseHoldHead
                } else {
                    binding.tvHouseholdName.text = "------------"
                }
                binding.tvHouseholdNumber.text = data.houseHoldNumber
                binding.tvNumberMembers.text = data.memberCount
                binding.tvAction.text = data.action
            }
            binding.tvAction.setOnClickListener {
                listener.onHouseHoldItemClick(data)
            }
        }

    }

    interface OnHouseHoldItemClick {
        fun onHouseHoldItemClick(data: HouseHoldDataItem)
    }

}