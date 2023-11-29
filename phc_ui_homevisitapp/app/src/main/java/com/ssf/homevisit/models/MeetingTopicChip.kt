package com.ssf.homevisit.models

import android.content.Context
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.Chip
import com.ssf.homevisit.R

class ChipMap : LinkedHashMap<MeetingTopicChip, DropDownData?>() {
    private val _dropDowns = MutableLiveData<List<DropDownData>>()
    val dropDowns: LiveData<List<DropDownData>> = _dropDowns

    init {
        this[MeetingTopicChip(R.drawable.ic_education, R.string.education)] = null
        this[MeetingTopicChip(R.drawable.ic_employment, R.string.employment)] = null
        this[MeetingTopicChip(R.drawable.ic_facilities, R.string.facility)] = null
        this[MeetingTopicChip(R.drawable.ic_food_water, R.string.food_amp_drinking_water)] = null
        this[MeetingTopicChip(R.drawable.ic_health_vhnd, R.string.health_vhnd)] = null
        this[MeetingTopicChip(R.drawable.ic_iec_program, R.string.iec_program)] = null
        this[MeetingTopicChip(R.drawable.ic_shelter, R.string.shelter)] = null
        this[MeetingTopicChip(null, R.string.other_meeting_topics)] = null
    }

    private fun getTopicItems(chipData: MeetingTopicChip): DropDownData {
        val options = when (chipData.label) {
            R.string.employment -> {
                listOf(
                    TopicItem("Social security measures"),
                    TopicItem("Freedom from exploitation and discrimination"),
                    TopicItem("Safe drinking water & sanitation"),
                    TopicItem("Safe work condition"),
                    TopicItem("First Aid kits at work place"),
                    TopicItem("Hard Labour (Cement factory/Quarries)"),
                    TopicItem("Receiving wages right on time - MGNREGA"),
                    TopicItem("Others")
                )
            }
            R.string.health_vhnd -> {
                listOf(
                    TopicItem("ANC"),
                    TopicItem("PNC"),
                    TopicItem("Immunization"),
                    TopicItem("Malnourishment"),
                    TopicItem("CD"),
                    TopicItem("NCD - Screening"),
                    TopicItem("Anganawadi Services"),
                    TopicItem("Others")
                )
            }
            R.string.education -> {
                listOf(
                    TopicItem("Girls Health, Hygiene & Education"),
                    TopicItem("Boys Health, Hygiene & Education"),
                    TopicItem("Mental Health"),
                    TopicItem("Mid-day Meals"),
                    TopicItem("Toilets"),
                    TopicItem("Drinking Water"),
                    TopicItem("Regularity of Teaching Staff")
                )
            }
            R.string.facility -> {
                listOf(
                    TopicItem("Monitoring health care services in PHC/SC"),
                    TopicItem("Jan Samvads - Grievance Redressal"),
                    TopicItem("Monitor - Rashtriya Swasthya Bima Yojana & other Bima Yojana")
                )
            }
            R.string.food_amp_drinking_water -> {
                listOf(
                    TopicItem("PDS - Providing food Grains"),
                    TopicItem("Hand Pumps/Borewells/Mini Water Tank/Water Supply to HH")
                )
            }
            R.string.iec_program -> {
                listOf(
                    TopicItem("Diabetes Day"),
                    TopicItem("Eye Donation Fortnight"),
                    TopicItem("International Day of the Elderly"),
                    TopicItem("International Women's Day"),
                    TopicItem("Measles immunization day"),
                    TopicItem("New Born Care Week"),
                    TopicItem("National Anti-Drug Addiction Day"),
                    TopicItem("National Nutrition Week"),
                    TopicItem("No Smoking Day"),
                    TopicItem("ORS Day (Orel Rehydration Day)"),
                    TopicItem("World AIDS Day"),
                    TopicItem("World Asthma Day"),
                    TopicItem("World Breastfeeding Week"),
                    TopicItem("World Cancer Day"),
                    TopicItem("World Environment Day"),
                    TopicItem("World Epilepsy Day"),
                    TopicItem("World Food Day"),
                    TopicItem("World Health Day"),
                    TopicItem("World Heart Day"),
                    TopicItem("World Hepatitis Day"),
                    TopicItem("World Immunization Day"),
                    TopicItem("World Leprosy Eradication Day"),
                    TopicItem("World Malaria Day"),
                    TopicItem("World Mental Health Day"),
                    TopicItem("World Patient Safety Day"),
                    TopicItem("World Pneumonia Day"),
                    TopicItem("World Polio Day"),
                    TopicItem("World Population Day"),
                    TopicItem("World Rabies Day"),
                    TopicItem("World Sight Day"),
                    TopicItem("World TB Day")
                )
            }
            R.string.shelter -> {
                listOf(
                    TopicItem("Advantages of pucca housing"),
                    TopicItem("Identifying & recommending needy person for housing scheme")
                )
            }
            else -> listOf(TopicItem("Others"))
        }
        return DropDownData(chipData, options)
    }

    fun getChipViews(context: Context): List<Chip> {
        return keys.map { item ->
            Chip(context).apply {
                text = context.getString(item.label)
                tag = item
                id = View.generateViewId()
                isCheckable = true
                item.icon?.let {
                    setChipIconResource(it)
                    setCheckedIconResource(it)
                } ?: (setCheckedIconVisible(false))
                setChipBackgroundColorResource(R.color.chip_background_selector)
                setTextAppearanceResource(R.style.chip_text)
                isSelected = this@ChipMap[item] != null
                isChecked = this@ChipMap[item] != null
            }
        }
    }

    fun attachDropDown(chipData: MeetingTopicChip):DropDownData {
        val dropDown = getTopicItems(chipData)
        this[chipData] = dropDown
        return dropDown
    }

    override fun put(
        key: MeetingTopicChip,
        value: DropDownData?
    ): DropDownData? {
        val addedValue = super.put(key, value)
        _dropDowns.value = getDropdownViews()
        return addedValue
    }

    override fun remove(key: MeetingTopicChip): DropDownData? {
        this[key] = null
        val removedValue = super.remove(key)
        _dropDowns.value = getDropdownViews()
        return removedValue
    }

    override fun remove(key: MeetingTopicChip, value: DropDownData?): Boolean {
        this[key] = null
        val removedValue = super.remove(key, value)
        _dropDowns.value = getDropdownViews()
        return removedValue
    }

    fun getDropdownViews(): List<DropDownData> {
        return values.filterNotNull()
    }

    fun clearSelection() {
        keys.forEach { this[it] = null }
    }
}

data class MeetingTopicChip(
    @DrawableRes
    val icon: Int?,
    @StringRes
    val label: Int
)

data class TopicItem(
    val label: String,
    var isSelected: Boolean = false
)

data class DropDownData(
    val chipData: MeetingTopicChip,
    var dropDown: List<TopicItem>
)