package com.ssf.homevisit.models



data class CcChildMotherDetailRequest(
    val relationshipType: String,
    val sourceType: String,
    val srcInClause: SrcinClauseMotherDetails,
    val stepCount: Int,
    val targetType: String
)

data class SrcinClauseMotherDetails(
    val uuid: List<String>
)
