package com.ssf.homevisit.models

data class RegisterAdolescentCareResponse(
    val birthCertificateNo: String,
    val childAadharNo: String,
    val citizenId: String,
    val didContactCovidPatient: Boolean,
    val fatherName: String,
    val financialYear: Int,
    val id: String,
    val isCovidResultPositive: Boolean,
    val isCovidTestDone: Boolean,
    val isILIExperienced: Boolean,
    val mobileNumber: String,
    val mobileOwner: String,
    val rchId: String,
    val registeredOn: String
)