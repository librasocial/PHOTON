package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem

class HouseHoldDiffUtils(
    private val oldList: List<HouseHoldDataItem>,
    private val newList: List<HouseHoldDataItem>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].houseHoldNumber == newList[newItemPosition].houseHoldNumber
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].houseHoldNumber == newList[newItemPosition].houseHoldNumber ||
                        oldList[oldItemPosition].nameHouseHoldHead == newList[newItemPosition].nameHouseHoldHead
                )
    }
}