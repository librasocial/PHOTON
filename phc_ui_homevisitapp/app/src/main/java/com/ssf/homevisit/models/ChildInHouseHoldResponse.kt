package com.ssf.homevisit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class ChildInHouseHoldResponse(
    val content: List<ChildInHouseContent>,
    val totalElements: Int,
    val totalPages: Int
)

@Parcelize
data class ChildInHouseContent(
    val relationship: ChildInHouseRelationship,
    val sourceNode: ChildInHouseSourceNode,
    val targetNode: ChildInHouseTargetNode
): Parcelable

@Parcelize
data class ChildInHouseRelationship(
    val id: Int?,
    val properties: ChildInHouseProperties,
    val type: String
): Parcelable

@Parcelize
data class ChildInHouseSourceNode(
    val id: Int,
    val labels: List<String>,
    val properties: ChildInHousePropertiesX
): Parcelable

@Parcelize
data class ChildInHouseTargetNode(
    val id: Int,
    val labels: List<String>,
    val properties: ChildInHousePropertiesXX?=null
): Parcelable

@Parcelize
data class ChildInHouseProperties(
    val type: String
): Parcelable

@Parcelize
data class ChildInHousePropertiesX(
    val age: Double,
    val createdBy: String,
    val dateCreated: String,
    val dateModified: String,
    val dateOfBirth: String,
    val gender: String,
    val isAdult: Boolean,
    val modifiedBy: String,
    val rmnchaServiceStatus: String,
    val type: String,
    val uuid: String,
    val imageUrls: List<String>?,
    val healthID: String?,
    val firstName: String?,
    var motherName:String?=null
): Parcelable

@Parcelize
data class ChildInHousePropertiesXX(
    val createdBy: String,
    val dateCreated: String,
    val dateModified: String,
    val hasDelivered: Boolean,
    val hasEligibleChildCare: Boolean,
    val hasEligibleCouple: Boolean,
    val hasNewBorn: Boolean,
    val hasPregnantWoman: Boolean,
    val hasRegisteredChildCare: Boolean,
    val houseHeadImageUrls: List<String>?,
    val houseHeadName: String,
    val houseID: String,
    val modifiedBy: String,
    val pregnantLadyName: String,
    val totalFamilyMembers: Double,
    val type: String,
    val uuid: String
): Parcelable

data class ChildInHouseholdRequest(
    val relationshipType: String,
    val sourceProperties: ChildInHouseholdRequestSourceProperties,
    val sourceType: String,
    val srcInClause: ChildInHouseholdRequestSrcInClause,
    val stepCount: Int,
    val targetProperties: ChildInHouseholdRequestTargetProperties,
    val targetType: String
)

data class ChildInHouseholdRequestSourceProperties(
    val isAdult: Boolean
)

data class ChildInHouseholdRequestSrcInClause(
    val rmnchaServiceStatus: List<String>
)

data class ChildInHouseholdRequestTargetProperties(
    val uuid: String
)