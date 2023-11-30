package com.ssf.homevisit.rmncha.adolescentCare

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.hasValueEqualTo
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.hasValueOtherThan
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import com.ssf.homevisit.rmncha.childCare.mapping.ChildCareActivity

class AdolescentCareViewModel(private val handle: SavedStateHandle) : ViewModel() {
    var selectedPhc: MutableLiveData<PhcResponse.Content> = handle.getLiveData("phc")
    var selectedSubCenter: MutableLiveData<SubcentersFromPHCResponse.Content> =
        handle.getLiveData("subCenter")
    var selectedVillage: MutableLiveData<SubCVillResponse.Content> = handle.getLiveData("village")
    val selectedHousehold: MutableLiveData<TargetNodeCCHouseHoldProperties> =
        handle.getLiveData(ChildCareActivity.household)
    var listModeAll = MutableLiveData(false)
    val selectedChild: MutableLiveData<ChildInHouseContent> =
        handle.getLiveData(AdolescentCareServiceActivity.childDetail)

    private val villageLevelMappingRepository = VillageLevelMappingRepository()
    val isLoading = MutableLiveData(false)
    val whoseMobile = MutableLiveData("")
    val covidTested = MutableLiveData("")
    val childAadhaar = MutableLiveData("")
    val covidTestResult = MutableLiveData("")
    val hasILI = MutableLiveData("")
    val hadContactWithCovid = MutableLiveData("")
    val birthCertNo = MutableLiveData("")
    val selectedAsha = MutableLiveData("")
    val isValidData = MutableLiveData(false)

    fun setLoading(loading: Boolean) {
        isLoading.value = loading
    }

    val ashaWorkers = selectedSubCenter.switchMap {
        villageLevelMappingRepository.getAshaWorkerLiveData(it.target.properties.uuid)
    }

    val allHHList = selectedVillage.switchMap {
        setLoading(true)
        val request = CCHouseHoldRequest(
            relationshipType = "CONTAINEDINPLACE",
            sourceType = "Village",
            sourceProperties = SourceProperties(it?.target?.villageProperties?.uuid ?: ""),
            targetProperties = TargetProperties(hasEligibleChildCare = true),
            targetType = "HouseHold",
            stepCount = 1
        )
        villageLevelMappingRepository.getChildCareHouseHoldInVillage(request)
    }
    val allChildren = selectedVillage.switchMap {
        setLoading(true)
        val request = CcChildListRequest(
            relationshipType = "RESIDESIN",
            sourceType = "Citizen",
            sourceProperties = CcChildListSourceProperties(isAdult = false),
            targetProperties = CcChildListTargetProperties(
                uuid = it?.target?.villageProperties?.uuid ?: ""
            ),
            targetType = "Village",
            stepCount = 2,
            srcInClause = SrcInClause(
                rmnchaServiceStatus = listOf(
                    "CHILDCARE_ONGOING",
                    "ADOLESCENTCARE_REGISTERED"
                )
            )
        )
        villageLevelMappingRepository.getChildCareChildrenInVillage(request)
    }

    val childInHouseHold = selectedHousehold.switchMap {
        setLoading(true)
        val request = ChildInHouseholdRequest(
            relationshipType = "RESIDESIN",
            sourceProperties = ChildInHouseholdRequestSourceProperties(isAdult = false),
            sourceType = "Citizen",
            srcInClause = ChildInHouseholdRequestSrcInClause(
                rmnchaServiceStatus = listOf(
                    "CHILDCARE_ONGOING",
                    "ADOLESCENTCARE_REGISTERED"
                )
            ),
            stepCount = 1,
            targetProperties = ChildInHouseholdRequestTargetProperties(uuid = it.uuid),
            targetType = "HouseHold"
        )
        villageLevelMappingRepository.getChildCareChildrenInVillage(request)
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

    val coupleDetail = selectedChild.switchMap {
        val request = ChildMotherDetailRequest(
            relationshipType = "MARRIEDTO",
            sourceProperties = ChildMotherDetailRequestProperties(uuid = childMotherDetail.value?.content?.get(0)?.sourceNode?.properties?.uuid),
            sourceType = "Citizen",
            stepCount = 1,
            targetProperties = ChildMotherDetailRequestProperties(uuid = null),
            targetType = "Citizen"
        )
        villageLevelMappingRepository.getCoupleDetailByWifeID(request)
    }

    fun registerAdolescent(): MutableLiveData<RegisterAdolescentCareResponse> {
        val request = AdolescentCareRegRequest(
            citizenId = selectedChild.value?.sourceNode?.properties?.uuid!!,
            birthCertificateNo = birthCertNo.value ?: "",
            didContactCovidPatient = hadContactWithCovid.value?.hasValueEqualTo("done") ?: false,
                fatherName = "",
////            fatherName = coupleDetail.value?.content?.get(0)?.targetNode?.properties?.firstName
//                ?: "",
            isCovidTestDone = covidTested.value?.hasValueEqualTo("done") ?: false,
            isCovidResultPositive = covidTestResult.value?.hasValueEqualTo("done") ?: false,
            financialYear = RMNCHAUtils.getCurrentFinancialYear().substringBefore(" "),
            isILIExperienced = hasILI.value?.hasValueEqualTo("done") ?: false,
            mobileOwner = whoseMobile.value ?: "",
            rchId = childMotherDetail.value?.content?.get(0)?.targetNode?.properties?.rchId ?: "",
            childAadharNo = childAadhaar.value ?: "",
            registeredOn = RMNCHAUtils.getCurrentDate() + "Z",
                mobileNumber = ""
        )

//            mobileNumber = coupleDetail.value?.content?.get(0)?.targetNode?.properties?.contact
//                ?: "",
        return villageLevelMappingRepository.registerAdolescentCare(request)
    }

    fun checkValidation() {
        isValidData.value =
            whoseMobile.value?.hasValueOtherThan() ?: false && hasILI.value?.hasValueOtherThan() ?: false && hadContactWithCovid.value?.hasValueOtherThan() ?: false
    }
}