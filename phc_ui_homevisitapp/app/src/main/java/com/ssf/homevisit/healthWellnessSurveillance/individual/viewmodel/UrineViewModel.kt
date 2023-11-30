package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.GetUrineFilterData
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveIndividualDataResponse
import com.ssf.homevisit.healthWellnessSurveillance.data.UrineRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UrineViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {

    var saveDataResponse: MutableLiveData<SaveIndividualDataResponse?> = MutableLiveData()
    var urineData: MutableLiveData<GetUrineFilterData?> = MutableLiveData()
    var presignedUrl: MutableLiveData<String> = MutableLiveData()

    fun saveUrineData(requestData: UrineRequestData) {
        viewModelScope.launch {
            repo.saveUrineData(requestData).onEach {
                when (it) {
                    is DataState.Success -> {
                        saveDataResponse.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                    }
                }

            }.launchIn(this)
        }
    }

    fun getLabReportData(citizenId: String) {
        viewModelScope.launch {
            repo.getUrineData(citizenId).onEach {
                when (it) {
                    is DataState.Success -> {
                        urineData.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        urineData.postValue(null)
                    }
                }

            }.launchIn(this)
        }
    }

    fun getImageUrl(bucketKey: String) {
        viewModelScope.launch {
            repo.getImageUrl(bucketKey).onEach {
                when (it) {
                    is DataState.Success -> {
                        presignedUrl.postValue(it.baseResponseData.preSignedUrl)
                    }
                }
            }.launchIn(this)
        }
    }
}