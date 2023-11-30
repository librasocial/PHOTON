package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.AcfRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.GetAcfFilterData
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveIndividualDataResponse
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AcfViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {


    var saveDataResponse: MutableLiveData<SaveIndividualDataResponse?> = MutableLiveData()
    var acfData:MutableLiveData<GetAcfFilterData?> = MutableLiveData()

    fun saveAcfData(requestData: AcfRequestData) {
        viewModelScope.launch {
            repo.saveAcfData(requestData).onEach {
                when (it) {
                    is DataState.Success -> {
                        saveDataResponse.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        Log.d("TAG", it.errorMessage)
                    }
                }

            }.launchIn(this)
        }
    }

    fun getData(citizenId: String) {
        viewModelScope.launch {
            repo.getAcfData(citizenId).onEach {
                when (it) {
                    is DataState.Success -> {
                        acfData.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        Log.d("TAG", it.errorMessage)
                        acfData.postValue(null)
                    }
                }

            }.launchIn(this)
        }
    }
}

