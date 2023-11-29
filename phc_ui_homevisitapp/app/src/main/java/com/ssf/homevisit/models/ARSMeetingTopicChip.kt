package com.ssf.homevisit.models

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.chip.Chip
import com.ssf.homevisit.R

class ARSMeetingTopicChip : LinkedHashMap<MeetingTopicChip, DropDownData?>() {
    private val _dropDowns = MutableLiveData<List<DropDownData>>()
    val dropDowns: LiveData<List<DropDownData>> = _dropDowns

    init {
        this[MeetingTopicChip(R.drawable.ic_employment, R.string.development)] = null
        this[MeetingTopicChip(R.drawable.ic_building_maintanance, R.string.structure_maintanance)] =
            null
        this[MeetingTopicChip(R.drawable.ic_facilities, R.string.infra_maintanance)] = null
        this[MeetingTopicChip(R.drawable.ic_garden_planting, R.string.garden_plantation)] = null
        this[MeetingTopicChip(R.drawable.ic_iec_program, R.string.human_resource)] = null
        this[MeetingTopicChip(R.drawable.ic_health_services, R.string.health_services)] = null
        this[MeetingTopicChip(R.drawable.ic_account_planning, R.string.annual_account_planning)] =
            null
        this[MeetingTopicChip(R.drawable.ic_mainantance_records, R.string.maintaining_records)] =
            null
        this[MeetingTopicChip(null, R.string.other_meeting_topics)] = null
    }

    private fun getTopicItems(chipData: MeetingTopicChip): DropDownData {
        val options = when (chipData.label) {
            R.string.development -> {
                listOf(
                    TopicItem("Others")
                )
            }
            R.string.structure_maintanance -> {
                listOf(
                    TopicItem("Others")
                )
            }
            R.string.infra_maintanance -> {
                listOf(
                    TopicItem("Construction"),
                    TopicItem("Others")
                )
            }
            R.string.garden_plantation -> {
                listOf(
                    TopicItem("Others")
                )
            }
            R.string.human_resource -> {
                listOf(TopicItem("Others"))
            }
            R.string.health_services -> {
                listOf(
                    TopicItem("Others")
                )
            }
            R.string.annual_account_planning -> {
                listOf(
                    TopicItem("Equipments (Repair/Purchase)"),
                    TopicItem("Financial management"),
                    TopicItem("Others"),
                )
            }
            R.string.maintaining_records -> {
                listOf(
                    TopicItem("Others")
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
                isSelected = this@ARSMeetingTopicChip[item] != null
                isChecked = this@ARSMeetingTopicChip[item] != null
            }
        }
    }

    fun attachDropDown(chipData: MeetingTopicChip): DropDownData {
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