package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.MappingRawModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.TestResultDataItem


class MappingDiffUtils(
        private val oldList: List<MappingRawModel>,
        private val newList: List<MappingRawModel>
) :
        DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].breedingSpot == newList[newItemPosition].breedingSpot &&
                oldList[oldItemPosition].breedingSite == newList[newItemPosition].breedingSite
                && oldList[oldItemPosition].surveillanceMappingModel.menuIconId == oldList[oldItemPosition].surveillanceMappingModel.menuIconId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].breedingSpot == newList[newItemPosition].breedingSpot &&
                        oldList[oldItemPosition].breedingSite == newList[newItemPosition].breedingSite
                        && oldList[oldItemPosition].surveillanceMappingModel.menuIconId == oldList[oldItemPosition].surveillanceMappingModel.menuIconId
                )
    }
}