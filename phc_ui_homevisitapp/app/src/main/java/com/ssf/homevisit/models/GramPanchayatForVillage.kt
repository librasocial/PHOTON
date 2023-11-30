package com.ssf.homevisit.models

data class GramPanchayatForVillage(
    val content: List<ContentGPForVillage>,
    val totalElements: Int,
    val totalPages: Int
)

data class ContentGPForVillage(
    val relationship: Relationship,
    val source: SourceNode,
    val target: TargetNode,
)