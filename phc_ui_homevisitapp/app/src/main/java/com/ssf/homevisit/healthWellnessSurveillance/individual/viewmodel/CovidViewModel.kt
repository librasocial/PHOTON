package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.CovidRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CovidViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {

     var saveDataResponse: MutableLiveData<Boolean> = MutableLiveData()


    fun saveCovidData(requestData: CovidRequestData) {
        viewModelScope.launch {
            repo.saveCovidData(requestData).onEach {
                when (it) {
                    is DataState.Success -> {
                        saveDataResponse.postValue(true)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {
                        Log.d("TAG", it.errorMessage)
                        saveDataResponse.postValue(false)
                    }
                }

            }.launchIn(this)
        }
    }


}