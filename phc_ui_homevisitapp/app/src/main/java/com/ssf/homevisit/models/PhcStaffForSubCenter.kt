package com.ssf.homevisit.models

data class PhcStaffSubCenter(
    val content: List<PhcContent>,
    val totalElements: Int,
    val totalPages: Int
)

data class PhcContent(
    val relationship: Relationship,
    val sourceNode: SourceNode,
    val targetNode: TargetNode
)

data class Relationship(
    val id: Int,
    val properties: RelationProperties,
    val type: String
)

data class SourceNode(
    val id: Int,
    val labels: List<String>,
    val properties: PhcStaffSourceProperties
)

data class TargetNode(
    val id: Int,
    val labels: List<String>,
    val properties: PhcStaffTargetProperties
)

data class RelationProperties(
    val type: String
)

data class PhcStaffSourceProperties(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val type: String,
    val uuid: String
)

data class PhcStaffTargetProperties(
    val createdBy: String,
    val dateCreated: String,
    val dateModified: String,
    val modifiedBy: String,
    val name: String,
    val email: String?,
    val contact: String,
    val type: String,
    val uuid: String,
    val phoneNumber: String // in case of GPMember
)