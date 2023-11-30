package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.SurveillanceRvItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.inVisible
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.setDrawable
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.SurveillanceMappingModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.SurveillanceMappingDiffUtils


class SurveillanceMappingAdapter(
    val oldList: MutableList<SurveillanceMappingModel>,
    val listener: OnSurveillanceItemClick,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setData(newList: MutableList<SurveillanceMappingModel>) {
        val diffCallback = SurveillanceMappingDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SurveillanceMappingViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.surveillance_rv_item, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SurveillanceMappingViewHolder).bind(oldList[position])
    }

    inner class SurveillanceMappingViewHolder(val binding: SurveillanceRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: SurveillanceMappingModel) {
            if (data.menuIconId != 0) {
                binding.menuIcon.setDrawable(data.menuIconId)
                binding.cvItem.visible()
            } else
                binding.cvItem.inVisible()
            binding.menuName.text = data.menuItem.menuName
            binding.cvItem.setOnClickListener {
                listener.onSurveillanceItemClick(data,adapterPosition)
            }
            if(data.isSelected==true){
                binding.cvItem.setCardBackgroundColor(ContextCompat.getColor(context, com.ssf.homevisit.R.color.green))
            }
            else{
                binding.cvItem.setCardBackgroundColor(ContextCompat.getColor(context, com.ssf.homevisit.R.color.light_grey_card))

            }
        }
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    interface OnSurveillanceItemClick {
        fun onSurveillanceItemClick(data: SurveillanceMappingModel,position: Int)
    }

}
