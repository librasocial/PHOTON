package com.ssf.homevisit.healthWellnessSurveillance.individual.adapter


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssf.homevisit.R
import com.ssf.homevisit.databinding.IndividualSurveillanceSingleItemBinding
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.AssetDiffUtils
import com.ssf.homevisit.requestmanager.AppDefines


class IndividualAssetAdapter(
    private val oldList: MutableList<Asset>,
    private val listener: OnItemClick,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


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
    ): SelectSTViewHolder {
        return SelectSTViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.individual_surveillance_single_item, parent, false
            )
        )
    }

    inner class SelectSTViewHolder(val binding: IndividualSurveillanceSingleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n", "UseCompatLoadingForDrawables")
        fun bind(data: MutableList<Asset>, position: Int) {
            binding.tvTitle.text = data[position].title
            binding.clIndividualType.setOnClickListener {
                listener.onItemClick(data[position])
            }
            when (data[position].title) {
                AppDefines.ACTIVE_CASE_FINDING -> {
                    binding.ivIcon.background =
                        context.resources.getDrawable(R.drawable.ic_tuber)
                }
                AppDefines.BLOOD_SMEAR -> {
                    binding.ivIcon.background =
                        context.resources.getDrawable(R.drawable.ic_blood_smear)
                }
                AppDefines.IDSP_S_Form -> {
                    binding.ivIcon.background =
                        context.resources.getDrawable(R.drawable.ic_idsp)
                }
                AppDefines.COVID -> {
                    binding.ivIcon.background =
                        context.resources.getDrawable(R.drawable.ic_coronavirus_second_wave_bro_1)
                }
                AppDefines.URINE_SAMPLE -> {
                    binding.ivIcon.background =
                        context.resources.getDrawable(R.drawable.ic_urine)
                }
                AppDefines.LEPROSY -> {
                    binding.ivIcon.background =
                        context.resources.getDrawable(R.drawable.ic_leprosy)
                }
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SelectSTViewHolder).bind(oldList, position)
    }

    interface OnItemClick {
        fun onItemClick(
            data: Asset
        )
    }

    override fun getItemCount(): Int {
        return oldList.size
    }
}
