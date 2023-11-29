package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.IdspData
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class IDSPViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {


    var saveDataResponse: MutableLiveData<Boolean> = MutableLiveData()

    fun saveIdspData(requestData: IdspData) {
        viewModelScope.launch {
            repo.saveIdspData(requestData).onEach {
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

}