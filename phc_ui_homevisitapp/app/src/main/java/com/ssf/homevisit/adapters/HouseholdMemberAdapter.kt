package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemHouseholdIndividualBinding
import com.ssf.homevisit.models.ResidentInHouseHoldResponse

class HouseholdMemberAdapter(val sentInvites: List<String>?, val onSelectionUpdate: () -> Unit) :
    ListAdapter<ResidentInHouseHoldResponse.Content, HouseholdMemberAdapter.ItemViewholder>(
        DiffCallbackHouseHoldMember()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(ItemHouseholdIndividualBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemHouseholdIndividualBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: ResidentInHouseHoldResponse.Content) =
            with(itemView) {
                item.inviteSent = sentInvites?.contains(item.target.properties.uuid)?:false
                view.member = item
                setOnClickListener {
//                    if (item.inviteSent)
//                        return@setOnClickListener
                    item.checked = !(item.checked ?: false)
                    notifyItemChanged(adapterPosition)
                    onSelectionUpdate()
                }
            }
    }
}

class DiffCallbackHouseHoldMember :
    DiffUtil.ItemCallback<ResidentInHouseHoldResponse.Content?>() {
    override fun areItemsTheSame(
        oldItem: ResidentInHouseHoldResponse.Content,
        newItem: ResidentInHouseHoldResponse.Content
    ): Boolean {
        return oldItem.target.id == newItem.target.id
    }

    override fun areContentsTheSame(
        oldItem: ResidentInHouseHoldResponse.Content,
        newItem: ResidentInHouseHoldResponse.Content
    ): Boolean {
        return oldItem == newItem
    }
}