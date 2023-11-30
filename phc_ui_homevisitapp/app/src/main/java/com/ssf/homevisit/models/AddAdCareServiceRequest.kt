package com.ssf.homevisit.models

data class AddAdCareServiceRequest(
    val childCitizenId: String,
    val commoditiesProvided: List<ServiceData>,
    val councelingData: List<ServiceData>,
    val informationCollected: List<ServiceData>,
    val isReferredToPhc: Boolean,
    val visitDate: String
)

data class ServiceData(
    val info: String,
    val status: Any
)
