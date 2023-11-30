package com.ssf.homevisit.rmncha.childCare.immunization

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.ImmunizationHistorySingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.gone
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.visible


class HistoryAdapter(
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var data: LinkedHashMap<String, List<Immunization>> = linkedMapOf()

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): HistoryViewHolder {
        return HistoryViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.immunization_history_single_item,
                parent,
                false
            )
        )
    }

    inner class HistoryViewHolder(val binding: ImmunizationHistorySingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("NotifyDataSetChanged")
        fun bind(data: HashMap<String, List<Immunization>>, position: Int) {
            val dose = data.keys.toList()[adapterPosition]
            val vaccineList=data[dose]
            val vaccinationAdapter=VaccinationAdapter(vaccineList ?:listOf())
            binding.rvVaccineName.adapter=vaccinationAdapter
            binding.tvDoseName.text = dose
            var count=0
            vaccineList?.forEach {
                if(it.immunizationSubGroup.vaccinatedDate==null){
                    count+=1
                }
            }
            if(dose=="Birth Dose"){
                if(count==0){
                    binding.tvDoseName.setTextColor(ResourcesCompat.getColor(context.resources,R.color.green,null))
                }
            }
            if(dose=="Week 6"){
                if(count>1){
                    binding.tvDoseName.setTextColor(ResourcesCompat.getColor(context.resources,R.color.voilet,null))
                }
            }
            binding.root.setOnClickListener{
                if(binding.rvVaccineName.isVisible){
                    binding.rvVaccineName.gone()
                    binding.ivRightArrow.background=ResourcesCompat.getDrawable(context.resources,R.drawable.ic_right_arrow,null)
                }
                else{
                    binding.ivRightArrow.background=ResourcesCompat.getDrawable(context.resources,R.drawable.ic_down_arrow,null)
                    binding.rvVaccineName.visible()
                }

            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as HistoryViewHolder).bind(data, position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}