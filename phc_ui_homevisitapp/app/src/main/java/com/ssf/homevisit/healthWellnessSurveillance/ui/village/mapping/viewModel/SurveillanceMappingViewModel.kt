package com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.R
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.toSharedFlow
import com.ssf.homevisit.healthWellnessSurveillance.data.CheckIfSurveillanceIsCreated
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.SurveillanceMappingMenuItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.mapping.SurveillanceMappingModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.IsSurveillanceCreated
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.LarvaVisitModel
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.LarvaVisitModelResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SurveillanceMappingViewModel @Inject constructor(
    private val repo: IGetSurveillance
) : ViewModel() {

    private val _mappingList = MutableStateFlow(emptyList<SurveillanceMappingModel>())
    val mappingList = _mappingList.toSharedFlow()

    private val _isSurveillanceCreated = MutableSharedFlow<Boolean>()
    val isSurveillanceCreated = _isSurveillanceCreated.toSharedFlow()

    private val _larvaSurveillance = MutableSharedFlow<IsSurveillanceCreated>()
    val larvaSurveillance = _larvaSurveillance.toSharedFlow()

    private val _saveLarvaSurveillance = MutableSharedFlow<Boolean>()
    val saveLarvaSurveillance = _saveLarvaSurveillance.toSharedFlow()

    private val _larvaVisit = MutableSharedFlow<DataState<LarvaVisitModelResponse>>()
    val larvaVisit = _larvaVisit.toSharedFlow()

    lateinit var surveillanceId: String

    private val _preSignedUrl = MutableSharedFlow<String>()
    val preSignedUrl = _preSignedUrl.toSharedFlow()

    private val _uploadToBucket = MutableSharedFlow<Boolean>()
    val uploadToBucket = _uploadToBucket.toSharedFlow()


    fun createSurveillance(
        dateOfSurveillance: String,
        villageId: String?=null,
        placeId: String?=null,
        placeType: String?=null,
        placeName: String?=null,
        houseHoldId: String?=null
    ) {
        viewModelScope.launch {
            repo.checkLarvaCreated(
                    CheckIfSurveillanceIsCreated(
                            dateOfSurveillance = dateOfSurveillance,
                            householdId = houseHoldId,
                            placeName = placeName,
                            placeOrgId = placeId,
                            placeType = placeType,
                            villageId = villageId
                    )
            ).onEach {
                when (it) {
                    is DataState.Error -> _isSurveillanceCreated.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        surveillanceId = it.baseResponseData.content.first().id
                        _isSurveillanceCreated.emit(it.baseResponseData.content.isNotEmpty())
                    }
                }
            }.launchIn(this)
        }
    }

    fun getLarvaVisits(surveillanceId: String) {
        viewModelScope.launch {
            repo.getLarvaSurveillanceVisitsByFilter(surveillanceId).onEach {
                _larvaVisit.emit(it)
            }.launchIn(this)
        }
    }

    fun getLarvaByFilter(villageId: String, placeId: String?=null, placeType: String?=null,houseHoldId:String?=null) {
        viewModelScope.launch {
            repo.getLarvaByFilter(
                villageId = villageId,
                placeOrgId = placeId,
                householdId = houseHoldId,
                page = 0,
                size = 5000,
                placeType = placeType
            ).onEach {
                when (it) {
                    is DataState.Error -> Timber.e(it.errorMessage)
                    DataState.Loading -> Timber.e(it.toString())
                    is DataState.Success -> {
                        if (it.baseResponseData.content.size > 0) {
                            surveillanceId = it.baseResponseData.content.first().id
                        }
                        _larvaSurveillance.emit(it.baseResponseData)
                    }
                }
            }.launchIn(this)
        }
    }

    fun saveSurveillance(saveSurveillance: SaveSurveillance) {
        viewModelScope.launch {
            repo.saveSurveillanceVisit(saveSurveillance).onEach {
                when (it) {
                    is DataState.Error -> Timber.e(it.errorMessage)
                    DataState.Loading -> Timber.e(it.toString())
                    is DataState.Success -> _saveLarvaSurveillance.emit(true)
                }
            }.launchIn(this)
        }
    }

    fun initMappingList() {
        viewModelScope.launch(Dispatchers.Default) {
            val list = emptyList<SurveillanceMappingModel>().toMutableList()

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.stone_tank,
                    menuItem = SurveillanceMappingMenuItem.CementAndStoneTank
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.drum,
                    menuItem = SurveillanceMappingMenuItem.DrumAndBarrel
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.pot, menuItem = SurveillanceMappingMenuItem.Pot
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.freezer,
                    menuItem = SurveillanceMappingMenuItem.FreezerAndCooler
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.sump, menuItem = SurveillanceMappingMenuItem.Sump
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.solid_waste,
                    menuItem = SurveillanceMappingMenuItem.SolidWaste
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.grinding_stone,
                    menuItem = SurveillanceMappingMenuItem.GrindingStone
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.tap_pits, menuItem = SurveillanceMappingMenuItem.TapPits
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.tire, menuItem = SurveillanceMappingMenuItem.Tire
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.coconut,
                    menuItem = SurveillanceMappingMenuItem.CoconutShell
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.well, menuItem = SurveillanceMappingMenuItem.Well
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = R.drawable.pond, menuItem = SurveillanceMappingMenuItem.Pond
                )
            )

            list.add(
                SurveillanceMappingModel(
                    menuIconId = 0, menuItem = SurveillanceMappingMenuItem.Others
                )
            )

            _mappingList.emit(list)
        }
    }

    fun getPreSignedUrl(bucketKey: String) {
        viewModelScope.launch {
            repo.getPreSignedUrl(bucketKey).onEach {
                when (it) {
                    is DataState.Error -> _preSignedUrl.emit("")
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        _preSignedUrl.emit(it.baseResponseData.preSignedUrl)
                    }

                }
            }.launchIn(this)
        }
    }

    fun uploadToS3Bucket(preSignedUrl: String, contentType: String, body: RequestBody) {
        viewModelScope.launch {
            repo.putImageToS3Bucket(preSignedUrl, contentType, body).onEach {
                when (it) {
                    is DataState.Error -> _uploadToBucket.emit(false)
                    is DataState.Loading -> {}
                    is DataState.Success -> {
                        _uploadToBucket.emit(true)
                    }

                }
            }.launchIn(this)
        }
    }

}