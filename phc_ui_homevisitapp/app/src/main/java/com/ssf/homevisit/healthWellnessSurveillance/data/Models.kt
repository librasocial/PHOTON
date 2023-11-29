package com.ssf.homevisit.healthWellnessSurveillance.data

import com.google.gson.annotations.SerializedName


data class PlaceResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<Content>
)

data class Content(
    @SerializedName("source") val source: Source,
    @SerializedName("target") val target: Target
)

data class Source(
    @SerializedName("id") val id: Int,
    @SerializedName("labels") val labels: List<String>,
    @SerializedName("properties") val properties: SourceProperty
)

data class Target(
    @SerializedName("id") val id: Int,
    @SerializedName("labels") val labels: List<String>,
    @SerializedName("properties") val properties: TargetProperty
)

data class TargetProperty(
    @SerializedName("imgUrl") val imgUrl: List<String>,
    @SerializedName("assetSubType") val assetSubType: String,
    @SerializedName("requiredSurveys") val requiredSurveys: String,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("name") val name: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("assetType") val assetType: String
)

data class SourceProperty(
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("lgdCode") val lgdCode: String,
    @SerializedName("totalPopulation") val totalPopulation: Int,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("name") val name: String,
    @SerializedName("dateModified") val dateModified: String,
    @SerializedName("modifiedBy") val modifiedBy: String,
    @SerializedName("houseHoldCount") val houseHoldCount: String,
    @SerializedName("uuid") val uuid: String
)

data class SurveyFormData(
    @SerializedName("data") val data: List<SurveyFormResponse>
)

data class SurveyFormResponse(
    @SerializedName("_id") val id: String,
    @SerializedName("surveyType") val surveyType: String,
    @SerializedName("surveyName") val surveyName: String,
    @SerializedName("quesOptions") val quesOptions: List<Options>

)

data class Options(
    @SerializedName("question") val question: String,
    @SerializedName("choices") val choices: List<String>,
    @SerializedName("propertyName") val propertyName: String,
    @SerializedName("checked") var checked:Boolean=false
)


data class OptionsData(
    val question: String? = null,
    val choices: List<String>? = null,
    val propertyName: String? = null,
    var isSelected: Boolean = false,
    var value: Boolean = false
)


data class SaveSurvey(
    val audit: Audit?=null,
    val conductedBy: String,
    val context: Context,
    val quesResponse: List<QuesResponse>,
    val respondedBy: String,
    val surveyId: String,
    val surveyName: String
)

data class Audit(
    val createdBy: String, val dateCreated: String, val dateModified: String, val modifiedBy: String
)

data class Context(
        val hId: String, val villageId: String, val memberId: String
)

data class QuesResponse(
    val propertyName: String?, val question: String?, var response: MutableList<String>?
)

data class CheckIfSurveillanceIsCreated(
    val householdId: String?=null,
    val dateOfSurveillance: String?=null,
    val placeName: String?=null,
    val placeOrgId: String?=null,
    val placeType: String?=null,
    val villageId: String?=null
)

data class SaveSurveillance(
    val breedingSpotName: String? = "",
    val imageUrl:String?="",
    val isLarvaFound: String? = "",
    val larvaSurveillanceId: String = "",
    val nextInspectionDate: String? = "",
    val solutionProvided: String? = "",
    val visitDate: String? = ""
)

data class PresignedUrl(
    @SerializedName("preSignedUrl") val preSignedUrl: String
)

// water sample models
data class WaterSampleResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<WaterSampleContent>
)

data class WaterSampleContent(
    val id: String?=null,
    val dateOfSurveillance: String?=null,
    val villageId: String,
    val placeType: String,
    val placeOrgId: String,
    val placeName: String,
    val householdId: String?=null,
    val testResult: TestResult?=null,
    val sample: WaterSample?=null,
    val audit: Audit?=null
)

data class WaterSample(
    val noOfSamples: String, val collectionDate: String, val labSubmittedDate: String
)

data class TestResult(
    val reportImageUrl: MutableList<String>,
    val labTechName: String,
    val h2sResult: String,
    val resultDate: String
)

//iodine sample data


data class IodineSampleData(
    val iodineSurveillanceId: String,
    val shopOwnerName: String,
    val saltBrandName: String,
    val noOfSamplesCollected: String,
    val dateCollected: String,
    val labSubmittedDate: String,
    val labTestResultStatus: String,
    val reportImageUrl: String,
    val iodineResultQty: String,
    val resultDate: String
)

data class IodineSampleResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<IodineSampleContent>
)

data class IodineSampleContent(
    val iodineSurveillanceId: String,
    val shopOwnerName: String,
    val saltBrandName: String,
    val noOfSamplesCollected: Int,
    val dateCollected: String,
    val labSubmittedDate: String,
    val labTestResultStatus: String,
    val reportImageUrl: List<String>,
    val iodineResultQty: Int,
    val resultDate: String,
    val id:String
)
data class HouseHoldResponseByName(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<HouseHoldTarget>
)

