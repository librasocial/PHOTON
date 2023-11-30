package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset


class AssetDiffUtils(
        private val oldList: List<Asset>,
        private val newList: List<Asset>
) :
        DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].title == newList[newItemPosition].title
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (
                oldList[oldItemPosition].title == newList[newItemPosition].title
                        && oldList[oldItemPosition].selected == newList[newItemPosition].selected
                )
    }
}
