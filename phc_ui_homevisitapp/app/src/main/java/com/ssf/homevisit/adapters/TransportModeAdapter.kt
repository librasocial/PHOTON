package com.ssf.homevisit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.FragmentTransportModesSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.data.Options


class TransportModeAdapter(private val listener: TransportModeAdapterItemClick?) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: List<Options> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TransportModeViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.fragment_transport_modes_single_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as TransportModeViewHolder).bind(data[position], position)
    }
    private inner class TransportModeViewHolder(val binding: FragmentTransportModesSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Options, position: Int) {
            binding.checkbox.isChecked = data.checked
            binding.checkbox.text = data.question
            binding.checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) listener?.onCheckItem(position, data.question,data.propertyName)
                else listener?.onUnCheckItem(position, data.question,data.propertyName)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface TransportModeAdapterItemClick {
        fun onCheckItem(position: Int, transport: String, propertyName: String)
        fun onUnCheckItem(position: Int, transport: String, propertyName: String)
    }
}