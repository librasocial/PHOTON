package com.ssf.homevisit.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentPublicFacilitySingleItemBinding
import com.ssf.homevisit.databinding.OthersLayoutBinding
import com.ssf.homevisit.healthWellnessSurveillance.data.OptionsData


class PublicFacilityAdapter(
    private val listener: PublicFacilityAdapterItemClick?,
    private val otherListener: OnOtherClick
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val VIEW_TYPE_ONE = 1
        const val VIEW_TYPE_TWO = 2
    }

    var data: MutableList<OptionsData> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == VIEW_TYPE_ONE) {
            return PublicFacilityViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.fragment_public_facility_single_item,
                    parent,
                    false
                )
            )
        }
        return PublicFacilityViewHolder2(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.others_layout, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (data[position].question == "Others") {
            (holder as PublicFacilityViewHolder2).bind(data, data[position])
        } else {
            (holder as PublicFacilityViewHolder).bind(data, data[position])
        }

    }

    private inner class PublicFacilityViewHolder(val binding: FragmentPublicFacilitySingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: MutableList<OptionsData>, item: OptionsData) {
            binding.tvFacilityName.text = item.question
            binding.radioYes.setOnClickListener {
                listener?.onSelect(
                    position = adapterPosition,
                    item = item.question,
                    isYesSelected = true,
                    data = data,
                    propertyName = item.propertyName
                )
            }
            binding.radioNo.setOnClickListener {
                listener?.onSelect(
                    position = adapterPosition, item = item.question, isYesSelected = false, data,item.propertyName
                )
            }
            if (!item.isSelected) {
                binding.radioGroup.clearCheck()
            } else {
                if (item.value) {
                    binding.radioYes.isChecked = true
                } else {
                    binding.radioNo.isChecked = true
                }

            }
        }
    }

    private inner class PublicFacilityViewHolder2(val binding: OthersLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: MutableList<OptionsData>, item: OptionsData) {
            binding.tvOthers.text = item.question
            binding.etOthers.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    otherListener.onOtherClick(s.toString(), item.question, adapterPosition,item.propertyName)
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }
            })
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface PublicFacilityAdapterItemClick {
        fun onSelect(
            position: Int,
            item: String?,
            isYesSelected: Boolean = false,
            data: MutableList<OptionsData>,
            propertyName: String?
        )
    }

    interface OnOtherClick {
        fun onOtherClick(
            data: String,
            question: String?,
            position: Int,
            propertyName: String?,
        )
    }

    override fun getItemViewType(position: Int): Int {
        if (data[position].question == "Others") {
            return VIEW_TYPE_TWO
        }
        return VIEW_TYPE_ONE
    }
}