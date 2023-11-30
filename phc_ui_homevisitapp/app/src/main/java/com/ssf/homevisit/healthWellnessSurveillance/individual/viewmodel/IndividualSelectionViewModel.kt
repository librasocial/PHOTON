package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.IndividualDetailProperty
import com.ssf.homevisit.healthWellnessSurveillance.data.LabResultStatusResponse
import com.ssf.homevisit.healthWellnessSurveillance.data.Response
import com.ssf.homevisit.healthWellnessSurveillance.data.repImpl.GetSurveillanceRepo
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndividualSelectionViewModel @Inject constructor(private val repo: GetSurveillanceRepo) :
    ViewModel() {


    var citizensInHousehold: MutableLiveData<MutableList<IndividualDetailProperty>> =
        MutableLiveData()
    var individualSurveillanceData: MutableLiveData<Response?> = MutableLiveData()
    val _placeDetails: MutableLiveData<String> = MutableLiveData()
    var individualDetailPropertyData: IndividualDetailProperty? = null
    var citizenUuid: String = ""
    var bloodSmearType: String? = null
    var surveillanceId: String = ""
    lateinit var surveillanceType: String
    lateinit var houseUuid: String
    var labResultStatus: MutableLiveData<List<LabResultStatusResponse?>?> = MutableLiveData()

    fun getIndividualSurveillanceData(flowType: String) {
        viewModelScope.launch {
            repo.getIndividualSurveillanceData(flowType).onEach {
                when (it) {
                    is DataState.Success -> {
                        individualSurveillanceData.postValue(it.baseResponseData)
                    }
                }
            }.launchIn(this)
        }
    }


    fun getLabResultStatus(url: String, citizenList: MutableList<String>) {
        var citizenUuidId: String = ""
        citizenList.forEachIndexed { index, s ->
            if (index == citizenList.size - 1) {
                citizenUuidId += citizenList[index]
            } else {
                citizenUuidId += citizenList[index]
                citizenUuidId += ","
            }
        }
        viewModelScope.launch {
            repo.getLabResultStatus(url = url, citizenUuidId).onEach {
                when (it) {
                    is DataState.Success -> {
                        labResultStatus.postValue(it.baseResponseData)
                    }
                }
            }.launchIn(this)
        }
    }

    fun getCitizensInHouse(citizenId: String) {
        viewModelScope.launch {
            repo.getCitizensInHouse(citizenId).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<IndividualDetailProperty> = mutableListOf()
                        it.baseResponseData.contents.forEach {
                            if (it.target?.properties != null) {
                                data.add(it.target.properties)
                            }
                        }
                        citizensInHousehold.postValue(data)
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
                        _placeDetails.postValue(it.baseResponseData.preSignedUrl)
                    }
                }
            }.launchIn(this)
        }
    }

}