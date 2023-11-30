package com.ssf.homevisit.rmncha.childCare.immunization


data class Immunization(
    val doseNumber: String, val immunizationSubGroup: ImmunizationSubGroup
)

data class ImmunizationSubGroup(
     val name: String? = null,
     val batchNumber: String? = null,
     val manufacturer: String? = null,
     val expirationDate: String? = null,
     val ashaWorker: String? = null,
     val vaccinatedDate: String? = null,
     val earliestDueDate: String? = null,
     val pendingReason: String? = null,
     val aefiStatus: String? = null,
     val aefiReason: String? = null,
     val isCovidResultPositive: Boolean? = null
)


data class VaccinationStatus(
     var year:String,
     var status: Boolean
)