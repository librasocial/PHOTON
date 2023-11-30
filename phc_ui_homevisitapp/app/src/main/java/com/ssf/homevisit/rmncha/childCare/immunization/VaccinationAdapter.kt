package com.ssf.homevisit.rmncha.childCare.immunization


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.VaccineNameSingleItemBinding


class VaccinationAdapter(
    val vaccineList: List<Immunization>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VaccineViewHolder {
        return VaccineViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.vaccine_name_single_item, parent, false
            )
        )
    }

    inner class VaccineViewHolder(val binding: VaccineNameSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Immunization) {
            binding.item1.text = data.immunizationSubGroup.name
            if (data.immunizationSubGroup.vaccinatedDate == null) {
                binding.item2.text = data.immunizationSubGroup.earliestDueDate
            } else {
                binding.item2.text = data.immunizationSubGroup.vaccinatedDate
            }
            if (data.immunizationSubGroup.aefiStatus == null) {
                binding.item3.text = "----"
            } else {
                binding.item3.text = data.immunizationSubGroup.aefiStatus
            }

            if (data.immunizationSubGroup.aefiReason == null) {
                binding.item4.text = "----"
            } else {
                binding.item4.text = data.immunizationSubGroup.aefiReason
            }

            if (data.immunizationSubGroup.manufacturer == null) {
                binding.item5.text = "----"
            } else {
                binding.item5.text = data.immunizationSubGroup.manufacturer
            }
            if (data.immunizationSubGroup.batchNumber == null) {
                binding.item6.text = "----"
            } else {
                binding.item6.text = data.immunizationSubGroup.batchNumber
            }
            if (data.immunizationSubGroup.expirationDate == null) {
                binding.item7.text = "----"
            } else {
                binding.item7.text = data.immunizationSubGroup.expirationDate
            }
            if (data.immunizationSubGroup.ashaWorker == null) {
                binding.item8.text = "----"
            } else {
                binding.item8.text = data.immunizationSubGroup.ashaWorker
            }
        }}

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as VaccineViewHolder).bind(vaccineList[position])
    }


    override fun getItemCount(): Int {
        return vaccineList.size
    }
}