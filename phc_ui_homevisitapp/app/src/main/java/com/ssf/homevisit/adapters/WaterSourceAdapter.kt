package com.ssf.homevisit.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.WaterSourcesSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.OptionsData

class WaterSourceAdapter(
    private val context: Context, private val listener: OnItemClick
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<OptionsData> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return WaterSourceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.water_sources_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as WaterSourceViewHolder).bind(data, position)
    }

    private inner class WaterSourceViewHolder(val binding: WaterSourcesSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(data: MutableList<OptionsData>, position: Int) {
            binding.menuName.text = data[position].question
            binding.menuIcon.visible()
            when (data[position].question) {
                "Piped Water" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_pipe
                        )
                    )
                }
                "Well" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.well
                        )
                    )
                }
                "Tube Well" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_tube_well
                        )
                    )
                }
                "Hand Pump" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_hand_pump
                        )
                    )
                }
                "Canal" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_canal
                        )
                    )
                }
                "River" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_river
                        )
                    )
                }
                "Lake" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_lake
                        )
                    )
                }
                "Watershed" -> {
                    binding.menuIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_canal
                        )
                    )
                }
                "Other" -> {
                    binding.menuName.text = "Others"
                    binding.menuIcon.gone()
                }
                else -> {

                }
            }
            if (data[position].isSelected) {
                binding.cvItem.setCardBackgroundColor(context.resources.getColor(R.color.lime_green))
            } else {
                binding.cvItem.setCardBackgroundColor(Color.parseColor("#E5E5E5"))
            }
            binding.cvItem.setOnClickListener {
                listener.onItemClick(data[position].question,position,data,data[position].propertyName)
            }
        }
    }

    interface OnItemClick {
        fun onItemClick(optionName: String?, position: Int, data: MutableList<OptionsData>,propertyName:String?)
    }


    override fun getItemCount(): Int {
        return data.size
    }
}
