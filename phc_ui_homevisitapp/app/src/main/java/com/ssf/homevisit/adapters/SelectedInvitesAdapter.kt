package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.ItemInviteesBinding
import com.ssf.homevisit.models.Recipient

class InvitesAdapter(
    val hideAction: Boolean = false,
    val selectionListener: (houseHold: Recipient) -> Unit = {}
) :
    ListAdapter<Recipient, InvitesAdapter.ItemViewholder>(DiffCallInvites()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ItemInviteesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: InvitesAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemInviteesBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: Recipient) = with(view) {
            if (hideAction) {
                view.guideline2.setGuidelinePercent(.5f)
                view.guideline3.setGuidelinePercent(.73f)
                view.txtAction.isVisible = false
                view.guideline4.setGuidelinePercent(1f)
            }
            view.recipient = item
            val drawable = if (item.isCommiteeMember) R.drawable.ic_check_filled else R.drawable.ic_check
            view.txtAction.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0)
            view.txtAction.setOnClickListener {
                item.isCommiteeMember = !item.isCommiteeMember
                notifyItemChanged(adapterPosition)
                selectionListener(item)
            }
        }
    }
}

class DiffCallInvites : DiffUtil.ItemCallback<Recipient>() {
    override fun areItemsTheSame(oldItem: Recipient, newItem: Recipient): Boolean {
        return oldItem.citizenId == newItem.citizenId
    }

    override fun areContentsTheSame(oldItem: Recipient, newItem: Recipient): Boolean {
        return oldItem == newItem
    }
}