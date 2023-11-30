package com.ssf.homevisit.rmncha.childCare.mapping

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemChildCareSelectionBinding
import com.ssf.homevisit.models.ChildInHouseContent
import com.ssf.homevisit.rmncha.base.RMNCHAUtils

class HouseholdMemberAdapter(val onClick: (item: ChildInHouseContent) -> Unit) :
    ListAdapter<ChildInHouseContent, HouseholdMemberAdapter.ItemViewholder>(
        DiffCallChildInHouseContent()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(ItemChildCareSelectionBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemChildCareSelectionBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: ChildInHouseContent) {
            view.member = item
            val age = RMNCHAUtils.getBabyAgeFromDOB(item.sourceNode.properties.dateOfBirth)
            view.ageGender.text=RMNCHAUtils.getBabyAgeInWeeks(age).plus(" - ").plus(item.sourceNode.properties.gender)
            view.root.setOnClickListener {
                onClick.invoke(item)
            }
        }
    }
}

class DiffCallChildInHouseContent :
    DiffUtil.ItemCallback<ChildInHouseContent?>() {
    override fun areItemsTheSame(
        oldItem: ChildInHouseContent,
        newItem: ChildInHouseContent
    ): Boolean {
        return oldItem.sourceNode.id == newItem.sourceNode.id
    }

    override fun areContentsTheSame(
        oldItem: ChildInHouseContent,
        newItem: ChildInHouseContent
    ): Boolean {
        return oldItem == newItem
    }
}