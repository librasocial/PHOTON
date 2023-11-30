package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.GetLeprosyFilterData
import com.ssf.homevisit.healthWellnessSurveillance.data.LeprosyRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveIndividualDataResponse
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LeprosyViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {

    var saveDataResponse: MutableLiveData<SaveIndividualDataResponse?> = MutableLiveData()
    var leprosyData: MutableLiveData<GetLeprosyFilterData?> = MutableLiveData()

    fun saveLeprosyData(requestData: LeprosyRequestData) {
        viewModelScope.launch {
            repo.saveLeprosyData(requestData).onEach {
                when (it) {
                    is DataState.Success -> {
                        saveDataResponse.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        Log.d("TAG",it.errorMessage)
                    }
                }

            }.launchIn(this)
        }
    }


    fun getLabResultsData(citizenId: String) {
        viewModelScope.launch {
            repo.getLeprosyData(citizenId).onEach {
                when (it) {
                    is DataState.Success -> {
                        leprosyData.postValue(it.baseResponseData)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        leprosyData.postValue(null)
                    }
                }

            }.launchIn(this)
        }
    }

}