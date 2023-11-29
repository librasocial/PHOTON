package com.ssf.homevisit.models

data class CcChildListRequest(
    val relationshipType: String,
    val sourceProperties: CcChildListSourceProperties,
    val sourceType: String,
    val srcInClause: SrcInClause,
    val stepCount: Int,
    val targetProperties: CcChildListTargetProperties,
    val targetType: String
)

data class CcChildListSourceProperties(
    val isAdult: Boolean
)

data class SrcInClause(
    val rmnchaServiceStatus: List<String>
)

data class CcChildListTargetProperties(
    val uuid: String
)