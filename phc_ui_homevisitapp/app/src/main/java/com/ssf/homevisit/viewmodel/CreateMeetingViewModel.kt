package com.ssf.homevisit.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import com.google.gson.Gson
import com.ssf.homevisit.activity.VHSNCActivity
import com.ssf.homevisit.app.AndroidApplication
import com.ssf.homevisit.fragment.vhnd.VHNDTopics
import com.ssf.homevisit.fragment.vhsnc.AllInviteeItem
import com.ssf.homevisit.fragment.vhsnc.BottomBarMode
import com.ssf.homevisit.fragment.vhsnc.ListMode
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.HouseHoldLevelMappingRepository
import com.ssf.homevisit.repository.MappingRepository
import com.ssf.homevisit.repository.MeetingRepository
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class CreateMeetingViewModel(private val handle: SavedStateHandle) : ViewModel() {

    val village = handle.getLiveData<SubCVillResponse.Content>(VHSNCActivity.keyVillage)
    val subCenter =
        handle.getLiveData<SubcentersFromPHCResponse.Content>(VHSNCActivity.keySubCenter)
    val vhc = handle.getLiveData<AllPhcResponse.Content>(VHSNCActivity.keyVhc)
    val vhsncOrVhd = handle.getLiveData<String>(VHSNCActivity.keyVhsncOrVhd)

    val gramPanchayat = MutableLiveData<PhcStaffTargetProperties>()

    val isVHND = vhsncOrVhd.value == VHND

    val isVHNDLivData = liveData<Boolean> { isVHND }

    val query = MutableLiveData("")

    var isRescheduling = MutableLiveData(false)

    val chipMap = ChipMap()
    val isValidData = MutableLiveData(false)
    val meetindId = handle.getLiveData("meetingId", "")
    val houseHoldId = handle.getLiveData("householdID", "")

    val page = MutableLiveData(0)
    private val meetingRepository = MeetingRepository()
    private val mappingRepository = MappingRepository()
    private val houseHoldRepo = HouseHoldLevelMappingRepository()
    private val villageRepo = VillageLevelMappingRepository()
    var totalPages = 0
    private var totalItems = 0
    val isLoading = MutableLiveData(false)
    private val houseHoldList = mutableListOf<HouseHoldInVillageResponse.Content>()

    private val venueType = MutableLiveData(0)

    var map1 = hashMapOf<Int, Int>()
    private val venuesList = venueType.distinctUntilChanged().switchMap { type ->
        setVenueSelection(null)
        val typeStr = when (type) {
            0 -> "####"
            1 -> "office"
            2 -> "school,collage,college,schoolcollage,schoolcollege"
            else -> ""
        }
        villageRepo.getPlacesInVillageData(village.value!!.target.villageProperties.uuid, 0, 1000)
            .map {
                val venues = it.content.onEach { it.selected = false }
                val arrStr = typeStr.split(",")
                when (type) {
                    4 -> {
                        val anganwadi = venues.firstOrNull {
                            it.target.properties.assetSubType.equals(
                                ANGANWADI, true
                            )
                        }?.target?.properties ?: getStaticVenue()
                        setVHNDVenue(anganwadi)
                        venues
                    }
                    0 -> listOf<PlacesInVillageResponse.Content>()
                    3 -> {
                        val allTypes =
                            "school,collage,college,schoolcollage,schoolcollege,office".split(",")
                        venues.filter { venueItem ->
                            allTypes.any { typeItem ->
                                !typeItem.equals(
                                    venueItem.target.properties.assetType,
                                    true
                                )
                            }
                        }
                    }
                    else -> venues.filter { venueItem ->
                        arrStr.any { typeItem ->
                            typeItem.equals(
                                venueItem.target.properties.assetType,
                                true
                            )
                        }
                    }
                }.apply {
                    // this works when re-scheduling a meeting
                    this.firstOrNull { it.target.properties.name == locationToEdit }?.selected =
                        true
                }
            }
    }

    val phcStaff = subCenter.switchMap {
        mappingRepository.getPHCStaffList("c87de2b8-afc6-4ff7-8886-eef129f5f96c")
    }

    private fun getStaticVenue() =
        PlaceProperties().apply {
            this.name = ANGANWADI
            this.uuid = ANGANWADI.hashCode().toString()
            this.villageId = village.value!!.target.villageProperties.uuid
        }


    private val participantFilter = MediatorLiveData<Pair<String, Int>>().apply {
        addSource(query) {
            listingMode.value = UIMode.FULL_LIST
            value = Pair(it, page.value!!)
        }
        addSource(page) {
            value = Pair(query.value!!, it)
        }
    }

    val houseHoldsList = participantFilter.distinctUntilChanged().switchMap { filter ->
        if (filter.first.isEmpty()) {
            page.value = 0
        }
        isLoading.value = true
        houseHoldRepo.getHouseholdInVillageData(
            village.value!!.target.villageProperties.uuid,
            filter.second,
            10000
        ).map { response ->
            isLoading.value = false
            response.let {
                totalPages = response.totalPages
                totalItems = response.totalElements
            }
            if (page.value == 0) {
                houseHoldList.clear()
            }
            if (filter.first.isNotEmpty()) {
                houseHoldList.clear()
                houseHoldList.addAll(response.content.filter {
                    !it.target.properties.houseHeadName.isNullOrEmpty() && it.target.properties.houseHeadName.contains(
                        filter.first,
                        true
                    )
                })
            } else {
                houseHoldList.clear()
                houseHoldList.addAll(response.content)
            }
            houseHoldList
        }
    }

    var bottomBarMode = BottomBarMode.FINISH

    private val _meetingData = MutableLiveData<Meeting>()
    var meetingData: LiveData<Meeting?> = _meetingData

    private val _meetingDate = MutableLiveData("")
    val meetingDate: LiveData<String> = _meetingDate
    fun setMeetingDate(yr: Int, month: Int, day: Int) {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, yr)
        cal.set(Calendar.MONTH, month)
        cal.set(Calendar.DAY_OF_MONTH, day)
        val myFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        _meetingDate.value = sdf.format(cal.time)
        checkValidation()
    }

    val venues: LiveData<List<PlacesInVillageResponse.Content>> = venuesList

    private val _venueSelection = MutableLiveData<PlacesInVillageResponse.Content?>()
    val venueSelection = _venueSelection
    fun setVenueSelection(venue: PlacesInVillageResponse.Content?) {
        _venueSelection.value = venue
        checkValidation()
    }

    private val _meetingTime = MutableLiveData<String>()
    val meetingTime = _meetingTime
    fun setMeetingTime(hour: Int, min: Int) {
        try {
            val fmt = SimpleDateFormat("HH:mm")
            val date = fmt.parse("$hour:$min")
            val dateFormat = SimpleDateFormat("hh:mm aa")
            _meetingTime.value = dateFormat.format(date)
            checkValidation()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    val assetForVenue = venueType
    fun setAsset(asset: Int) {
        venueType.value = asset
        checkValidation()
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

    fun getMeeting(id: String?=null): LiveData<Meeting?> {
        meetingData = meetingRepository.getMeeting(id?: meetingData.value?.id ?: meetindId.value!!).map {
            it?.let {
                setMeetingDetails(it)
            }
            it
        }
        return meetingData
    }

    private fun setMeetingDetails(it: Meeting) {
        _meetingDate.value = it.schedule?.createdDate ?: ""
        _meetingTime.value = it.schedule?.startTime ?: ""
        _venueSelection.value = PlacesInVillageResponse.Content().apply {
            this.selected = true
            this.target = PlacesInVillageResponse.Target().apply {
                this.id = it.schedule?.location.hashCode()
                locationToEdit = it.schedule?.location ?: ""
                try {
                    this.properties =
                        Gson().fromJson(it.schedule?.location ?: "", PlaceProperties::class.java)
                    setAsset(getAssetTypeForVenue(it.schedule?.location ?: ""))
                } catch (e: Exception) {
                    this.properties = PlaceProperties().apply {
                        this.name = it.schedule?.location
                        locationToEdit = it.schedule?.location ?: ""
                        setAsset(getAssetTypeForVenue(it.schedule?.location ?: ""))
                    }
                }
            }
        }

        _msgForSpclInvitees.value = it.schedule?.inviteesMsgTemplate
        _msgForParticipant.value = it.schedule?.recipientMsgTemplate
        checkValidation()
//        setMsgForParticipant()
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

    private fun setVHNDVenue(anganwadi: PlaceProperties) {
        _venueSelection.value = PlacesInVillageResponse.Content().apply {
            this.selected = true
            this.target = PlacesInVillageResponse.Target().apply {
                this.id = anganwadi.name.hashCode()
                locationToEdit = anganwadi.name ?: ""
                this.properties = anganwadi
            }
        }
    }

    fun updateMeetingSchedule(): LiveData<Meeting?> {
        isLoading.value = true
        val schedule = Schedule(
            createdDate = meetingDate.value!!,
            eventDate = meetingDate.value!!.substringBefore("T"),
            inviteesMsgTemplate = msgForSpclInvitees.value ?: "",
            location = getJsonString(venueSelection.value?.target?.properties),
            recipientMsgTemplate = msgForParticipant.value ?: "",
            startTime = meetingTime.value!!,
            status = null
        )
        val updateMeeting = UpdateMeetingSchedule(schedule, "SCHEDULE")
        meetindId.value?.let {
            meetingData = meetingRepository.updateMeeting(it, updateMeeting)
        }
        return meetingData
    }

    fun cancelMeeting(meetingId: String): LiveData<Meeting?> {
        isLoading.value = true
        return meetingRepository.updateMeeting(meetingId, CancelMeeting())
    }

    private fun getAssetTypeForVenue(name: String): Int {
        return when {
            isVHND -> 4
            name.contains("college", true) || name.contains("collage", true) || name.contains("school", true) -> 2
            name.contains("office", true) -> 1
            else -> 3
        }
    }

    fun hideLoading() {
        isLoading.value = false
    }

    val _msgForParticipant = MutableLiveData("")
    private val msgForParticipant: LiveData<String> = _msgForParticipant

    fun setMsgForVHND() {
        val amOrPm = if (_meetingTime.value?.endsWith("am", ignoreCase = true) == true) AM else PM
        val time = _meetingTime.value?.substringBefore(" ")
        val user = AndroidApplication.prefs.getString(AppDefines.USER_NAME, "")
        _msgForSpclInvitees.value = COMMON_MSG.replace("_date_", meetingDate.value!!.substringBefore("T")).replace("_amOrPm_", amOrPm).replace("_time_", time!!)
            .replace("_topics_", "").replace("_mode_", if(isVHND) VHND else VHSNC)
        _msgForParticipant.value = COMMON_MSG.replace("_date_", meetingDate.value!!.substringBefore("T")).replace("_amOrPm_", amOrPm).replace("_time_", time!!)
            .replace("_topics_", "").replace("_mode_", if(isVHND) VHND else VHSNC)

    }

    fun setMsgForVHSNC() {
        val topics = getMeetingData().eventTopics.joinToString(", ")
        val amOrPm = if (_meetingTime.value?.endsWith("am", ignoreCase = true) == true) AM else PM
        val time = _meetingTime.value?.substringBefore(" ")
        val place = _venueSelection.value?.target?.properties?.name
        val user = AndroidApplication.prefs.getString(AppDefines.USER_NAME, "")
        _msgForSpclInvitees.value = COMMON_MSG.replace("_date_", meetingDate.value!!.substringBefore("T")).replace("_amOrPm_", amOrPm).replace("_time_", time!!)
            .replace("_place_", place!!).replace("_topics_", topics).replace("_user_", user!!).replace("_mode_", if(isVHND) VHND else VHSNC)
        _msgForParticipant.value = COMMON_MSG.replace("_date_", meetingDate.value!!.substringBefore("T")).replace("_amOrPm_", amOrPm).replace("_time_", time!!)
                .replace("_place_", place).replace("_topics_", topics).replace("_user_", user).replace("_mode_", if(isVHND) VHND else VHSNC)

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

    fun getMeetingData(): Meeting {
        return Meeting(
            activityName = vhsncOrVhd.value ?: VHSNC,
            eventTopics = getSelectedTopics(),
            invitees = getSpecialInvitees(),
            meetingDate = meetingDate.value!!,
            minutes = meetingTime.value!!,
            programType = vhsncOrVhd.value ?: VHSNC,
            schedule = Schedule(
                createdDate = meetingDate.value!!,
                eventDate = meetingDate.value!!.substringBefore("T"),
                inviteesMsgTemplate = msgForSpclInvitees.value ?: "",
                location = getJsonString(venueSelection.value?.target?.properties),
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

    private fun getMeetingRecipients(): List<Recipient> {
        if (isRescheduling.value!!) {
            return meetingData.value?.recipients ?: listOf()
        }
        return recipients.value?: listOf()
    }

    private fun getRecipients() : List<Recipient> {
        val list = mutableListOf<Recipient>().apply { addAll(recipients.value?.toList()?: listOf()) }
        invitesSent.values.map { target->
            target.forEach { targetItem ->
                if (list.any { it.citizenId == targetItem.properties.uuid }) {
                    return@forEach
                }
                list.add(Recipient(targetItem.properties.age ?: 0, targetItem.properties.firstName, targetItem.properties.uuid, targetItem.headName, false, targetItem.properties.contact))
            }
        }
        recipients.value = list
        return list
    }

    private fun getSelectedTopics(): List<String> {
        if (isVHND) {
            return createVHNDTopics()
        } else {
            if (isRescheduling.value!!) {
                return meetingData.value?.eventTopics ?: listOf()
            }
            val list = arrayListOf<String>()
            chipMap.getDropdownViews().forEach {
                list.addAll(it.dropDown.filter { it.isSelected }.map { it.label })
            }
            return list
        }

    }

    fun checkValidation() {
        if (isVHND) {
            if (isRescheduling.value!!) {
                isValidData.value =
                    venueSelection.value != null && !meetingDate.value.isNullOrEmpty() && !meetingTime.value.isNullOrEmpty()
                return
            }
            val isValid =
                venueSelection.value != null && !meetingDate.value.isNullOrEmpty() && !meetingTime.value.isNullOrEmpty()
            isValidData.value = isValid
        } else {
            if (isRescheduling.value!!) {
                isValidData.value =
                    venueSelection.value != null && !meetingDate.value.isNullOrEmpty() && !meetingTime.value.isNullOrEmpty()
                return
            }
            val isValid =
                venueSelection.value != null && !meetingDate.value.isNullOrEmpty() && !meetingTime.value.isNullOrEmpty() && getSelectedTopics().isNotEmpty()
            isValidData.value = isValid
        }
    }

    fun clearData() {
        _meetingDate.value = ""
        _meetingDate.value = ""
        _venueSelection.value = null
        _msgForParticipant.value = ""
        _msgForSpclInvitees.value = ""
        venueType.value = 0
        meetindId.value = ""
        houseHoldId.value = ""
        specialInvitees.value = listOf()
        recipients.value = listOf()
        listingMode.value = UIMode.FULL_LIST
        invitesSent.clear()
        uploadImageMap.clear()
        invitedGpMembers.clear()
        chipMap.clearSelection()

        childCare.clear()
        anteNatalCare.clear()
        postNatalCare.clear()
        childCare.clear()
        adolescentCare.clear()
    }

    fun sendInvites(): Boolean {
        val invites = members.value?.filter { (it.checked ?: false) }
            ?.map { it.target } ?: listOf()
        invitesSent[houseHoldId.value ?: ""] = invites
        getRecipients()
        return true
    }

    fun getInvitesOfHousehold(): List<ResidentInHouseHoldResponse.Target>? {
        return invitesSent[houseHoldId.value ?: ""]
    }

    val invitesSent = hashMapOf<String, List<ResidentInHouseHoldResponse.Target>>()

    val recipients = MutableLiveData<List<Recipient>>()

    fun getInviteesCount(): Int {
        var count = 0
        invitesSent.forEach {
            count += it.value.size
        }
        return count
    }

    fun addNewSpecialInvitee() {
        val list = specialInvitees.value?.toMutableList() ?: mutableListOf()
        val spclInvitee =
            SpecialInvitee(MutableLiveData(""), MutableLiveData(""), MutableLiveData(""))
        list.add(spclInvitee)
        specialInvitees.value = list
    }

    fun addPhcStaffInvitee(invitees: List<SpecialInvitee>) {
        val list = specialInvitees.value?.toMutableList() ?: mutableListOf()
        list.addAll(invitees)
        specialInvitees.value = list
    }

    val members = houseHoldId.distinctUntilChanged().switchMap { householdId ->
        if (!householdId.isNullOrEmpty()) {
            isLoading.value = true
            houseHoldRepo.getResidentsInHousehold(householdId, 0, 100).map { response ->
                isLoading.value = false
                val headName =
                    response.content.firstOrNull { it.target.properties.isHead }?.target?.properties?.firstName
                        ?: ""
                response.content.onEach { contentItem ->
                    contentItem.target.headName = headName
                    contentItem.checked =
                        (getInvitesOfHousehold()?.any { it.properties.uuid == contentItem.target.properties.uuid }
                            ?: false)
                }
            }
        } else {
            isLoading.value = false
            liveData { listOf<ResidentInHouseHoldResponse.Content>() }
        }
    }

    val listingMode = MutableLiveData(UIMode.FULL_LIST)

    val specialInvitees = MutableLiveData<List<SpecialInvitee>>(listOf())

    private fun getSpecialInvitees(): List<Invitee> {
        if (isRescheduling.value!!) {
            return meetingData.value?.invitees ?: listOf()
        }
        val list = specialInvitees.value?.filter { !it.name.value.isNullOrEmpty() }?.map {
            Invitee(it.name.value!!, it.emailId.value, it.phoneNumber.value)
        }?.toMutableList()
        val gpMemberInvitees = invitedGpMembers.values.map { Invitee(it.properties.name, it.properties.email, it.properties.phoneNumber) }
        list?.addAll(gpMemberInvitees)
        return list ?: listOf()
    }

    fun removeInvitee(invitee: SpecialInvitee, position: Int) {
        val list = specialInvitees.value!!.toMutableList()
        list.removeAt(position)
        specialInvitees.value = list
    }

    private val refreshToggle = MutableLiveData(false)
    val meetings = refreshToggle.switchMap {
        meetingRepository.getMeetings(0, 1000, vhsncOrVhd.value?: VHSNC)
    }

    fun refreshMeetings() {
        refreshToggle.value = !refreshToggle.value!!
    }

    val uploadImageMap = hashMapOf<String, Pair<String, Uri>>()

    fun updateOutcome(id: String): LiveData<Meeting?> {
        val outcome = UpdateOutcome(getMeetingOutcomes())
        val update = MeetingOutcome(outcome)
        return meetingRepository.updateMeeting(id, update)
    }

    private fun getMeetingOutcomes(): List<Outcome> {
        return uploadImageMap.map {
            Outcome(it.value.first, it.key)
        }
    }

    var eligibleCouple = hashMapOf<String, Int>().apply {
        put(VHNDTopics.ELIGIBLE_COUPLE_Q1, 0)
        put(VHNDTopics.ELIGIBLE_COUPLE_Q2, 0)
        put(VHNDTopics.ELIGIBLE_COUPLE_Q3, 0)
        put(VHNDTopics.ELIGIBLE_COUPLE_Q4, 0)
        put(VHNDTopics.ELIGIBLE_COUPLE_Q5, 0)
    }

    var anteNatalCare = hashMapOf<String, Int>().apply {
        put(VHNDTopics.ANC_Q1, 0)
        put(VHNDTopics.ANC_Q2, 0)
        put(VHNDTopics.ANC_Q3, 0)
        put(VHNDTopics.ANC_Q4, 0)
        put(VHNDTopics.ANC_Q5, 0)
        put(VHNDTopics.ANC_Q6, 0)
        put(VHNDTopics.ANC_Q7, 0)
    }

    var postNatalCare = hashMapOf<String, Int>().apply {
        put(VHNDTopics.PNC_Q1, 0)
        put(VHNDTopics.PNC_Q2, 0)
        put(VHNDTopics.PNC_Q3, 0)
        put(VHNDTopics.PNC_Q4, 0)
        put(VHNDTopics.PNC_Q5, 0)
        put(VHNDTopics.PNC_Q6, 0)
        put(VHNDTopics.PNC_Q7, 0)
    }

    var childCare = hashMapOf<String, Int>().apply {
        put(VHNDTopics.CHILD_CARE_Q1, 0)
        put(VHNDTopics.CHILD_CARE_Q2, 0)
        put(VHNDTopics.CHILD_CARE_Q3, 0)
        put(VHNDTopics.CHILD_CARE_Q4, 0)
        put(VHNDTopics.CHILD_CARE_Q5, 0)
        put(VHNDTopics.CHILD_CARE_Q6, 0)
        put(VHNDTopics.CHILD_CARE_Q7, 0)
        put(VHNDTopics.CHILD_CARE_Q8, 0)
        put(VHNDTopics.CHILD_CARE_Q9, 0)
        put(VHNDTopics.CHILD_CARE_Q10, 0)
    }

    var adolescentCare = hashMapOf<String, Int>().apply {
        put(VHNDTopics.ADS_CARE_Q1, 0)
        put(VHNDTopics.ADS_CARE_Q2, 0)
        put(VHNDTopics.ADS_CARE_Q3, 0)
        put(VHNDTopics.ADS_CARE_Q4, 0)
        put(VHNDTopics.ADS_CARE_Q5, 0)
        put(VHNDTopics.ADS_CARE_Q6, 0)
        put(VHNDTopics.ADS_CARE_Q7, 0)
        put(VHNDTopics.ADS_CARE_Q8, 0)
        put(VHNDTopics.ADS_CARE_Q9, 0)
    }

    private fun createVHNDTopics(): List<String> {
        val topicList = mutableListOf<String>()
        topicList.addAll(eligibleCouple.filter { it.value>0 }.map { "${it.key}:${it.value}" })
        topicList.addAll(anteNatalCare.filter { it.value>0 }.map { "${it.key}:${it.value}" })
        topicList.addAll(postNatalCare.filter { it.value>0 }.map { "${it.key}:${it.value}" })
        topicList.addAll(childCare.filter { it.value>0 }.map { "${it.key}:${it.value}" })
        topicList.addAll(adolescentCare.filter { it.value>0 }.map { "${it.key}:${it.value}" })
        return topicList
    }

    fun getAllParticipants(type: ListMode): LiveData<List<AllInviteeItem>> {
        when(type) {
            ListMode.Participants -> {
                val list = recipients.value?.map {
                    AllInviteeItem(name = it.name, email = "", contact = it.contact)
                } ?: emptyList()
                return liveData {
                    emit(list)
                }
            }
            ListMode.SpecialInvitees -> {
                val list = specialInvitees.value?.map {
                    AllInviteeItem(name = it.name.value?:"", email = it.emailId.value, contact = it.phoneNumber.value)
                } ?: emptyList()
                return liveData {
                    emit(list)
                }
            }
            ListMode.GPMembers -> {
                val list = invitedGpMembers.values.map {
                    AllInviteeItem(
                        name = it.properties.name,
                        email = it.properties.email,
                        contact = it.properties.phoneNumber
                    )
                }
                return liveData {
                    emit(list)
                }
            }
        }
    }

    fun toggleGPMemberInvite(gpContent: ContentGP) {
        if (gpContent.isSelected) {
            invitedGpMembers[gpContent.targetNode.id] = gpContent.targetNode
        } else {
            invitedGpMembers.remove(gpContent.targetNode.id)
        }
    }

    val invitedGpMembers = hashMapOf<Int, TargetNode>()

    //    val gramPanchayat = mappingRepository.getGramPanchayatOfVillage(village.value?.target?.villageProperties?.uuid)
    val gpMembers = village.switchMap { village ->
        isLoading.value = true
        mappingRepository.getGramPanchayatOfVillage(village.target.villageProperties.uuid).switchMap { gramPanchayatRes ->
            isLoading.value = false
            gramPanchayatRes?.takeIf { it.content.isNotEmpty() }?.let {
                gramPanchayat.value = it.content[0].target.properties
                mappingRepository.getGPMemberList(it.content[0].target.properties.uuid)
            }
        }
    }
}

enum class UIMode(val modeId: Int) {
    FULL_LIST(0), INVITED_LIST(1);

    fun getInstance(modeId: Int): UIMode? {
        return values().firstOrNull { it.modeId == modeId }
    }
}