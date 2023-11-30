package com.ssf.homevisit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.common.extension.toSharedFlow
import com.ssf.homevisit.healthWellnessSurveillance.data.SaveSurvey
import com.ssf.homevisit.healthWellnessSurveillance.data.Source
import com.ssf.homevisit.healthWellnessSurveillance.data.SurveyFormResponse
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IVillageSurvey
import com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse.BaseResponse
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.NaturalResourceDataItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class NaturalResourceViewModel @Inject constructor(
    private val getDivisionDetailUseCase: IVillageSurvey
) : ViewModel() {

    var naturalResourceList: MutableList<NaturalResourceDataItem> = mutableListOf()
     lateinit var villageUuid: String
    private val _saveResponseResult = MutableSharedFlow<DataState<Unit>>()
    val saveResponseResult = _saveResponseResult.toSharedFlow()
    val naturalResourceListLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val selectedSurveyTypeHashMap: MutableSet<Int> = mutableSetOf()
    private val _villageFormResponse =
        MutableSharedFlow<DataState<BaseResponse<SurveyFormResponse?>>>()
    val villageFormResponse = _villageFormResponse.toSharedFlow()

    private val _stateByCountryData = MutableSharedFlow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>()
    val stateByCountryData = _stateByCountryData.toSharedFlow()
    private val _districtByStateData = MutableSharedFlow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>()
    val districtByStateData = _districtByStateData.toSharedFlow()
    private val _talukByDistrictData = MutableSharedFlow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>()
    val talukByDistrictData = _talukByDistrictData.toSharedFlow()
    private val _gramByTalukData = MutableSharedFlow<DataState<BaseResponse<List<com.ssf.homevisit.healthWellnessSurveillance.data.Target>>>>()
    val gramByTalukData = _gramByTalukData.toSharedFlow()
    var selectedStatePosition:Int=-1
    var selectedDistrictPosition:Int=-1
    var selectedTalukPosition:Int=-1
    var selectedGramPanchayatPosition:Int=-1

    fun fetchData() {
        naturalResourceList = mutableListOf()
        naturalResourceList.add(NaturalResourceDataItem("Quality of\n" + "Environment &\n" + "Sanitation"))
        naturalResourceList.add(NaturalResourceDataItem("Water Sources\n" + "in the Village"))
        naturalResourceList.add(NaturalResourceDataItem("Village\n" + "Infrastructure"))
        naturalResourceList.add(NaturalResourceDataItem("Available Mode\n" + "of Transport"))
        naturalResourceList.add(NaturalResourceDataItem("Availability of\n" + "Public Facilities"))
        selectedSurveyTypeHashMap.forEach {
            naturalResourceList[it].completed = true
        }
        naturalResourceListLiveData.postValue(true)
    }

    fun fetchSurveyResponse(surveyName: String) {
        viewModelScope.launch {
            getDivisionDetailUseCase.villageSurveyFormResponse(surveyName).onEach {
                when (it) {
                    is DataState.Success -> {
                        _villageFormResponse.emit(it)
                    }
                    is DataState.Error -> {}
                    is DataState.Loading -> {}
                }
            }.launchIn(this)
        }
    }

    fun saveSurveyResponse(saveSurvey: SaveSurvey) {
        viewModelScope.launch {
            getDivisionDetailUseCase.saveSurveyResponse(saveSurvey).onEach {
                when (it) {
                    is DataState.Success -> {
                        _saveResponseResult.emit(DataState.Success(Unit))
                    }
                    is DataState.Error -> _saveResponseResult.emit(it)
                    is DataState.Loading -> _saveResponseResult.emit(DataState.Loading)
                }

            }.launchIn(this)
        }
    }

    fun fetchDistrictByState(srcId: String){
        viewModelScope.launch {
            getDivisionDetailUseCase.fetchDistrictByState(srcId).onEach {
                _districtByStateData.emit(it)
            }.launchIn(this)
        }
    }


    fun fetchStateByCountry(){
        viewModelScope.launch {
            getDivisionDetailUseCase.fetchStateByCountry().onEach {
                _stateByCountryData.emit(it)
            }.launchIn(this)
        }
    }

    fun fetchTalukByDistrict(srcId:String){
        viewModelScope.launch {
            getDivisionDetailUseCase.fetchTalukByDistrict(srcId).onEach {
                _talukByDistrictData.emit(it)
            }.launchIn(this)
        }
    }

    fun fetchGramByTaluk(srcId: String){
        viewModelScope.launch {
            getDivisionDetailUseCase.fetchGramPanchayatByTaluk(srcId).onEach {
                _gramByTalukData.emit(it)
            }.launchIn(this)
        }
    }
}