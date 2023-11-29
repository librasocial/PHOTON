package com.ssf.homevisit.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentNaturalResourceSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.setDrawable
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.NaturalResourceDataItem


class NaturalResourceVSEAdapter(
    private val listener: OnItemClick, private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: MutableList<NaturalResourceDataItem> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): NaturalResourceViewHolder {
        return NaturalResourceViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fragment_natural_resource_single_item,
                parent,
                false
            )
        )
    }

    inner class NaturalResourceViewHolder(val binding: FragmentNaturalResourceSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: NaturalResourceDataItem, position: Int) {
            binding.tvTitle.text = data.imageDescription
            when (position) {
                0 -> {
                    if (data.completed) {
                        binding.cvNaturalResource.setCardBackgroundColor(
                            context.resources.getColor(
                                R.color.lime_green
                            )
                        )
                        binding.ivYes.visibility = View.VISIBLE
                    }
                    binding.ivIcon.setDrawable(R.drawable.quality)
                }
                1 -> {
                    if (data.completed) {
                        binding.cvNaturalResource.setCardBackgroundColor(
                            context.resources.getColor(
                                R.color.lime_green
                            )
                        )
                        binding.ivYes.visibility = View.VISIBLE
                    }
                    binding.ivIcon.setDrawable(R.drawable.water_sources)
                }
                2 -> {
                    if (data.completed) {
                        binding.cvNaturalResource.setCardBackgroundColor(
                            context.resources.getColor(
                                R.color.lime_green
                            )
                        )
                        binding.ivYes.visibility = View.VISIBLE
                    }
                    binding.ivIcon.setDrawable(R.drawable.village_infrastructure)
                }
                3 -> {
                    if (data.completed) {
                        binding.cvNaturalResource.setCardBackgroundColor(
                            context.resources.getColor(
                                R.color.lime_green
                            )
                        )
                        binding.ivYes.visibility = View.VISIBLE
                    }
                    binding.ivIcon.setDrawable(R.drawable.transport)
                }

                4 -> {
                    if (data.completed) {
                        binding.cvNaturalResource.setCardBackgroundColor(
                            context.resources.getColor(
                                R.color.lime_green
                            )
                        )
                        binding.ivYes.visibility = View.VISIBLE
                    }
                    binding.ivIcon.setDrawable(R.drawable.public_facility)

                }
            }

            binding.clNaturalResource.setOnClickListener {
                listener.onItemClick(position, data)
            }
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as NaturalResourceViewHolder).bind(data[position], position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OnItemClick {
        fun onItemClick(position: Int, naturalResourceDataItem: NaturalResourceDataItem)
    }
}