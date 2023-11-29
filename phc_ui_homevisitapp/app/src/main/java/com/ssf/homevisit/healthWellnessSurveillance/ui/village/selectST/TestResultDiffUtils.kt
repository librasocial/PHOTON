package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.TestResultDataItem


class TestResultDiffUtils(
    private val oldList: List<TestResultDataItem>,
    private val newList: List<TestResultDataItem>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].dateOfSampleCollected == newList[newItemPosition].dateOfSampleCollected
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].dateOfSampleSubmitted == newList[newItemPosition].dateOfSampleSubmitted &&
                        oldList[oldItemPosition].sampleCount == newList[newItemPosition].sampleCount &&
                        oldList[oldItemPosition].dateOfSampleCollected == newList[newItemPosition].dateOfSampleCollected
                )
    }
}