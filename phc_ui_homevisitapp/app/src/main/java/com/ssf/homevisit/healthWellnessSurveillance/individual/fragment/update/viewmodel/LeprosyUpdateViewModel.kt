package com.ssf.homevisit.healthWellnessSurveillance.individual.fragment.update.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.LeprosyRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeprosyUpdateViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {


    var saveDataResponse: MutableLiveData<Boolean> = MutableLiveData()
    var presignedUrl: MutableLiveData<String> = MutableLiveData()

    fun updateLeprosy(requestData: LeprosyRequestData, surveillanceId: String) {
        viewModelScope.launch {
            repo.updateLeprosyData(requestData, surveillanceId).onEach {
                when (it) {
                    is DataState.Success -> {
                        saveDataResponse.postValue(true)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        saveDataResponse.postValue(false)
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