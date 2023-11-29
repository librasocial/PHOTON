package com.ssf.homevisit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlin.math.roundToInt


data class CCHouseHoldResponse(
    val content: List<CCHouseHoldContent>,
    val totalElements: Int,
    val totalPages: Int
)

data class CCHouseHoldContent(
    val relationship: CCHouseHoldContentRelationship,
    val sourceNode: SourceNodeCCHouseHold,
    val targetNode: TargetNodeCCHouseHold
)

data class CCHouseHoldContentRelationship(
    val id: Int,
    val properties: Properties,
    val type: String
)

data class SourceNodeCCHouseHold(
    val id: Int,
    val labels: List<String>,
    val properties: SourceNodeCCHouseHoldProperties
)

data class TargetNodeCCHouseHold(
    val id: Int,
    val labels: List<String>,
    val properties: TargetNodeCCHouseHoldProperties
)

data class SourceNodeCCHouseHoldProperties(
    val boundary: String?=null,
    val houseHoldCount: Double,
    val lgdCode: String,
    val name: String,
    val totalPopulation: Double,
    val type: String,
    val uuid: String
)

@Parcelize
data class TargetNodeCCHouseHoldProperties(
    val createdBy: String,
    val dateCreated: String,
    val dateModified: String,
    val hasDelivered: Boolean,
    val hasEligibleChildCare: Boolean,
    val hasEligibleCouple: Boolean,
    val hasNewBorn: Boolean,
    val hasPregnantWoman: Boolean,
    val hasRegisteredAdolescentCare: Boolean,
    val hasRegisteredChildCare: Boolean,
    val houseHeadImageUrls: List<String>,
    val houseHeadName: String,
    val houseID: String,
    val latitude: Double,
    val longitude: Double,
    val modifiedBy: String,
    val pregnantLadyName: String,
    val totalFamilyMembers: Double,
    val type: String,
    val uuid: String
) : Parcelable {
    fun getMembersCount() = totalFamilyMembers.roundToInt().toString()
}