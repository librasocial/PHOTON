package com.ssf.homevisit.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.google.gson.Gson
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.MeetingRepository
import com.ssf.homevisit.utils.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ARSViewModel(handle: SavedStateHandle) : ViewModel() {
    val toolbarPath = MutableLiveData("")
    val toolbarDestination = MutableLiveData("")
    val meetingDate = MutableLiveData("")
    val meetingTime = MutableLiveData("")
    val isValidData = MutableLiveData(false)
    val meetingId = MutableLiveData("")

    val isSpecialInviteeMode = MutableLiveData(false)

    val meetingTopicOptions = ARSMeetingTopicChip()

    private val meetingRepository = MeetingRepository()


    fun setToolbarTitle(path: String, destination: String) {
        toolbarPath.value = path
        toolbarDestination.value = destination
    }

    fun setMeetingDate(yr: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, yr)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        meetingDate.value = sdf.format(cal.time)
        checkValidation()
    }

    fun setMeetingTime(hour: Int, min: Int) {
        try {
            val fmt = SimpleDateFormat("HH:mm")
            val date = fmt.parse("$hour:$min")
            val dateFormat = SimpleDateFormat("hh:mm aa")
            meetingTime.value = dateFormat.format(date)
            checkValidation()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        checkValidation()
    }

    fun setLoading(show: Boolean) {
        isLoading.value = show
    }

    val isLoading = MutableLiveData(false)

    fun checkValidation() {
        val isValid =
            !meetingDate.value.isNullOrEmpty() && !meetingTime.value.isNullOrEmpty() && getSelectedTopics().isNotEmpty()
        isValidData.value = isValid
    }

    private fun getSelectedTopics(): List<String> {
        val list = arrayListOf<String>()
        meetingTopicOptions.getDropdownViews().forEach {
            list.addAll(it.dropDown.filter { it.isSelected }.map { it.label })
        }
        return list
    }

    private var locationToEdit = ""

    val _msgForSpclInvitees = MutableLiveData("")
    private val msgForSpclInvitees: LiveData<String> = _msgForSpclInvitees

    private fun getJsonString(obj: Any?): String {
        return try {
            val str = Gson().toJson(obj)
            str
        } catch (e: Exception) {
            ""
        }
    }

    val _msgForParticipant = MutableLiveData("")
    private val msgForParticipant: LiveData<String> = _msgForParticipant

    fun setMsgForARS() {
        val topics = getMeetingData().eventTopics.joinToString(", ")
        val amOrPm = if (meetingTime.value?.endsWith("am", ignoreCase = true) == true) AM else PM
        val time = meetingTime.value?.substringBefore(" ")
        _msgForSpclInvitees.value =
            COMMON_MSG.replace("_date_", meetingDate.value!!.substringBefore("T")).replace("_place_", "")
                .replace("_amOrPm_", amOrPm).replace("_time_", time!!).replace("_topics_", topics).replace("_mode_", ARS)
        _msgForParticipant.value =
            COMMON_MSG.replace("_date_", meetingDate.value!!.substringBefore("T"))
                .replace("_amOrPm_", amOrPm).replace("_time_", time).replace("_place_", "")
                .replace("_topics_", topics).replace("_mode_", ARS)

    }

    fun getMeetingData(): Meeting {
        return Meeting(
            activityName = ARS,
            eventTopics = getSelectedTopics(),
            invitees = getSpecialInvitees(),
            meetingDate = meetingDate.value!!,
            minutes = meetingTime.value!!,
            programType = ARS,
            schedule = Schedule(
                createdDate = meetingDate.value!!,
                eventDate = meetingDate.value!!.substringBefore("T"),
                inviteesMsgTemplate = msgForSpclInvitees.value ?: "",
                location = "",
                recipientMsgTemplate = msgForParticipant.value ?: "",
                startTime = meetingTime.value!!,
                status = null
            ),
            recipients = getMeetingRecipients(),
            audit = null,
            outcomes = null,
            id = null
        )
    }

    private val _meetingData = MutableLiveData<Meeting>()
    var meetingData: LiveData<Meeting?> = _meetingData
    val meetindId = handle.getLiveData("meetingId", "")

    val listingMode = MutableLiveData(UIMode.FULL_LIST)

    val specialInvitees = MutableLiveData<List<SpecialInvitee>>(listOf())
    val arsCommInvitees = MutableLiveData<List<SpecialInvitee>>(listOf())

    val minutesOfMeeting = MutableLiveData("")

    private fun getSpecialInvitees(): List<Invitee> {
        return specialInvitees.value?.filter { !it.name.value.isNullOrEmpty() }?.map {
            Invitee(it.name.value!!, it.emailId.value, it.phoneNumber.value)
        } ?: listOf()
    }

    fun removeInvitee(invitee: SpecialInvitee, position: Int) {
        val list = specialInvitees.value!!.toMutableList()
        list.removeAt(position)
        specialInvitees.value = list
    }

    fun addNewSpecialInvitee() {
        val list = specialInvitees.value?.toMutableList() ?: mutableListOf()
        val spclInvitee =
            SpecialInvitee(MutableLiveData(""), MutableLiveData(""), MutableLiveData(""))
        list.add(spclInvitee)
        specialInvitees.value = list
    }

    fun removeCommiteeInvitee(invitee: SpecialInvitee, position: Int) {
        val list = arsCommInvitees.value!!.toMutableList()
        list.removeAt(position)
        arsCommInvitees.value = list
    }

    fun addNewCommiteeInvitee() {
        val list = arsCommInvitees.value?.toMutableList() ?: mutableListOf()
        val spclInvitee =
            SpecialInvitee(MutableLiveData(""), MutableLiveData(""), MutableLiveData(""))
        list.add(spclInvitee)
        arsCommInvitees.value = list
    }

    private val refreshToggle = MutableLiveData(false)
    val meetings = refreshToggle.switchMap {
        meetingRepository.getMeetings(0, 1000, ARS)
    }

    fun cancelMeeting(meetingId: String): LiveData<Meeting?> {
        isLoading.value = true
        return meetingRepository.updateMeeting(meetingId, CancelMeeting())
    }

    fun refreshMeetings() {
        refreshToggle.value = !refreshToggle.value!!
    }

    val uploadImageMap = hashMapOf<String, Pair<String, Uri>>()
    val recipients = MutableLiveData<List<Recipient>>()


    fun updateOutcome(id: String): LiveData<Meeting?> {
        val outcome = UpdateOutcome(getMeetingOutcomes())
        val update = MeetingOutcome(outcome)
        return meetingRepository.updateMeeting(id, update)
    }

    private fun getMeetingOutcomes(): List<Outcome> {
        return uploadImageMap.map {
            Outcome(minutesOfMeeting.value ?: "", it.key)
        }
    }

    private fun getMeetingRecipients(): List<Recipient> {
       return arsCommInvitees.value?.filter { !it.name.value.isNullOrEmpty() }?.map {
            Recipient(
                age = 0,
                name = it.name.value!!,
                citizenId = "",
                hhHeadName = "",
                isCommiteeMember = true,
                contact = it.phoneNumber.value!!,
                emailId = it.emailId.value
            )
        } ?: listOf()
    }

    val invitesSent = hashMapOf<String, List<ResidentInHouseHoldResponse.Target>>()

    private fun getRecipients(): List<Recipient> {
        val list =
            mutableListOf<Recipient>().apply { addAll(recipients.value?.toList() ?: listOf()) }
        invitesSent.values.map { target ->
            target.forEach { targetItem ->
                if (list.any { it.citizenId == targetItem.properties.uuid }) {
                    return@forEach
                }
                list.add(
                    Recipient(
                        targetItem.properties.age,
                        targetItem.properties.firstName,
                        targetItem.properties.uuid,
                        targetItem.headName,
                        false,
                        targetItem.properties.contact
                    )
                )
            }
        }
        recipients.value = list
        return list
    }

    fun updateMeeting(): LiveData<Meeting?> {
        isLoading.value = true
        val meeting = getMeetingData()
        val updateMeeting = UpdateMeeting(meeting, "BASEPROGRAM")
        meetindId.value?.let {
            meetingData = meetingRepository.updateMeeting(it, updateMeeting)
        }
        return meetingData
    }

    fun createMeeting(): LiveData<Meeting?> {
        val meeting = getMeetingData()
        return if (meetindId.value.isNullOrEmpty()) {
            meeting.schedule?.status = "SCHEDULED"
            meetingData = meetingRepository.postMeeting(meeting)
            meetingData
        } else {
            updateMeeting()
        }
    }

    fun clearCaches() {
        meetingTime.value = ""
        meetingDate.value = ""
        _msgForParticipant.value = ""
        _msgForSpclInvitees.value = ""
        meetindId.value = ""
        specialInvitees.value = listOf()
        recipients.value = listOf()
        listingMode.value = UIMode.FULL_LIST
        invitesSent.clear()
        uploadImageMap.clear()
        arsCommInvitees.value = listOf()
        meetingTopicOptions.clearSelection()
    }

    fun setSpecialInviteeMode(isSpecialInvitee: Boolean) {
        isSpecialInviteeMode.value = isSpecialInvitee
    }

    fun getMeeting(id: String? = null): LiveData<Meeting?> {
        meetingData =
            meetingRepository.getMeeting(id ?: meetingData.value?.id ?: meetindId.value!!).map {
                it?.let {
                    setMeetingDetails(it)
                }
                it
            }
        return meetingData
    }

    private fun setMeetingDetails(it: Meeting) {
        meetingDate.value = it.schedule?.createdDate ?: ""
        meetingTime.value = it.schedule?.startTime ?: ""
        _msgForSpclInvitees.value = it.schedule?.inviteesMsgTemplate
        _msgForParticipant.value = it.schedule?.recipientMsgTemplate
        checkValidation()
    }

}