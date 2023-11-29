package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST

import androidx.recyclerview.widget.DiffUtil
import com.ssf.homevisit.healthWellnessSurveillance.data.IndividualDetailProperty

class CitizenDiffUtils(
    private val oldList: List<IndividualDetailProperty>,
    private val newList: List<IndividualDetailProperty>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].healthID == newList[newItemPosition].healthID
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return (oldList[oldItemPosition].healthID == newList[newItemPosition].healthID || oldList[oldItemPosition].contact == newList[newItemPosition].contact)
    }
}