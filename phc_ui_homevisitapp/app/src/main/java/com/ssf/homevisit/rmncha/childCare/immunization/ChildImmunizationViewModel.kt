package com.ssf.homevisit.rmncha.childCare.immunization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.hasValueEqualTo
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import com.ssf.homevisit.rmncha.base.RMNCHAUtils
import com.ssf.homevisit.rmncha.base.RMNCHAUtils.RMNCHA_DATE_FORMAT
import com.ssf.homevisit.rmncha.childCare.registration.ChildCareRegistrationActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChildImmunizationViewModel(private val handle: SavedStateHandle) : ViewModel() {
    val selectedPhc: MutableLiveData<PhcResponse.Content> =
        handle.getLiveData(ChildImmunizationActivity.keyVhc)
    private val selectedSubCenter: MutableLiveData<SubcentersFromPHCResponse.Content> =
        handle.getLiveData(ChildImmunizationActivity.keySubCenter)
    val selectedVillage: MutableLiveData<SubCVillResponse.Content> =
        handle.getLiveData(ChildImmunizationActivity.keyVillage)
    val selectedChild: MutableLiveData<CcChildListContent> =
        handle.getLiveData(ChildImmunizationActivity.childDetail)
    val selectedAsha = MutableLiveData("")

    private val villageLevelMappingRepository = VillageLevelMappingRepository()
    val isLoading = MutableLiveData(false)
    val OPV1DoseDate = MutableLiveData("")
    val pantavalentDoseDate = MutableLiveData("")
    val rota1DoseDate = MutableLiveData("")
    val pCV1DoseDate = MutableLiveData("")
    val iCV1DoseDate = MutableLiveData("")
    val vaccinationStatus = MutableLiveData("")
    val vaccinationNames = MutableLiveData(listOf<VaccineItemData>())
    val isCaseClosure = MutableLiveData(false)
    val vaccineNotDoneReason = MutableLiveData("")
    val caseClosureReason = MutableLiveData("")
    val deathDate = MutableLiveData("")
    val deathPlace = MutableLiveData("")
    val deathCause = MutableLiveData("")
    val isCovidTestDone = MutableLiveData("")
    val covidResult = MutableLiveData("")
    val feelingLikeILI = MutableLiveData("")
    val covidContact = MutableLiveData("")
    val aefiStatus = MutableLiveData("")
    val vaccineName = MutableLiveData("")
    val remarks = MutableLiveData("")

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

    fun setOPV1DoseDate(d: Int, m: Int, y: Int) {
        OPV1DoseDate.value = formatDate(d, m, y)
    }

    fun setPanavalentDoseDate(d: Int, m: Int, y: Int) {
        pantavalentDoseDate.value = formatDate(d, m, y)
    }

    fun setRota1DoseDate(d: Int, m: Int, y: Int) {
        rota1DoseDate.value = formatDate(d, m, y)
    }

    fun setPCV1DoseDate(d: Int, m: Int, y: Int) {
        pCV1DoseDate.value = formatDate(d, m, y)
    }

    fun setICV1DoseDate(d: Int, m: Int, y: Int) {
        iCV1DoseDate.value = formatDate(d, m, y)
    }

    fun setIsCaseClosure(isClosure: Boolean) {
        isCaseClosure.value = isClosure
    }

    fun setCaseClosureReason(reason: String) {
        caseClosureReason.value = reason
    }

    fun setDateOfDeath(d: Int, m: Int, y: Int) {
        deathDate.value = formatDate(d, m, y)
    }

    fun setCovidTestDone(value: String) {
        isCovidTestDone.value = value
    }

    fun setVaccineNotDoneReason(reason: String) {
        vaccineNotDoneReason.value = reason
    }

    fun setAEFIStatus(status: String) {
        aefiStatus.value = status
    }

    fun setVaccineName(name: String) {
        vaccineName.value = name
    }

    fun setDeathPlace(place: String) {
        deathPlace.value = place
    }

    fun setDeathCause(cause: String) {
        deathCause.value = cause
    }

    fun setDeathTestResult(result: String) {
        covidResult.value = result
    }

    fun setIsFeelingILI(result: String) {
        feelingLikeILI.value = result
    }

    fun setCovidContactValue(result: String) {
        covidContact.value = result
    }

    private fun formatDate(d: Int, m: Int, y: Int): String {
        val cal = Calendar.getInstance()
        cal.set(Calendar.YEAR, y)
        cal.set(Calendar.MONTH, m)
        cal.set(Calendar.DAY_OF_MONTH, d)
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        return sdf.format(cal.time)
    }

    private fun parseDateToddMMyyyy(time: String): String {
        val inputPattern = "yyyy-MM-dd"
        val inputFormat = SimpleDateFormat(inputPattern)
        val outputFormat = SimpleDateFormat(RMNCHA_DATE_FORMAT)
        var date: Date?
        var str = ""
        try {
            date = inputFormat.parse(time)
            str = outputFormat.format(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return str
    }

    fun addVaccineName(name: String) {
        val vaccineName = MutableLiveData(name)
        vaccinationNames.value = vaccinationNames.value?.toMutableList()
            .also { it?.add(VaccineItemData(name = vaccineName)) }
    }

    fun removeVaccineName(name: String) {
        vaccinationNames.value = vaccinationNames.value?.toMutableList()
            .also { it?.removeIf { data -> data.name.value.contentEquals(name, true) } }
    }

    fun setVaccineDate(item: VaccineItemData, day: Int, month: Int, year: Int) {
        val list = vaccinationNames.value?.toMutableList()
        val targetItem = list?.firstOrNull { it.name == item.name }
        val newItem = targetItem?.copy(expiryDate = MutableLiveData(formatDate(day, month, year)))
        targetItem?.let {
            val index = list.indexOf(targetItem)
            list.removeAt(index)
            list.add(index, newItem!!)
            vaccinationNames.value = list
        }
    }

    fun createImmunization(): MutableLiveData<CreateImmunization> {
        val requestBody = CreateImmunization(
            ashaWorker = selectedAsha.value ?: "",
            childCitizenId = selectedChild.value?.sourceNode?.properties?.uuid ?: "",
            didContactCovidPatient = covidContact.value?.hasValueEqualTo("yes") ?: false,
            description = remarks.value ?: "",
            doseNumber = "",
            isCovidTestDone = isCovidTestDone.value?.hasValueEqualTo("done") ?: false,
            isCovidResultPositive = covidResult.value?.hasValueEqualTo("positive") ?: false,
            isILIExperienced = feelingLikeILI.value?.hasValueEqualTo("yes") ?: false,
            reaction = Reaction(
                isCaseClosed = isCaseClosure.value ?: false,
                caseClosureReason = caseClosureReason.value ?: "",
                date = deathDate.value ?: "",
                type = aefiStatus.value ?: "",
                detail = deathCause.value ?: "",
                death = Death(
                    cause = parseDateToddMMyyyy(deathCause.value ?: "") + "Z",
                    date = deathDate.value ?: "",
                    place = deathPlace.value ?: ""
                )
            ),
            registrationDate = RMNCHAUtils.getCurrentDate().substringBefore("T"),
            series = "",
            targetDiseaseName = "",
            vaccines = vaccinationNames.value?.map {
                Vaccine(
                    code = it.name.value ?: "",
                    earliestDueDate = "",
                    immunization = Immunization(
                        batchNumber = it.batch.value ?: "",
                        expirationDate = it.expiryDate.value ?: "",
                        manufacturer = it.manufacturer.value ?: "",
                        vaccinatedDate = RMNCHAUtils.getCurrentDate().substringBefore("T")
                    ),
                    name = vaccineName.value ?: "",
                    pendingReason = vaccineNotDoneReason.value ?: ""

                )
            } ?: listOf()
        )
        return villageLevelMappingRepository.createChildImmunization(requestBody)
    }

    fun getPncServiceData(): MutableLiveData<PncServiceData>? {
        val pncRegistrationId =
            childMotherDetail.value?.content?.get(0)?.relationship?.properties?.pncInfantRegistrationId
        val pncInfantRegistrationId =
            childMotherDetail.value?.content?.get(0)?.relationship?.properties?.pncInfantRegistrationId
        if (pncInfantRegistrationId != null && pncRegistrationId != null) {
            return villageLevelMappingRepository.getPncServiceData(
                pncRegistrationId,
                pncInfantRegistrationId
            )
        }
        return null
    }
}