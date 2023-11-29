package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.SelectPlaceSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Place
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.PlaceItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.PlaceDiffUtils


class SelectPlaceAdapter(
    private val oldList: MutableList<Place>,
    private val listener: OnPlaceItemClick,
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun setData(newList: MutableList<Place>) {
        val diffCallback = PlaceDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): SelectPlaceViewHolder {
        return SelectPlaceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.select_place_single_item,
                parent,
                false
            )
        )
    }

    inner class SelectPlaceViewHolder(val binding: SelectPlaceSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Place, position: Int) {
            binding.tvImageTitle.text = data.name
            val view = binding.ivPlace
            if (data.imageUrl.isEmpty()) {
                binding.ivError.visible()
                binding.tvError.visible()
            } else {
                binding.ivPlace.visible()
                Picasso.get().load(data.imageUrl[0]).resize(100, 100)
                    .into(binding.ivPlace)
            }
            binding.cvImage.setOnClickListener {
                var imageUrl=""
                if(data.imageUrl.isNotEmpty()){
                    imageUrl=data.imageUrl[0]
                }
                listener.onPlaceClick(
                    PlaceItem(
                        name = data.name,
                        imageUrl = imageUrl,
                        latitude = data.latitude,
                        longitude = data.longitude,
                        assetType = data.assetType,
                        requiredSurveys = data.requiredSurveys,
                        id = data.id
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectPlaceViewHolder).bind(oldList[position], position)
    }

    interface OnPlaceItemClick {
        fun onPlaceClick(
            data: PlaceItem
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }
}
