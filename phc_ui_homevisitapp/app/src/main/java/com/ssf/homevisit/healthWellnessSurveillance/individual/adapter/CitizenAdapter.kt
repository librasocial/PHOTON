package com.ssf.homevisit.healthWellnessSurveillance.individual.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.IndividualSelectionSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.inVisible
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible
import com.ssf.homevisit.healthWellnessSurveillance.data.IndividualDetailProperty
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.CitizenDiffUtils
import com.ssf.homevisit.requestmanager.AppDefines


class CitizenAdapter(
    private val oldList: MutableList<IndividualDetailProperty>,
    private val listener: OnCitizenClick, private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    fun setData(newList: MutableList<IndividualDetailProperty>) {
        val diffCallback = CitizenDiffUtils(oldList, newList)
        val diffCourses = DiffUtil.calculateDiff(diffCallback)
        oldList.clear()
        oldList.addAll(newList)
        diffCourses.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CitizenViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.individual_selection_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as CitizenViewHolder).bind(oldList[position])
    }

    override fun getItemCount(): Int {
        return oldList.size
    }

    private inner class CitizenViewHolder(val binding: IndividualSelectionSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(data: IndividualDetailProperty) {
            data.imageUrls?.let {
                if (it.isNotEmpty()) {
                    if (it[0].isEmpty()) {
                        binding.ecPhoto.background =
                            context.resources.getDrawable(R.drawable.ic_image_place_holder)
                    } else {
                        Picasso.get().load(it[0]).resize(100, 100).into(binding.ecPhoto)
                    }
                } else {
                    binding.ecPhoto.background =
                        context.resources.getDrawable(R.drawable.ic_image_place_holder)
                }
            }
            if (data.healthID?.isNotEmpty() == true) {
                binding.ecHealthId.text = "Health ID number : " + data.healthID
            }
            binding.ecName.text = data.firstName
            if (data.contact?.isNotEmpty() == true) {
                binding.ecPhoneNo.text = "Ph :" + data.contact
            }
            if (data.dateOfBirth?.isNotEmpty() == true) {
                binding.ecDOB.text = "DOB :" + data.dateOfBirth
            }
            if (data.age == null) {
                if (data.gender?.isNotEmpty() == true) {
                    binding.ecAge.text = data.gender
                } else {
                    binding.ecAge.gone()
                }
            } else {
                if (data.gender?.isNotEmpty() == true) {
                    binding.ecAge.text = data.age.toInt().toString() + "years" + " - " + data.gender
                } else {
                    binding.ecAge.text = data.age.toInt().toString()
                }
            }
            binding.llParent.setOnClickListener {
                listener.onCitizenClick(data)
            }
            if (data.labStatus != null) {
                binding.cvStatus.visible()
                when (data.labStatus) {
                    "Positive" -> {
                        binding.ivIcon.background =
                            context.resources.getDrawable(R.drawable.ic_result)
                        binding.tvStatus.text = "Lab Results : Positive"
                        binding.cvStatus.backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.limeRed)

                    }
                    "Negative" -> {
                        binding.ivIcon.background =
                            context.resources.getDrawable(R.drawable.ic_result)
                        if (data.labStatusDate != null) {
                            val labStatusDate = data.labStatusDate!!.split("T").first()
                            binding.cvStatus.backgroundTintList =
                                ContextCompat.getColorStateList(context, R.color.lime_light_green)
                            binding.tvStatus.text = "Negative Result : ${labStatusDate}}"
                        } else {
                            binding.tvStatus.text = "Negative Result"
                        }
                    }
                    AppDefines.PENDING_STATUS -> {
                        binding.cvStatus.backgroundTintList =
                            ContextCompat.getColorStateList(context, R.color.yellow)
                        binding.ivIcon.background =
                            context.resources.getDrawable(R.drawable.ic_result_pending)
                        binding.tvStatus.text = "Lab Results : Pending"
                    }
                    else -> {
                        binding.cvStatus.inVisible()
                    }
                }

            }
        }
    }

    interface OnCitizenClick {
        fun onCitizenClick(data: IndividualDetailProperty)
    }

}