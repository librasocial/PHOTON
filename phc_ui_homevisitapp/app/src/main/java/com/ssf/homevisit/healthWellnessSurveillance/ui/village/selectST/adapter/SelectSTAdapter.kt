package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.SelectStSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.SelectST
import com.ssf.homevisit.requestmanager.AppDefines


class SelectSTAdapter(
    private val oldList: MutableList<SelectST>,
    private val listener: OnItemClick,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var lastSelectedIndex = -1
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectSTViewHolder {
        return SelectSTViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.select_st_single_item, parent, false
            )
        )
    }

    inner class SelectSTViewHolder(val binding: SelectStSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bind(data: MutableList<SelectST>, position: Int) {
            when (data[position].type) {
                AppDefines.LARVA_TEST -> {
                    binding.tvSt.text = "Larva Surveillance"
                    binding.ivSt.background =
                        context.resources.getDrawable(R.drawable.ic_larva_surveillance)
                }
                AppDefines.WATER_SAMPLE_TEST -> {
                    binding.tvSt.text = "Water Sample Collection"
                    binding.ivSt.background =
                        context.resources.getDrawable(R.drawable.ic_tap)

                }
                AppDefines.IODINE_TEST -> {
                    binding.tvSt.text = "Iodine Test"
                    binding.ivSt.background =
                        context.resources.getDrawable(R.drawable.ic_iodine_test)
                }
            }

            binding.clSt.setOnClickListener {
                if (lastSelectedIndex != -1) {
                    listener.onItemClick(
                        data = data,
                        previousPosition = lastSelectedIndex,
                        currentPosition = position
                    )
                } else {
                    listener.onItemClick(
                        data = data,
                        previousPosition = -1,
                        currentPosition = position
                    )
                }
                lastSelectedIndex = position
            }
            if (data[position].isSelected) {
                binding.clSt.background = context.resources.getDrawable(R.drawable.select_st_green)
            }
            else{
                binding.clSt.background = context.resources.getDrawable(R.drawable.select_st)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectSTViewHolder).bind(oldList, position)
    }

    interface OnItemClick {
        fun onItemClick(
            data: MutableList<SelectST>,
            currentPosition: Int,
            previousPosition: Int
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }
}
