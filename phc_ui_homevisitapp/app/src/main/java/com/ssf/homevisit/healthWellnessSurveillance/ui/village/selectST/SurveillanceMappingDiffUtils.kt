package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.SurveillanceMappingModel


class SurveillanceMappingDiffUtils(
    private val oldList: MutableList<SurveillanceMappingModel>,
    private val newList: MutableList<SurveillanceMappingModel>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].menuIconId == newList[newItemPosition].menuIconId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].menuIconId == newList[newItemPosition].menuIconId ||
                        oldList[oldItemPosition].menuItem == newList[newItemPosition].menuItem
                )
    }
}