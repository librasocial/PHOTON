package com.ssf.homevisit.rmncha.childCare.immunization

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.VaccineStatusSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone


class VaccinationStatusAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var vaccinationStatusList: MutableList<VaccinationStatus> = mutableListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VaccineViewHolder {
        return VaccineViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.vaccine_status_single_item, parent, false
            )
        )
    }

    inner class VaccineViewHolder(val binding: VaccineStatusSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: VaccinationStatus) {
            binding.tvYear.text = data.year.toString()
            if (data.status) {
                binding.tvStatus.text = "Done"
                binding.ivStatus.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_yes, null)
            } else {
                binding.ivStatus.background =
                    ResourcesCompat.getDrawable(context.resources, R.drawable.ic_pending, null)
                binding.tvStatus.text = "Pending"
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VaccineViewHolder).bind(vaccinationStatusList[position])
    }


    override fun getItemCount(): Int {
        return vaccinationStatusList.size
    }
}