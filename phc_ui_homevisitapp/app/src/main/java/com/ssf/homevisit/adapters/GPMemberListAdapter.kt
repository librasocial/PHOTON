package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemGpInviteeBinding
import com.ssf.homevisit.models.ContentGP

class GPMemberListAdapter(val listener: (data: ContentGP) -> Unit) :
    ListAdapter<ContentGP, GPMemberListAdapter.GPInviteeViewHolder>(GPMemberDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GPInviteeViewHolder {
        return GPInviteeViewHolder(
            ItemGpInviteeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: GPInviteeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GPInviteeViewHolder(val view: ItemGpInviteeBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: ContentGP) = with(view) {
            contentGP = item
            checkbox.setOnCheckedChangeListener(null)
            checkbox.isChecked = item.isSelected
            checkbox.setOnCheckedChangeListener { _, _ ->
                item.isSelected = !item.isSelected
                submitList(currentList)
                listener(item)
            }
        }
    }
}

class GPMemberDiff : DiffUtil.ItemCallback<ContentGP>() {
    override fun areItemsTheSame(
        oldItem: ContentGP,
        newItem: ContentGP
    ): Boolean {
        return oldItem.targetNode.id == newItem.targetNode.id
    }

    override fun areContentsTheSame(
        oldItem: ContentGP,
        newItem: ContentGP
    ): Boolean {
        return oldItem == newItem
    }
}
