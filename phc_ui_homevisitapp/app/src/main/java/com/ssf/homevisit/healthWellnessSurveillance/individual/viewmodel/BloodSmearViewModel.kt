package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.BloodSmearRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.GetBloodFilterData
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveIndividualDataResponse
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BloodSmearViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {

    var saveDataResponse: MutableLiveData<SaveIndividualDataResponse?> = MutableLiveData()
    var bloodSmearData: MutableLiveData<GetBloodFilterData?> = MutableLiveData()


    fun saveBloodSmearData(requestData: BloodSmearRequestData) {
        viewModelScope.launch {
            repo.saveBloodSmearData(requestData).onEach {
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


    fun getLabResultsData(citizenId: String) {
        viewModelScope.launch {
            repo.getBloodSmearData(citizenId).onEach {
                when (it) {
                    is DataState.Success -> {
                        bloodSmearData.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        bloodSmearData.postValue(null)
                    }
                }

            }.launchIn(this)
        }
    }
}