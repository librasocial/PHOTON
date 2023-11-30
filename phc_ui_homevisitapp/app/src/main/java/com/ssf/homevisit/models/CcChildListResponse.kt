package com.ssf.homevisit.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class CcChildListResponse(
    val content: List<CcChildListContent>,
    val totalElements: Int,
    val totalPages: Int
)

@Parcelize
data class CcChildListContent(
    val relationship: CcChildListContentRelationship,
    val sourceNode: CcChildListContentSourceNode,
    val targetNode: CcChildListContentTargetNode
) : Parcelable

@Parcelize
data class CcChildListContentRelationship(
    val id: Int?,
    val properties: CcChildListContentRelationshipProperties,
    val type: String
) : Parcelable

@Parcelize
data class CcChildListContentSourceNode(
    val id: Int,
    val labels: List<String>,
    val properties: CcChildListContentSourceNodeProperties
) : Parcelable

@Parcelize
data class CcChildListContentTargetNode(
    val id: Int,
    val labels: List<String>,
    val properties: CcChildListContentTargetNodeProperties?=null
) : Parcelable

@Parcelize
class CcChildListContentRelationshipProperties : Parcelable

@Parcelize
data class CcChildListContentSourceNodeProperties(
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
        val firstName: String?,
        val birthWeight: String?=null,
        val healthID:String?=null,
        val imageUrls:List<String>?=null
) : Parcelable

@Parcelize
data class CcChildListContentTargetNodeProperties(
    val boundary: String?=null,
    val houseHoldCount: Double,
    val lgdCode: String,
    val name: String,
    val totalPopulation: Double,
    val type: String,
    val houseHoldName: String?
) : Parcelable