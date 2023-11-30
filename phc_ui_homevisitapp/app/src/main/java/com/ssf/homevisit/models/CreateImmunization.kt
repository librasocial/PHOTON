package com.ssf.homevisit.models

data class CreateImmunization(
    val ashaWorker: String,
    val audit: Audit? = null,
    val childCitizenId: String,
    val description: String,
    val didContactCovidPatient: Boolean,
    val doseNumber: String,
    val isCovidResultPositive: Boolean,
    val isCovidTestDone: Boolean,
    val isILIExperienced: Boolean,
    val reaction: Reaction,
    val registrationDate: String,
    val series: String,
    val targetDiseaseName: String,
    val vaccines: List<Vaccine>
)

data class Reaction(
    val caseClosureReason: String,
    val date: String,
    val death: Death,
    val detail: String,
    val isCaseClosed: Boolean,
    val type: String
)

data class Vaccine(
    val code: String,
    val earliestDueDate: String,
    val immunization: Immunization,
    val name: String,
    val pendingReason: String
)

data class Death(
    val cause: String,
    val date: String,
    val place: String
)

data class Immunization(
    val batchNumber: String,
    val expirationDate: String,
    val manufacturer: String,
    val vaccinatedDate: String
)