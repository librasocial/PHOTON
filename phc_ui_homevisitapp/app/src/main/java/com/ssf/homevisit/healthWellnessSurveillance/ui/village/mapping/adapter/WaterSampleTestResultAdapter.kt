package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.TestResultSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.TestResultDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.TestResultDiffUtils

class WaterSampleTestResultAdapter(
    private val oldList: MutableList<TestResultDataItem>,
    private val listener: OnItemSelected
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(newList: MutableList<TestResultDataItem>) {
        val diffCallback = TestResultDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TestResultViewHolder {
        return TestResultViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.test_result_single_item, parent, false
            )
        )
    }

    inner class TestResultViewHolder(val binding: TestResultSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TestResultDataItem, position: Int) {
            if (data.labResults != "Select to update lab results") {
                binding.item4.isClickable = false
                binding.item4.isEnabled = false
            }
            binding.item2.text = data.sampleCount
            binding.item1.text = data.dateOfSampleCollected
            binding.item3.text = data.dateOfSampleSubmitted
            binding.item4.text = data.labResults
            binding.item4.setOnClickListener {
                listener.onItemSelected(data)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TestResultViewHolder).bind(oldList[position], position)
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    interface OnItemSelected{
        fun onItemSelected(data: TestResultDataItem)
    }
}