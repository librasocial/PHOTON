package com.ssf.homevisit.models

data class PncServiceData(
    val birthWeight: Int
)


data class PncRegistrationData(
    val deliveryDetails: DeliveryDetails
)

data class DeliveryDetails(
    val place: String
)