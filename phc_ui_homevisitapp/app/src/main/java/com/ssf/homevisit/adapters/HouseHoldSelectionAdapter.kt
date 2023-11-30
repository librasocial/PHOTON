package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemSelectParticipantBinding
import com.ssf.homevisit.databinding.ItemVenueSelectionBinding
import com.ssf.homevisit.models.HouseHoldInVillageResponse
import com.ssf.homevisit.models.Venue

class HouseHoldSelectionAdapter(
    val selectionListener: (houseHold: HouseHoldInVillageResponse.Content) -> Unit = {}
) :
    ListAdapter<HouseHoldInVillageResponse.Content, HouseHoldSelectionAdapter.ItemViewholder>(DiffCallbackHouseHold()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ItemSelectParticipantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: HouseHoldSelectionAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemSelectParticipantBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: HouseHoldInVillageResponse.Content) = with(view) {
            view.houseHold = item
            view.txtAction.setOnClickListener {
                selectionListener(item)
            }
        }
    }
}

class DiffCallbackHouseHold : DiffUtil.ItemCallback<HouseHoldInVillageResponse.Content>() {
    override fun areItemsTheSame(oldItem: HouseHoldInVillageResponse.Content, newItem: HouseHoldInVillageResponse.Content): Boolean {
        return oldItem.target.properties.houseID == newItem.target.properties.houseID
    }

    override fun areContentsTheSame(oldItem: HouseHoldInVillageResponse.Content, newItem: HouseHoldInVillageResponse.Content): Boolean {
        return oldItem == newItem
    }
}