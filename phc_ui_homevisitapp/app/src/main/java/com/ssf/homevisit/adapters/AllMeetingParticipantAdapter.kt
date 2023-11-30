package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemAllInviteeBinding
import com.ssf.homevisit.fragment.vhsnc.AllInviteeItem

class AllMeetingParticipantAdapter :
    ListAdapter<AllInviteeItem, AllMeetingParticipantAdapter.AllInviteeViewHolder>(
        AllParticipantDiff()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllInviteeViewHolder {
        return AllInviteeViewHolder(
            ItemAllInviteeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: AllInviteeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AllInviteeViewHolder(val view: ItemAllInviteeBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: AllInviteeItem) = with(view) {
            txtSn.text = (adapterPosition + 1).toString()
            phcStaffData = item
        }
    }
}

class AllParticipantDiff : DiffUtil.ItemCallback<AllInviteeItem>() {
    override fun areItemsTheSame(
        oldItem: AllInviteeItem,
        newItem: AllInviteeItem
    ): Boolean {
        return oldItem.name == newItem.name && oldItem.contact == newItem.contact
    }

    override fun areContentsTheSame(
        oldItem: AllInviteeItem,
        newItem: AllInviteeItem
    ): Boolean {
        return oldItem == newItem
    }
}
