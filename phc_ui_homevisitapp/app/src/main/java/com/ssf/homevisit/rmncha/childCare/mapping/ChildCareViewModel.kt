package com.ssf.homevisit.rmncha.childCare.mapping

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChildCareViewModel(private val handle: SavedStateHandle) : ViewModel() {
    var selectedPhc: MutableLiveData<PhcResponse.Content> = handle.getLiveData("phc")
    var selectedSubCenter: MutableLiveData<SubcentersFromPHCResponse.Content> =
        handle.getLiveData("subCenter")
    var selectedVillage: MutableLiveData<SubCVillResponse.Content> = handle.getLiveData("village")
    val selectedHousehold: MutableLiveData<TargetNodeCCHouseHoldProperties> =
        handle.getLiveData(ChildCareActivity.household)
    var listModeAll = MutableLiveData(false)
    var houseHoldChildrenLiveData: MutableLiveData<CCHouseHoldResponse> = MutableLiveData()
    var registeredChildrenLiveData: MutableLiveData<CcChildListResponse> = MutableLiveData()
    var childInHouseHoldLiveData: MutableLiveData<ChildInHouseHoldResponse> = MutableLiveData()
    var childMotherDetailsResponse: MutableLiveData<ChildMotherDetailResponse> = MutableLiveData()
    private val villageLevelMappingRepository = VillageLevelMappingRepository()
    val isLoading = MutableLiveData(false)

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    fun getChildCareCompleteChildrenList(){
        setLoading(true)
        val request = CCHouseHoldRequest(
                relationshipType = "CONTAINEDINPLACE",
                sourceType = "Village",
                sourceProperties = SourceProperties(selectedVillage.value?.target?.villageProperties?.uuid ?: ""),
                targetProperties = TargetProperties(hasEligibleChildCare = true),
                targetType = "HouseHold",
                stepCount = 1
        )
        viewModelScope.launch {
            val response =villageLevelMappingRepository.getChildCareHouseHoldInVillage(request)
            delay(1000)
            if(response.value != null){
                houseHoldChildrenLiveData.value=response.value
            }
        }
    }
    fun getRegisteredChildrenList(){
        setLoading(true)
        val request = CcChildListRequest(
                relationshipType = "RESIDESIN",
                sourceType = "Citizen",
                sourceProperties = CcChildListSourceProperties(isAdult = false),
                targetProperties = CcChildListTargetProperties(
                        uuid = selectedVillage.value?.target?.villageProperties?.uuid ?: ""
                ),
                targetType = "Village",
                stepCount = 2,
                srcInClause = SrcInClause(
                        rmnchaServiceStatus = listOf(
                                "PNC_ONGOING",
                                "CHILDCARE_REGISTERED",
                                "CHILDCARE_ONGOING"
                        )
                )
        )
        viewModelScope.launch {
            val response=villageLevelMappingRepository.getChildCareChildrenInVillage(request)
            delay(1000)
            if(response.value != null){
                registeredChildrenLiveData.postValue(response.value)
            }
        }
    }

    fun getChildInHouseHold(){
        setLoading(true)
        val request = selectedHousehold.value?.let { ChildInHouseholdRequestTargetProperties(uuid = it.uuid) }?.let {
            ChildInHouseholdRequest(
                relationshipType = "RESIDESIN",
                sourceProperties = ChildInHouseholdRequestSourceProperties(isAdult = false),
                sourceType = "Citizen",
                srcInClause = ChildInHouseholdRequestSrcInClause(rmnchaServiceStatus = listOf("PNC_ONGOING", "CHILDCARE_REGISTERED","CHILDCARE_ONGOING")),
                stepCount = 1,
                targetProperties = it,
                targetType = "HouseHold"
        )
        }
        viewModelScope.launch {
            val response = villageLevelMappingRepository.getChildCareChildrenInVillage(request)
            delay(1000)
            if (response != null) {
                childInHouseHoldLiveData.value = response.value
            }
        }

    }

    fun getChildMotherDetail(uuid: MutableList<String>) {
        val request = CcChildMotherDetailRequest(
            relationshipType = "MOTHEROF",
            sourceType = "Citizen",
            stepCount = 1,
            targetType = "Citizen",
            srcInClause = SrcinClauseMotherDetails(uuid = uuid)
        )
        viewModelScope.launch {
            val response =
                villageLevelMappingRepository.getChildMotherDetailsByChildUUIDList(request)
            delay(2000)
            if (response.value?.content?.isNotEmpty() == true) {
                childMotherDetailsResponse.value=response.value
            }
        }
    }
}