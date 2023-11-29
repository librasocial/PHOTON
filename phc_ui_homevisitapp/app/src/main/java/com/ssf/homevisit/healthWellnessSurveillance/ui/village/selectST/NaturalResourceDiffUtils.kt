package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.NaturalResourceDataItem


class NaturalResourceDiffUtils(
    private val oldList: List<NaturalResourceDataItem>,
    private val newList: List<NaturalResourceDataItem>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].imageDescription == newList[newItemPosition].imageDescription
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].imageDescription == newList[newItemPosition].imageDescription
                )
    }
}