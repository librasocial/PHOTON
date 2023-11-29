package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.MappingRawBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.inVisible
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.setDrawable
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.MappingRawModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.MappingDiffUtils


class MappingDynamicAdapter(
        private val oldList: MutableList<MappingRawModel>,
        private val listener: OnItemClick,
        private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(newList: MutableList<MappingRawModel>) {
        val diffCallback = MappingDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MappingDynamicViewHolder(
                DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context), R.layout.mapping_raw, parent, false
                )
        )
    }
    inner class MappingDynamicViewHolder(val binding: MappingRawBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MappingRawModel) {
            if (data.disabled==true && data.surveillanceMappingModel.menuIconId==-1) {
                binding.deleteRow.inVisible()
                binding.attachImageView.isEnabled = false
                binding.anyLarvaSpinner.isEnabled = false
                binding.breedingSiteSpinner.isEnabled = false
                binding.solutionProvidedSpinner.isEnabled = false
            }
            else{
                binding.deleteRow.visible()
                binding.attachImageView.isEnabled = true
                binding.anyLarvaSpinner.isEnabled = true
                binding.breedingSiteSpinner.isEnabled = true
                binding.solutionProvidedSpinner.isEnabled = true
            }
            if (data.isLarvaFound== "Yes") {
                binding.solutionProvidedSpinner.isClickable=true
                binding.solutionProvidedSpinner.background =
                    context.resources.getDrawable(R.drawable.spinner_bg)
            } else {
                binding.solutionProvidedSpinner.isEnabled = false
                binding.solutionProvidedSpinner.background =
                    context.resources.getDrawable(R.drawable.ic_disabled_spinner)
            }
            binding.solutionProvidedSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                ) {
                    if(data.disabled == false){
                    listener.onItemClick(
                            data,
                            adapterPosition,
                            "solutionSpinner",
                            binding.solutionProvidedSpinner.selectedItem.toString(),
                            position - 1
                    )
                }}

                override fun onNothingSelected(parentView: AdapterView<*>?) {}
            }
            binding.anyLarvaSpinner.onItemSelectedListener = object :
                    AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                        parentView: AdapterView<*>?,
                        selectedItemView: View?,
                        position: Int,
                        id: Long
                ) {
                    if(data.disabled==false){
                    listener.onItemClick(
                            data,
                            adapterPosition,
                            "anyLarvaSpinner",
                            binding.anyLarvaSpinner.selectedItem.toString(),
                            position - 1
                    )
                }}

                override fun onNothingSelected(parentView: AdapterView<*>?) {}
            }
            binding.breedingSiteSpinner.onItemSelectedListener =
                    object :
                            AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                                parentView: AdapterView<*>?,
                                selectedItemView: View?,
                                position: Int,
                                id: Long,
                        ) {
                            if(data.disabled==false){
                            listener.onItemClick(
                                    data,
                                    adapterPosition,
                                    "breedingSiteSpinner",
                                    binding.breedingSiteSpinner.selectedItem.toString(),
                                    position - 1
                            )
                        }}

                        override fun onNothingSelected(parentView: AdapterView<*>?) {}
                    }
            if (data.surveillanceMappingModel.menuIconId == -1) {
                binding.rowIcon.inVisible()
            } else if (data.surveillanceMappingModel.menuIconId != 0) {
                binding.rowIcon.setDrawable(data.surveillanceMappingModel.menuIconId)
            } else {}
            if (data.breedingSpot?.isNotEmpty() == true) {
                binding.rowTitle.text = data.breedingSpot
            }
            if (data.nextInspection?.isNotEmpty() == true)
                binding.dateTitleValue.text = data.nextInspection

            if (data.image?.isNotEmpty() == true)
                binding.attachImageView.setDrawable(R.drawable.image_selected_place_holde)
            else
                binding.attachImageView.setDrawable(R.drawable.ic_image_place_holder)

            binding.deleteRow.setOnClickListener {
                listener.onItemClick(data, adapterPosition, "deleteRow")
            }
            binding.attachImageView.setOnClickListener {
                listener.onItemClick(data, adapterPosition, "attachImage")
            }
            if (data.breedingSite?.isNotEmpty() == true && data.breedingSite != "") {
                val index = context.resources.getStringArray(R.array.breedingSite)
                        .indexOf(data.breedingSite)
                binding.breedingSiteSpinner.setSelection(index)
            } else {
                binding.breedingSiteSpinner.setSelection(0)
            }
            if (data.isLarvaFound?.isNotEmpty() == true && data.isLarvaFound != "") {
                val index = context.resources.getStringArray(R.array.anyLarvaFound)
                        .indexOf(data.isLarvaFound)
                binding.anyLarvaSpinner.setSelection(index)
            } else {
                binding.anyLarvaSpinner.setSelection(0)
            }
            if (data.solutionProvided?.isNotEmpty() == true && data.solutionProvided != "") {
                val index = context.resources.getStringArray(R.array.solutionProvide)
                        .indexOf(data.solutionProvided)
                binding.solutionProvidedSpinner.setSelection(index)
            } else {
                binding.solutionProvidedSpinner.setSelection(0)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MappingDynamicViewHolder).bind(oldList[position])
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    interface OnItemClick {
        fun onItemClick(
                data: MappingRawModel,
                position: Int,
                action: String,
                item: String? = null,
                spinnerPosition: Int = 0
        )

    }

}