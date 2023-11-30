package com.ssf.homevisit.models

data class ChildMotherDetailRequest(
    val relationshipType: String,
    val sourceProperties: ChildMotherDetailRequestProperties,
    val sourceType: String,
    val stepCount: Int,
    val targetProperties: ChildMotherDetailRequestProperties,
    val targetType: String
)

data class ChildMotherDetailRequestProperties(
    val uuid: String?
)

data class CreateChildRegRequest(
    val aadharEnrollmentNo: String? = null,
    val aadharNo: String? = null,
    val birthCertificateNo: String? = null,
    val citizenId: String? = null,
    val dataEntryStatus: String? = null,
    val didContactCovidPatient: Boolean? = false,
    val fatherName: String? = null,
    val financialYear: String? = null,
    val immunizationRecommendationId: String? = null,
    val isBornInOtherDistrict: Boolean? = false,
    val isCovidResultPositive: Boolean? = false,
    val isCovidTestDone: Boolean? = false,
    val isILIExperienced: Boolean? = false,
    val mobileNumber: String? = null,
    val mobileOwner: String? = null,
    val placeOfBirth: String? = null,
    val rchId: String? = null,
    val registeredBy: String? = null,
    val registeredByName: String? = null,
    val registeredOn: String? = null,
    val serialNumber: String? = null
)

data class CreateChildRegResponse(
    val aadharEnrollmentNo: String?,
    val aadharNo: String?,
    val audit: ChildRegAudit?,
    val birthCertificateNo: String?,
    val childId: String?,
    val citizenId: String?,
    val dataEntryStatus: String?,
    val didContactCovidPatient: Boolean?,
    val fatherName: String?,
    val financialYear: String?,
    val id: String,
    val immunizationRecommendationId: String?,
    val isBornInOtherDistrict: Boolean?,
    val isCovidResultPositive: Boolean?,
    val isCovidTestDone: Boolean?,
    val isILIExperienced: Boolean?,
    val mobileNumber: String?,
    val mobileOwner: String?,
    val placeOfBirth: String?,
    val registeredBy: String?,
    val registeredByName: String?,
    val registeredOn: String?,
    val serialNumber: Int?
)

data class ChildRegAudit(
    val createdBy: String?,
    val dateCreated: String?,
    val dateModified: String?,
    val modifiedBy: String?
)