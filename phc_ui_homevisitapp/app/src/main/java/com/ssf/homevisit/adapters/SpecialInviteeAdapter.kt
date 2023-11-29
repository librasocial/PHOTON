package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemSpecialInviteeBinding
import com.ssf.homevisit.models.SpecialInvitee

class SpecialInviteeAdapter(val onDeleteItem: (item: SpecialInvitee, position: Int)->Unit) :
    ListAdapter<SpecialInvitee, SpecialInviteeAdapter.ItemViewholder>(DiffCallbackSpecialInvitee()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ItemSpecialInviteeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemSpecialInviteeBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: SpecialInvitee) {
            view.specialInvitee = item
            view.txtSINumber.text = (adapterPosition+1).toString()
            view.txtDelete.setOnClickListener {
                onDeleteItem(item, adapterPosition)
//                notifyItemRemoved(adapterPosition)
            }
        }
    }
}

class DiffCallbackSpecialInvitee : DiffUtil.ItemCallback<SpecialInvitee>() {
    override fun areItemsTheSame(oldItem: SpecialInvitee, newItem: SpecialInvitee): Boolean {
        return oldItem.name.value == newItem.name.value
    }

    override fun areContentsTheSame(oldItem: SpecialInvitee, newItem: SpecialInvitee): Boolean {
        return oldItem == newItem
    }
}