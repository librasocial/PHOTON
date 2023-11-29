package com.ssf.homevisit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.ItemAdolescentCareListBinding
import com.ssf.homevisit.models.CcChildListContent
import com.ssf.homevisit.models.RMNCHAServiceStatus

class AllAdolescentCareChildrenAdapter(val listener: (data: CcChildListContent) -> Unit) :
    ListAdapter<CcChildListContent, AllAdolescentCareChildrenAdapter.ACChildrenHoldViewHolder>(
        ACChildrenDiff()
    ) {

    var context: Context?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ACChildrenHoldViewHolder {
        return ACChildrenHoldViewHolder(
            ItemAdolescentCareListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ACChildrenHoldViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ACChildrenHoldViewHolder(val view: ItemAdolescentCareListBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: CcChildListContent) = with(view) {
            listItem = item
            if (item.sourceNode.properties.rmnchaServiceStatus == RMNCHAServiceStatus.CHILDCARE_REGISTERED.name || item.sourceNode.properties.rmnchaServiceStatus == RMNCHAServiceStatus.CHILDCARE_ONGOING.name) {
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

class ACChildrenDiff : DiffUtil.ItemCallback<CcChildListContent>() {
    override fun areItemsTheSame(
        oldItem: CcChildListContent,
        newItem: CcChildListContent
    ): Boolean {
        return oldItem.sourceNode.properties.uuid == newItem.sourceNode.properties.uuid
    }

    override fun areContentsTheSame(
        oldItem: CcChildListContent,
        newItem: CcChildListContent
    ): Boolean {
        return oldItem == newItem
    }
}
