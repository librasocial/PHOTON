package com.ssf.homevisit.rmncha.adolescentCare

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import com.ssf.homevisit.rmncha.base.RMNCHAUtils

class AdolescentCareServiceViewModel(private val handle: SavedStateHandle) : ViewModel() {
    var selectedPhc: MutableLiveData<AllPhcResponse.Content> = handle.getLiveData("phc")
    var selectedSubCenter: MutableLiveData<SubcentersFromPHCResponse.Content> =
        handle.getLiveData("subCenter")
    var selectedVillage: MutableLiveData<SubCVillResponse.Content> = handle.getLiveData("village")
    val selectedChild: MutableLiveData<CcChildListContent> =
        handle.getLiveData(AdolescentCareServiceActivity.childDetail)

    private val villageLevelMappingRepository = VillageLevelMappingRepository()
    val isLoading = MutableLiveData(false)
    val selectedAsha = MutableLiveData("")
    val isValidData = MutableLiveData(false)
    var isReferralUpToPHC = false;

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    val ashaWorkers = selectedSubCenter.switchMap {
        villageLevelMappingRepository.getAshaWorkerLiveData(it.target.properties.uuid)
    }

    val childMotherDetail = selectedChild.switchMap {
        it?.let {
            val request = ChildMotherDetailRequest(
                relationshipType = "MOTHEROF",
                sourceProperties = ChildMotherDetailRequestProperties(uuid = it.sourceNode.properties.uuid),
                sourceType = "Citizen",
                stepCount = 1,
                targetProperties = ChildMotherDetailRequestProperties(uuid = null),
                targetType = "Citizen"
            )
            villageLevelMappingRepository.getChildMotherDetail(request)
        }
    }

//    val coupleDetail = selectedChild.switchMap {
//        val request = ChildMotherDetailRequest(
//            relationshipType = "MARRIEDTO",
//            sourceProperties = ChildMotherDetailRequestProperties(uuid = /*it.sourceNode.properties.uuid*/"4f285dca-2a6a-4546-a433-25c67fb32494"),
//            sourceType = "Citizen",
//            stepCount = 1,
//            targetProperties = ChildMotherDetailRequestProperties(uuid = null),
//            targetType = "Citizen"
//        )
//        villageLevelMappingRepository.getCoupleDetailByWifeID(request)
//    }

    fun createAdCareService(): MutableLiveData<AddAdCareServiceRequest> {
        val request = AddAdCareServiceRequest(
            childCitizenId = selectedChild.value?.sourceNode?.properties?.uuid ?: "",
            commoditiesProvided = commoditiesData.map { ServiceData(it.key, it.value) },
            councelingData = followUpData.map { ServiceData(it.key, it.value) },
            informationCollected = informationData.map { ServiceData(it.key, it.value) },
            isReferredToPhc = isReferralUpToPHC,
            visitDate = RMNCHAUtils.getCurrentDate() + "Z"
        )
        return villageLevelMappingRepository.createAdCareService(request)
    }

    fun clearData() {
        commoditiesData = HashMap(data1)
        informationData = HashMap(data2)
        followUpData = HashMap(data3)
    }

    companion object {
        private val data1 = hashMapOf<String, Any>(
            AdCareServiceTopics.COMMODITIES_Q1 to false,
            AdCareServiceTopics.COMMODITIES_Q2 to false,
            AdCareServiceTopics.COMMODITIES_Q3 to false,
            AdCareServiceTopics.COMMODITIES_Q4 to false,
            AdCareServiceTopics.COMMODITIES_Q5 to "",
            AdCareServiceTopics.COMMODITIES_Q6 to "",
        )

        private val data2 = hashMapOf(
            AdCareServiceTopics.INFORMATION_Q1 to false,
            AdCareServiceTopics.INFORMATION_Q2 to false,
            AdCareServiceTopics.INFORMATION_Q3 to false,
            AdCareServiceTopics.INFORMATION_Q4 to false,
            AdCareServiceTopics.INFORMATION_Q5 to false,
            AdCareServiceTopics.INFORMATION_Q6 to false,
            AdCareServiceTopics.INFORMATION_Q7 to false,
            AdCareServiceTopics.INFORMATION_Q8 to false
        )

        private val data3 = hashMapOf(
            AdCareServiceTopics.FOLLOW_UPS_Q1 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q2 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q3 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q4 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q5 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q6 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q7 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q8 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q9 to false,
            AdCareServiceTopics.FOLLOW_UPS_Q10 to false
        )

        var commoditiesData = HashMap(data1)

        var informationData = HashMap(data2)

        var followUpData = HashMap(data3)
    }
}
