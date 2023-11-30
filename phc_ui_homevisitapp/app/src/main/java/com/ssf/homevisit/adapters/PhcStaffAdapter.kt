package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemPhcStaffBinding
import com.ssf.homevisit.models.PhcContent

class PhcStaffAdapter(val clickListener: (phcStaff: PhcContent) -> Unit) :
    ListAdapter<PhcContent, PhcStaffAdapter.PhcStaffViewholder>(DiffCallbackPhcStaff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhcStaffViewholder {
        return PhcStaffViewholder(
            ItemPhcStaffBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PhcStaffViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PhcStaffViewholder(val view: ItemPhcStaffBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: PhcContent) = with(view) {
            txtSn.text = (adapterPosition+1).toString()
            phcStaffData = item.targetNode.properties
        }
    }
}

class DiffCallbackPhcStaff : DiffUtil.ItemCallback<PhcContent>() {
    override fun areItemsTheSame(
        oldItem: PhcContent,
        newItem: PhcContent
    ): Boolean {
        return oldItem.targetNode.id == newItem.targetNode.id
    }

    override fun areContentsTheSame(
        oldItem: PhcContent,
        newItem: PhcContent
    ): Boolean {
        return oldItem.targetNode == newItem.targetNode
    }
}
