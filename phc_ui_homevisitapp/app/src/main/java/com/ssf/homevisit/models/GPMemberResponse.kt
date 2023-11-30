package com.ssf.homevisit.models

data class GPMemberResponse(
    val content: List<ContentGP>,
    val totalElements: Int,
    val totalPages: Int
)

data class ContentGP(
    val relationship: Relationship,
    val sourceNode: SourceNode,
    val targetNode: TargetNode,
    var isSelected: Boolean = false
)

data class Properties(
    val type: String
)