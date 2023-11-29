package com.ssf.homevisit.healthWellnessSurveillance.ui.village.models

import com.google.gson.annotations.SerializedName

data class SelectST(
    val type: String, var isSelected: Boolean = false
)

data class Asset(
    var selected: Boolean = false, var title: String
)

data class Place(
    val id:String,
    val name:String,
    val label:String,
    var imageUrl: List<String>,
    val latitude: Double,
    val longitude: Double,
    val assetType: String,
    val assetSubType:String,
    val requiredSurveys: String
)

data class PlaceItem(
    val id:String,
    val name: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val assetType: String,
    val requiredSurveys: String
)

data class CreateLarvaSurveillance(
    val larvaSurveillanceId: String,
    val visitDate: String,
    val isLarvaFound: String,
    val breedingSpotName: String,
    val solutionProvided: String,
    val imageUrl: String,
    val nextInspectionDate: String
)

data class HouseHoldDataItem(
    val action: String? = null,
    val uuid: String? = null,
    val houseHoldNumber: String? = null,
    val nameHouseHoldHead: String? = null,
    val memberCount: String? = null,
    var imageUrl: List<String>? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val age: Float? = null,
    val name: String? = null,
    val phoneNumber:String?=null,
    val dob:String?=null,
    val gender:String?=null
)

data class TestResultDataItem(
        val dateOfSampleCollected: String? = null,
        var dateOfSampleSubmitted: String? = null,
        val sampleCount: String? = null,
        val tradeNameOfSalt: String? = null,
        val labResults: String,
        val sampleId:String
)

data class NaturalResourceDataItem(
    val imageDescription: String, var completed: Boolean = false
)

data class IsSurveillanceCreated(
    val totalPages: Int,
    val totalElements: Int,
    val content: MutableList<SurveillanceContent> = emptyList<SurveillanceContent>().toMutableList()
)

data class LarvaVisitModelResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<LarvaVisitModel>
)

data class LarvaVisitModel(
    val breedingSpotName:String?=null,
    val isLarvaFound:String?=null,
    val breedingSite:String?=null,
    val larvaSurveillanceId:String?=null,
    val visitDate:String?=null,
    val solutionProvided:String?=null,
    val imageUrl:String?=null,
    val nextInspectionDate:String?=null

)


data class SurveillanceContent(
    val id: String,
    val dateOfSurveillance: String,
    val villageId: String,
    val placeType: String,
    val placeOrgId: String,
    val placeName: String,
    val householdId: String,
)

data class SurveyResponse(
    val id: String,
    val surveyType: String,
    val quesOptions: List<Options>,
)

data class Options(
    val question: String, val option: List<Option>
)

data class Option(
    val optionName: String, val optionValue: OptionValue
)

data class OptionValue(
    val choices: List<String>
)

data class ResourceCount(
    val menuItem: String?,
    var data:String?,
    var propertyName:String?
)


// water sample surveillance

data class CreateSurveillanceResponse(
    val totalPages: Int, val totalElements: Int, val content: List<Content>
)

data class Content(
    val id: String,
    val dateOfSurveillance: String,
    val villageId: String,
    val placeType: String,
    val placeOrgId: String,
    val placeName: String,
    val householdId: String,
    val testResult: TestResult?,
    val sample: Sample,
    val audit: Audit?
)

data class Sample(
    val noOfSamples: String, val collectionDate: String, val labSubmittedDate: String
)

data class Audit(
    val createdBy: String, val modifiedBy: String, val dateCreated: String
)

data class TestResult(
    val reportImageUrl: String,
    val labTechName: String,
    val h2sResult: String,
    val resultDate: String
)

data class CreateIodineSample(
    val iodineSurveillanceId: String,
    val shopOwnerName: String?=null,
    val saltBrandName: String?=null,
    val noOfSamplesCollected: Int?=null,
    val dateCollected: String?=null,
    val labSubmittedDate: String?=null,
    val labTestResultStatus: String?=null,
    val reportImageUrl: MutableList<String>?=null,
    val iodineResultQty: Int?=null,
    val resultDate: String?=null
)

data class CreateIodineSurveillance(
    val dateOfSurveillance: String,
    val villageId: String,
    val placeType: String,
    val placeOrgId: String,
    val placeName: String,
    val householdId: String,
)

