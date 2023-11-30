package com.ssf.homevisit.models

data class Meeting(
    val activityName: String,
    val audit: Audit?,
    val eventTopics: List<String>,
    val id: String?,
    val invitees: List<Invitee>?,
    val meetingDate: String,
    val minutes: String,
    val outcomes: List<Outcome>?,
    val programType: String,
    val recipients: List<Recipient>,
    val schedule: Schedule?
)

data class Invitee(
    val name:String,
    val email: String?,
    val phone: String?
)

data class Audit(
    val _id:String,
    val createdBy: String,
    val dateCreated: String,
    val dateModified: String,
    val modifiedBy: String
)

data class Outcome(
    val imgUrl: String,
    val title: String
)

data class Recipient(
    val age: Int?,
    val name: String,
    val citizenId: String,
    val hhHeadName: String,
    var isCommiteeMember: Boolean = false,
    val contact: String? = "",
    val emailId: String? = null
)

data class Schedule(
    val createdDate: String,
    val eventDate: String,
    val inviteesMsgTemplate: String,
    val location: String,
    val recipientMsgTemplate: String,
    val startTime: String,
    var status: String?
)

data class ScheduleCancel(
    val status: String = "CANCELLED"
)

data class UpdateMeeting(
    val properties: Meeting,
    val type: String
)

data class CancelMeeting(
    val properties: ScheduleCancel = ScheduleCancel(),
    val type: String = "SCHEDULE"
)

data class UpdateMeetingSchedule(
    val properties: Schedule,
    val type: String
)

data class UpdateOutcome(
    val outcomes: List<Outcome>
)

data class MeetingOutcome(
    val properties: UpdateOutcome,
    val type: String = "OUTCOMES"
)


