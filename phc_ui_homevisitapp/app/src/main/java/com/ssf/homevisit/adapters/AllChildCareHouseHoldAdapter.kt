package com.ssf.homevisit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.ItemAllHhListBinding
import com.ssf.homevisit.models.RMNCHAServiceStatus
import com.ssf.homevisit.models.TargetNodeCCHouseHoldProperties

class AllChildCareHouseHoldAdapter(val listener: (data: TargetNodeCCHouseHoldProperties) -> Unit) :
    ListAdapter<TargetNodeCCHouseHoldProperties, AllChildCareHouseHoldAdapter.CCHouseHoldViewHolder>(
        CCHouseHoldDiff()
    ) {
    var context:Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CCHouseHoldViewHolder {
        return CCHouseHoldViewHolder(
            ItemAllHhListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CCHouseHoldViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CCHouseHoldViewHolder(val view: ItemAllHhListBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: TargetNodeCCHouseHoldProperties) = with(view) {
            listItem = item
            if (item.hasRegisteredChildCare || item.hasRegisteredAdolescentCare) {
                context?.resources?.getColor(R.color.green)?.let { this.txtHeadAction.setTextColor(it) };
            }
            else{
                context?.resources?.getColor(R.color.button_blue)?.let { this.txtHeadAction.setTextColor(it) };
            }
            view.txtHeadAction.setOnClickListener {
                listener.invoke(item)
            }
        }
    }
}

class CCHouseHoldDiff : DiffUtil.ItemCallback<TargetNodeCCHouseHoldProperties>() {
    override fun areItemsTheSame(
        oldItem: TargetNodeCCHouseHoldProperties,
        newItem: TargetNodeCCHouseHoldProperties
    ): Boolean {
        return oldItem.uuid == newItem.uuid
    }

    override fun areContentsTheSame(
        oldItem: TargetNodeCCHouseHoldProperties,
        newItem: TargetNodeCCHouseHoldProperties
    ): Boolean {
        return oldItem == newItem
    }
}
