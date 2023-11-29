package com.ssf.homevisit.rmncha.childCare.registration

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.hasValueEqualTo
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.hasValueOtherThan
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import com.ssf.homevisit.rmncha.base.RMNCHAUtils

class ChildCareRegistrationViewModel(private val handle: SavedStateHandle) : ViewModel() {
    var selectedPhc: MutableLiveData<PhcResponse.Content> = handle.getLiveData("phc")
    var selectedSubCenter: MutableLiveData<SubcentersFromPHCResponse.Content> = handle.getLiveData("subCenter")
    var selectedVillage: MutableLiveData<SubCVillResponse.Content> = handle.getLiveData("village")
    val selectedHousehold: MutableLiveData<TargetNodeCCHouseHoldProperties> = handle.getLiveData(ChildCareRegistrationActivity.household)
    val selectedChild: MutableLiveData<ChildInHouseContent> = handle.getLiveData(ChildCareRegistrationActivity.childDetail)
    private val villageLevelMappingRepository = VillageLevelMappingRepository()
    val isLoading = MutableLiveData(false)
    val whoseMobile = MutableLiveData("")
    val covidTested = MutableLiveData("")
    val placeOfBirth = MutableLiveData("")
    val aadharEnroll = MutableLiveData("")
    val childAadhaar = MutableLiveData("")
    val covidTestResult = MutableLiveData("")
    val hasILI = MutableLiveData("")
    val hadContactWithCovid = MutableLiveData("")
    val birthCertNo = MutableLiveData("")
    val siNumberInRCH = MutableLiveData("")
    val selectedAsha = MutableLiveData("")
    val isValidData = MutableLiveData(false)
    val bornInOtherCity = MutableLiveData(false)

    val ashaWorkers = selectedSubCenter.switchMap {
        villageLevelMappingRepository.getAshaWorkerLiveData(it.target.properties.uuid)
    }
    val childMotherDetail = selectedChild.switchMap {
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

    val coupleDetail = selectedChild.switchMap {
        val request = ChildMotherDetailRequest(
            relationshipType = "MARRIEDTO",
            sourceProperties = ChildMotherDetailRequestProperties(uuid = childMotherDetail.value?.content?.get(0)?.targetNode?.properties?.uuid),
            sourceType = "Citizen",
            stepCount = 1,
            targetProperties = ChildMotherDetailRequestProperties(uuid = null),
            targetType = "Citizen"
        )
        villageLevelMappingRepository.getCoupleDetailByWifeID(request)
    }

    fun createChild(): MutableLiveData<CreateChildRegResponse> {
        val request = CreateChildRegRequest(
            citizenId = selectedChild.value?.sourceNode?.properties?.uuid,
            aadharEnrollmentNo = aadharEnroll.value,
            aadharNo = childAadhaar.value,
            birthCertificateNo = birthCertNo.value,
            didContactCovidPatient = hadContactWithCovid.value?.hasValueEqualTo("done") ?: false,
            fatherName = coupleDetail.value?.content?.get(0)?.targetNode?.properties?.firstName,
            isCovidTestDone = covidTested.value?.hasValueEqualTo("done") ?: false,
            isCovidResultPositive = covidTestResult.value?.hasValueEqualTo("done") ?: false,
            isBornInOtherDistrict = bornInOtherCity.value,
            financialYear = RMNCHAUtils.getCurrentFinancialYear(),
            isILIExperienced = hasILI.value?.hasValueEqualTo("done") ?: false,
            mobileNumber = coupleDetail.value?.content?.get(0)?.targetNode?.properties?.contact,
            mobileOwner = whoseMobile.value,
            placeOfBirth = placeOfBirth.value,
            serialNumber = siNumberInRCH.value,
            rchId = siNumberInRCH.value
        )
        return villageLevelMappingRepository.createChildReg(request)
    }

    fun checkValidation() {
        isValidData.value =
            whoseMobile.value?.hasValueOtherThan() ?: false && hasILI.value?.hasValueOtherThan() ?: false && hadContactWithCovid.value?.hasValueOtherThan() ?: false
    }

    fun getPncRegistrationData(): MutableLiveData<PncRegistrationData>? {
        val pncRegistrationId =
            childMotherDetail.value?.content?.get(0)?.relationship?.properties?.pncInfantRegistrationId
        if (pncRegistrationId != null) {
            return villageLevelMappingRepository.getPncRegistrationData(
                pncRegistrationId,
            )
        }
        return null
    }
}