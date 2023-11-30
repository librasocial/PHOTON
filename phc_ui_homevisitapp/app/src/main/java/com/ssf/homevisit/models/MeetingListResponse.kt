package com.ssf.homevisit.models

import com.ssf.homevisit.models.SurveyFilterResponse.Meta

data class MeetingListResponse(
    val `data`: List<Meeting>,
    val meta: Meta
)