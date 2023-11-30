package com.ssf.homevisit.models

data class CCHouseHoldRequest(
    val relationshipType: String,
    val sourceProperties: SourceProperties,
    val sourceType: String,
    val stepCount: Int,
    val targetProperties: TargetProperties,
    val targetType: String
)

data class SourceProperties(
    val uuid: String
)

data class TargetProperties(
    val hasEligibleChildCare: Boolean
//    val hasEligibleAdolescentCare: Boolean = false
)