data class NearByHouseHoldResponse(
        @SerializedName("totalPages") val totalPages: Int,
        @SerializedName("totalElements") val totalElements: Int,
        @SerializedName("content") val contents: List<HouseHoldProperties>

)

data class HouseHoldProperties(
        val uuid: String? = null,
        val houseID: String? = null,
        val latitude: String? = null,
        val longitude: String? = null,
        val houseHeadName: String? = null,
        val totalFamilyMembers: Int? = null,
        val dateCreated: String? = null,
        val createdBy: String? = null,
        val dateModified: String? = null,
        val modifiedBy: String? = null,
        val village: String? = null,
        val houseHeadImageUrls: List<String>)

data class HouseHoldResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<HouseHoldContent>
)

data class HouseHoldContent(
    @SerializedName("source") val source: Source,
    @SerializedName("target") val target: HouseHoldTarget
)

data class HouseHoldTarget(
    @SerializedName("id") val id: Int,
    @SerializedName("labels") val labels: List<String>,
    @SerializedName("properties") val properties: HouseHoldTargetProperty
)

data class HouseHoldTargetProperty(
    val houseID: String,
    val houseHeadName: String?=null,
    val totalFamilyMembers: Int,
    val houseHeadImageUrls: List<String>?,
    val latitude: Double,
    val longitude: Double,
    val uuid: String?

)

data class ChildrenResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<ChildContent>
)

data class ChildContent(
    @SerializedName("sourceNode") val source: ChildSource,
)

data class ChildSource(
    @SerializedName("id") val id: Int,
    @SerializedName("labels") val labels: List<String>,
    @SerializedName("properties") val properties: ChildSourceProperty
)

data class ChildSourceProperty(
    val age: Float? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null,
    val firstName: String? = null,
    val uuid: String? = null,
    val contact: String? = null,
    val imageUrls: List<String>? = null,
)


data class IndividualResponse(
    @SerializedName("totalPages") val totalPages: Int,
    @SerializedName("totalElements") val totalElements: Int,
    @SerializedName("content") val contents: List<IndividualContent>
)

data class IndividualContent(
    @SerializedName("sourceNode") val source: Source,
    @SerializedName("targetNode") val target: IndividualTarget?
)

data class IndividualTarget(
    @SerializedName("id") val id: Int,
    @SerializedName("labels") val labels: List<String>?,
    @SerializedName("properties") val properties: IndividualDetailProperty?
)

data class IndividualDetailProperty(
    val uuid: String?,
    val firstName: String?,
    val dateOfBirth: String?,
    val gender: String?,
    val age: Float?,
    val contact: String?,
    val healthID: String?,
    var labStatusDate:String?,
    var imageUrls: List<String>?,
    var labStatus:String?=null
)


data class HouseHoldByNameRequest(
    val type: String, val page: Int, val size: Int, val properties: Property
)

data class ChildrenRequestData(
    val relationshipType: String? = null,
    val sourceType: String? = null,
    val sourceProperties: ChildrenSourceProperties?,
    val targetProperties: ChildrenTargetProperties?,
    val targetType: String?,
    val stepCount: Int
)

data class ChildrenTargetProperties(
    val uuid: String
)

data class ChildrenSourceProperties(
    val isAdult: Boolean
)

data class Property(
    val villageId: String,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val distance: Double? = null
)

data class PlaceRequest(
    val relationshipType: String,
    val sourceType: String,
    val sourceProperties: PlaceSourceProperties,
    val targetProperties: TargetProperties,
    val targetType: String,
    val stepCount: Int
)

data class PlaceSourceProperties(
    val uuid: String
)

data class TargetProperties(
    val assetType: String
)

data class CovidRequestData(
    val citizenId: String,
    val dateOfSurveillance: String? = null,
    val vaccineType: String?,
    val noOfDoses: String?,
    val wasPreviouslyDiagnosed: String?,
)

data class GetLeprosyFilterData(
    @SerializedName("data") val data: List<LeprosyRequestData>
)

data class GetUrineFilterData(
    @SerializedName("data") val data: List<UrineRequestData>
)

data class GetAcfFilterData(
    @SerializedName("data") val data: List<AcfRequestData>
)

data class GetBloodFilterData(
    @SerializedName("data") val data: List<BloodSmearRequestData>
)

data class LeprosyRequestData(
    val citizenId: String,
    val id: String? = null,
    val dateOfSurveillance: String? = null,
    val isNewlySuspected: String? = null,
    val suspectedType: String? = null,
    val symptoms: List<String>? = null,
    val isReferredToPhc: String? = null,
    val labResult: LeprosyLabResult? = null,
    val sample: LeprosySample? = null,
    val past: Past? = null
)

