package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.SelectAssetSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.AssetDiffUtils

class AssetAdapter(
    private val oldList: MutableList<Asset>,
    private val listener: OnAssetClick,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var lastSelectedIndex = -1

    fun setData(newList: MutableList<Asset>) {
        val diffCallback = AssetDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelectAssetViewHolder {
        return SelectAssetViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.select_asset_single_item, parent, false
            )
        )
    }

    inner class SelectAssetViewHolder(val binding: SelectAssetSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MutableList<Asset>) {
            val title = data[adapterPosition].title
            binding.tvAsset.text = title
            when (title) {
                "Shop" -> {
                    setImageDrawable(binding, R.drawable.ic_shop)
                }
                "Home" -> {
                    setImageDrawable(binding, R.drawable.ic_home)
                }
                "Hotel" -> {
                    setImageDrawable(binding, R.drawable.ic_hotel)
                }
                "Others" -> {
                    setImageDrawable(binding, R.drawable.ic_others)
                }
                "WaterBody" -> {
                    binding.tvAsset.text = "Water Body"
                    setImageDrawable(binding, R.drawable.waterbodyicon)
                }
                "School/collage" -> {
                    setImageDrawable(binding, R.drawable.ic_school_college)
                }
                "Temple" -> {
                    setImageDrawable(binding, R.drawable.ic_temple)
                }
                "Mosque" -> {
                    setImageDrawable(binding, R.drawable.ic_mosque)
                }
                "Church" -> {
                    setImageDrawable(binding, R.drawable.ic_church)
                }
                "Office" -> {
                    setImageDrawable(binding, R.drawable.ic_office)
                }
                "Hospital" -> {
                    setImageDrawable(binding, R.drawable.ic_hospital)
                }
                "Construction" -> {
                    setImageDrawable(binding, R.drawable.ic_construction)
                }
                "BusStop" -> {
                    binding.tvAsset.text = "Bus Stop"
                    setImageDrawable(binding, R.drawable.ic_bus_stop)
                }
                "Toilet" -> {
                    setImageDrawable(binding, R.drawable.ic_toilet)
                }
                "Park" -> {
                    setImageDrawable(binding, R.drawable.ic_park)
                }
            }
            binding.clAsset.setOnClickListener {
                binding.ivYes.visibility = View.VISIBLE
                binding.clAsset.setBackgroundColor(context.getColor(R.color.lime_green))
                if (lastSelectedIndex != -1) {
                    listener.onAssetClick(
                        data = data,
                        previousPosition = lastSelectedIndex,
                        currentPosition = adapterPosition
                    )
                } else {
                    listener.onAssetClick(
                        data = data,
                        previousPosition = -1,
                        currentPosition = adapterPosition
                    )
                }
                lastSelectedIndex = adapterPosition
            }
            if (data[position].selected) {
                binding.clAsset.background=context.resources.getDrawable(R.drawable.ic_green_rectangle)
                binding.ivYes.visibility=View.VISIBLE
            }
            else{
                binding.ivYes.visibility=View.GONE
                binding.clAsset.background=context.resources.getDrawable(R.drawable.ic_rectangle)
            }
        }
    }

    fun setImageDrawable(binding: SelectAssetSingleItemBinding, imageId: Int) {
        binding.ivAsset.background =
            context.resources.getDrawable(imageId)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectAssetViewHolder).bind(oldList)
    }

    interface OnAssetClick {
        fun onAssetClick(
            data: MutableList<Asset>,
            previousPosition: Int,
            currentPosition: Int
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }
}
