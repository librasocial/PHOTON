package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.databinding.ItemVenueSelectionBinding
import com.ssf.homevisit.models.PlacesInVillageResponse
import com.ssf.homevisit.models.Venue

class VenueListAdapter(
    val selectionListener: (venue: PlacesInVillageResponse.Content) -> Unit = {}
) :
    ListAdapter<PlacesInVillageResponse.Content, VenueListAdapter.ItemViewholder>(DiffCallback()) {

    private var currentSelection = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder {
        return ItemViewholder(
            ItemVenueSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VenueListAdapter.ItemViewholder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ItemViewholder(val view: ItemVenueSelectionBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(item: PlacesInVillageResponse.Content) = with(view) {
            if (item.selected == true) {
                currentSelection = adapterPosition
            }
            view.venue = item
            view.root.setOnClickListener {
                if (currentSelection != adapterPosition) {
                    if (currentSelection > -1) {
                        currentList[currentSelection].selected = false
                        notifyItemChanged(currentSelection)
                    }
                    currentSelection = adapterPosition
                    item.selected = true
                    notifyItemChanged(currentSelection)
                    selectionListener(item)
                }
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<PlacesInVillageResponse.Content>() {
    override fun areItemsTheSame(oldItem: PlacesInVillageResponse.Content, newItem: PlacesInVillageResponse.Content): Boolean {
        return oldItem.target.id == oldItem.target.id
    }

    override fun areContentsTheSame(oldItem: PlacesInVillageResponse.Content, newItem: PlacesInVillageResponse.Content): Boolean {
        return oldItem == newItem
    }
}