data class Past(
    val wasConfirmed: String? = null,
    val result: String? = null,
    val hasUndergoneRCSSurgery: String? = null
)

data class LeprosySample(
    val isSampleCollected: String? = null,
    val sampleCollectedDate: String? = null,
    val labReceivedDate: String? = null
)

data class LeprosyLabResult(
    val resultDate: String? = null, val result: String? = null, val reportImages: List<String>?
)

data class UrineRequestData(
    val citizenId: String,
    val id: String? = null,
    val dateOfSurveillance: String? = null,
    val fatherName: String? = null,
    val schoolAttending: String? = null,
    val sample: UrineSample? = null,
    val labResult: UrineLabResult? = null
)


data class UrineLabResult(
    val resultDate: String?,
    val isDentalFlurosisFound: String?,
    val result: String?,
    val reportImages: List<String>?
)

data class UrineSample(
    val isSampleCollected: String?,
    val noOfSampleCollected: Int?,
    val sampleSubmittedDate: String?,
    val labReceivedDate: String?
)

data class BloodSmearUpdateRequestData(
    val type: String,
    val properties: BloodSmearUpdateLabResult
)

data class BloodSmearUpdateLabResult(
    val labResult: BloodSmearLabResult? = null
)

data class BloodSmearRequestData(
    val citizenId: String,
    val id: String? = null,
    val dateOfSurveillance: String? = null,
    val permanentAddress: String? = null,
    val type: String? = null,
    val isReferredToPhc: String? = null,
    val sample: BloodSmearSample? = null
)

data class BloodSmearSample(
    val slideNo: String? = null,
    val isSampleCollected: String? = null,
    val sampleCollectedDate: String? = null,
    val feverStartDuration: String? = null,
    val numberOf4AQProvided: String? = null,
    val sampleSubmittedDate: String? = null,
    val sampleType: String? = null,
    val labReceivedDate: String? = null,
    val rdtResult: String? = null,
    val labResult: BloodSmearLabResult? = null
)

data class BloodSmearLabResult(
    val resultDate: String? = null,
    val technicianName: String? = null,
    val result: String? = null,
    val reportImages: List<String>? = null
)

data class AcfRequestData(
    val citizenId: String,
    val id: String? = null,
    val dateOfSurveillance: String? = null,
    val hasTBSymptoms: String? = null,
    val hasDiabetes: String? = null,
    val tbSymptom:String?=null,
    val isReferredToPhc: String? = null,
    val wasTreatedForTBInPast: String? = null,
    val sample: List<AcfSample>? = null,
    val labResult: LabResult? = null
)

data class LabResult(
    val resultDate: String? = null,
    val dmcTestResult: String? = null,
    val naatTestResult: String? = null,
    val chestXRayTestResult: String? = null,
    val nameOfTechnician: String? = null,
    val labSerialNo: String? = null,
    val result: String? = null,
    val reportImages: List<String>? = null
)

data class AcfSample(
    val isSampleCollected: String? = null,
    val sampleCollectedDate: String? = null,
    val sampleSubmittedDate: String? = null,
    val sampleSentToLoc: String? = null,
    val labReceivedDate: String? = null,
    val remarks: String? = null
)

data class IdspData(
    val citizenId: String,
    val dateOfSurveillance: String?,
    val symptomType: String?,
    val fever: FeverRequestBody?,
    val cough: CoughRequestBody?,
    val looseStools: LooseStoolsRequestBody?,
    val jaundice: JaundiceRequestBody?,
    val animalBite: AnimalBiteRequestBody?,
    val dateOfDeath: String?,
    val isReferredToPhc: String?,
    val others: OtherRequestBody?
)

data class OtherRequestBody(
    val otherSymptom: String?, val additionalSymptoms: String?
)

data class LooseStoolsRequestBody(
    val suspectedPeriod: String?,
    val hasDehydration: String?,
    val extendOfDehydration: String?,
    val isBloodInStool: String?

)
data class SaveIndividualDataResponse(
    val citizenId: String? = null,
    val dateOfSurveillance: String? = null, @SerializedName("id") val surveillanceId: String? = null
)

data class CoughRequestBody(
    val suspectedPeriod: String?
)

data class JaundiceRequestBody(
    val suspectedPeriod: String?
)

data class AnimalBiteRequestBody(
    val typeOfBite: String?
)

data class FeverRequestBody(
    val suspectedPeriod: String?, val additionalSymptoms: String?
)


data class Response(
    val name: String, val formItems: List<Form>
)

data class Form(
    val groupName: String, val elements: List<Element>

)

data class Element(
    val title: String, val sequence: Int
)


data class LabResultStatusResponse(
    val result:String?=null,
    val dateModified:String?=null,
    val citizenId:String?=null
)