package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.toSharedFlow
import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.CreateIodineSample
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class WaterSampleViewModel @Inject constructor(
        private val repo: IGetSurveillance
) : ViewModel() {


    private val _isWaterSurveillanceCreated = MutableSharedFlow<Boolean>()
    val isWaterSurveillanceCreated = _isWaterSurveillanceCreated.toSharedFlow()

    private val _waterSurveillanceByFilter = MutableSharedFlow<List<WaterSampleContent>>()
    val waterSurveillanceByFilter = _waterSurveillanceByFilter.toSharedFlow()

    private val _isWaterSampleUpdated = MutableSharedFlow<Boolean>()
    val isWaterSampleUpdated = _isWaterSampleUpdated.toSharedFlow()

    //iodine
    private val _isIodineSurveillanceCreated = MutableSharedFlow<Boolean>()
    val isIodineSurveillanceCreated = _isIodineSurveillanceCreated.toSharedFlow()

    private val _isIodineSampleCreated = MutableSharedFlow<Boolean>()
    val isIodineSampleCreated = _isIodineSampleCreated.toSharedFlow()

    private val _iodineSurveillanceAlreadyCreated = MutableSharedFlow<Boolean>()
    val isIodineSurveillanceAlreadyCreated = _iodineSurveillanceAlreadyCreated.toSharedFlow()

    private val _iodineSampleByFilter = MutableSharedFlow<List<IodineSampleContent>>()
    val iodineSampleByFilter = _iodineSampleByFilter.toSharedFlow()

    private val _isIodineSampleUpdated = MutableSharedFlow<Boolean>()
    val isIodineSampleUpdated = _isIodineSampleUpdated.toSharedFlow()
    lateinit var waterSampleId: String
    lateinit var iodineSurveillanceId: String


    fun createWaterSample(
            villageId: String,
            placeId: String,
            placeType: String,
            placeName: String,
            sample: WaterSample,
            userId: String,
            dateOfSurveillance: String?=null
    ) {
        viewModelScope.launch {
            repo.createWaterSampleSurveillance(
                    WaterSampleContent(
                            placeName = placeName,
                            placeOrgId = placeId,
                            placeType = placeType,
                            villageId = villageId,
                            sample = sample,
                            dateOfSurveillance = dateOfSurveillance
                    ), userId = userId
            ).onEach {
                when (it) {
                    is DataState.Error -> _isWaterSurveillanceCreated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        waterSampleId = it.baseResponseData.contents.first().id.toString()
                        _isWaterSurveillanceCreated.emit(it.baseResponseData.contents.isNotEmpty())
                    }
                }
            }.launchIn(this)
        }
    }

    fun getWaterSampleByFilter(
            villageId: String? = null,
            placeId: String? = null,
            placeType: String? = null
    ) {
        viewModelScope.launch {
            repo.getWaterSampleByFilter(
                    villageId = villageId,
                    placeOrgId = placeId,
                    page = 0,
                    size = 5000,
                    placeType = placeType,
                    dateOfSurvey = null
            ).onEach {
                when (it) {
                    is DataState.Error -> Timber.e(it.errorMessage)
                    DataState.Loading -> Timber.e(it.toString())
                    is DataState.Success -> {
                        _waterSurveillanceByFilter.emit(it.baseResponseData.contents)
                    }
                }
            }.launchIn(this)
        }
    }

    fun createIodineSampleSurveillance(
            iodineSurveillanceId: String,
            shopOwnerName: String,
            saltBrandName: String,
            noOfSamplesCollected: Int,
            dateCollected: String,
            labSubmittedDate: String? = null,
            labTestResultStatus: String? = null,
            reportImageUrl: MutableList<String>? = null,
            iodineResultQty: Int? = null,
            resultDate: String? = null,
            userId: String
    ) {
        viewModelScope.launch {
            repo.createIodineSample(
                    CreateIodineSample(
                            iodineSurveillanceId,
                            shopOwnerName,
                            saltBrandName,
                            noOfSamplesCollected,
                            dateCollected,
                            labSubmittedDate,
                            labTestResultStatus,
                            reportImageUrl,
                            iodineResultQty,
                            resultDate
                    ), userId = userId
            ).onEach {
                when (it) {
                    is DataState.Error -> _isIodineSampleCreated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        _isIodineSampleCreated.emit(it.baseResponseData.contents.isNotEmpty())
                    }
                }
            }.launchIn(this)
        }
    }


    fun createIodineSurveillance(
            villageId: String? = null,
            placeId: String? = null,
            placeType: String? = null,
            placeName: String? = null,
            userId: String,
            houseHoldId: String? = null,
            dateOfSurveillance:String?=null
    ) {
        viewModelScope.launch {
            repo.createIodineSurveillance(
                    CheckIfSurveillanceIsCreated(
                            dateOfSurveillance=dateOfSurveillance,
                            householdId = houseHoldId,
                            placeName = placeName,
                            placeOrgId = placeId,
                            placeType = placeType,
                            villageId = villageId,
                    ), userId = userId
            ).onEach {
                when (it) {
                    is DataState.Error -> _isWaterSurveillanceCreated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        iodineSurveillanceId = it.baseResponseData.content.first().id
                        _isIodineSurveillanceCreated.emit(it.baseResponseData.content.isNotEmpty())
                    }
                }
            }.launchIn(this)
        }
    }


    fun getIodineSampleByFilter(userId: String, iodineSurveillanceId: String) {
        viewModelScope.launch {
            repo.getIodineSampleByFilter(
                    userId, iodineSurveillanceId
            ).onEach {
                when (it) {
                    is DataState.Error -> Timber.e(it.errorMessage)
                    is DataState.Loading -> Timber.e(it.toString())
                    is DataState.Success -> {
                        _iodineSampleByFilter.emit(it.baseResponseData.contents)
                    }
                }
            }.launchIn(this)
        }
    }

    fun getIodineSurveillanceByFilter(
            villageId: String? = null,
            placeId: String? = null,
            placeType: String? = null,
            placeName: String? = null,
            userId: String,
            houseHoldId: String? = null
    ) {
        viewModelScope.launch {
            repo.getIodineSurveillanceByFilter(
                    CheckIfSurveillanceIsCreated(
                            householdId = houseHoldId,
                            placeName = placeName,
                            placeOrgId = placeId,
                            placeType = placeType,
                            villageId = villageId,
                    ),
                    userId = userId
            ).onEach {
                when (it) {
                    is DataState.Error -> _iodineSurveillanceAlreadyCreated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        val content=it.baseResponseData.content
                        if (content.isNotEmpty()) {
                            iodineSurveillanceId=content.first().id
                            _iodineSurveillanceAlreadyCreated.emit(true)
                        } else {
                            _iodineSurveillanceAlreadyCreated.emit(false)
                        }
                    }

                }
            }.launchIn(this)
        }
    }


    fun updateIodineReport(
            iodineSurveillanceId: String,
            reportImageUrl: MutableList<String>? = null,
            iodineResultQty: Int? = null,
            resultDate: String? = null,
            userId: String,
            iodineSampleId:String,
            labSubmittedDate:String?=null
    ) {
        viewModelScope.launch {
            repo.updateIodineSample(
                    iodineSampleData =
                    CreateIodineSample(
                            iodineSurveillanceId,
                            reportImageUrl =
                            reportImageUrl,
                            iodineResultQty =
                            iodineResultQty,
                            resultDate =
                            resultDate,
                            labSubmittedDate = labSubmittedDate
                    ), userId = userId, iodineSampleId = iodineSampleId
            ).onEach {
                when (it) {
                    is DataState.Error -> _isIodineSampleUpdated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        _isIodineSampleUpdated.emit(true)
                    }
                }
            }.launchIn(this)
        }
    }

    fun updateWaterSampleReport(
            villageId: String,
            placeId: String,
            placeType: String,
            placeName: String,
            testResult: TestResult,
            userId: String,
            sampleId: String
    ) {
        viewModelScope.launch {
            repo.updateWaterSampleSurveillance(
                    waterSampleData = WaterSampleContent(
                            testResult = testResult,
                            villageId = villageId,
                            placeName = placeName,
                            placeType = placeType,
                            placeOrgId = placeId,
                            id = sampleId
                    ), userId = userId
            ).onEach {
                when (it) {
                    is DataState.Error -> _isWaterSampleUpdated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        _isWaterSampleUpdated.emit(true)
                    }
                }
            }.launchIn(this)
        }
    }

}