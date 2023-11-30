package com.ssf.homevisit.models

data class ChildMotherDetailResponse(
    val content: List<ChildMotherDetailContent>,
    val totalElements: Int,
    val totalPages: Int
)

data class ChildMotherDetailContent(
    val relationship: ChildMotherDetailRelationship,
    val sourceNode: ChildMotherDetail,
    val targetNode: ChildMotherDetail
)

data class ChildMotherDetailRelationship(
    val id: Int,
    val properties: ChildMotherDetailProperties,
    val type: String
)

data class ChildMotherDetail(
    val id: Int,
    val labels: List<String>,
    val properties: ChildMotherDetailPropertiesX
)

data class ChildMotherDetailProperties(
    val ccRegistrationId: String,
    val pncInfantRegistrationId: String,
    val pncRegistrationId: String,
    val type: String,
    val uuid: String,
    val childId:String
)

data class ChildMotherDetailPropertiesX(
    val age: Double,
    val createdBy: String,
    val dateCreated: String,
    val dateModified: String,
    val dateOfBirth: String,
    val rchId: String?,
    val gender: String,
    val isAdult: Boolean,
    val modifiedBy: String,
    val rmnchaServiceStatus: String,
    val type: String,
    val uuid: String,
    val contact: String,
    val firstName: String,
    val imageUrls: List<String>?,
    val isHead: Boolean,
    val isPregnant: Boolean,
    val lastName: String,
    val middleName: String,
    val residingInVillage: String,
    val healthID:String?=null
)