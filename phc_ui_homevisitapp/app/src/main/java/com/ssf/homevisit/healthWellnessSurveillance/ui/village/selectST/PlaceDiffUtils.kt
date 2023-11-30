package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Place

class PlaceDiffUtils(
    private val oldList: List<Place>,
    private val newList: List<Place>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].name == newList[newItemPosition].name
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].latitude == newList[newItemPosition].latitude ||
                        oldList[oldItemPosition].longitude == newList[newItemPosition].longitude
                )
    }
}