package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.toSharedFlow
import com.ssf.homevisit.healthWellnessSurveillance.data.PlaceRequest
import com.ssf.homevisit.healthWellnessSurveillance.data.PlaceSourceProperties
import com.ssf.homevisit.healthWellnessSurveillance.data.TargetProperties
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectPlaceViewModel @Inject constructor(
    private val getPlaceUseCase: IGetSurveillance
) : ViewModel() {

    val _placeData: MutableLiveData<List<Place>?> = MutableLiveData()

    private val _placeDetails = MutableSharedFlow<String>()
    val placeDetails = _placeDetails.toSharedFlow()

    fun getPlaceList(villageUuid: String, assetType: String) {
        viewModelScope.launch {
            val placeRequest= PlaceRequest(
                relationshipType = "CONTAINEDINPLACE",
                sourceType = "Village",
                sourceProperties = PlaceSourceProperties(
                    uuid = villageUuid
                ),
                targetProperties = TargetProperties(
                    assetType = assetType
                ),
                targetType = "Place",
                stepCount = 1
            )
            getPlaceUseCase.getPlace(villageUuid = villageUuid, assetType = assetType,placeRequest).onEach {
                when (it) {
                    is DataState.Success -> {
                        _placeData.postValue(it.baseResponseData.data)
                    }
                    is DataState.Error -> {
                        _placeData.postValue(mutableListOf())
                    }
                }
            }.launchIn(this)
        }
    }


    fun getImageUrl(bucketKey: String) {
        viewModelScope.launch {
            getPlaceUseCase.getPlaceImageUrl(bucketKey).onEach {
                when (it) {
                    is DataState.Success -> {
                        _placeDetails.emit(it.baseResponseData.preSignedUrl)
                    }
                }
            }.launchIn(this)
        }
    }
}