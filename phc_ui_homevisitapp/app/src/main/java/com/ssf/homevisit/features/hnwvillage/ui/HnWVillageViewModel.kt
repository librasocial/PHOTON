package com.ssf.homevisit.features.hnwvillage.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssf.homevisit.features.hnwvillage.data.IHnWRepository
import com.ssf.homevisit.features.hnwvillage.data.SurveillanceDataType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HnWVillageViewModel @Inject constructor(
    private val iHnWRepository: IHnWRepository,
    private val _mutableLiveData: MutableLiveData<List<SurveillanceDataType>>
) : ViewModel() {
    public val surveillanceLiveData: LiveData<List<SurveillanceDataType>> = _mutableLiveData

    init {
        getSurveillanceSelectionItemList()
    }

    private fun getSurveillanceSelectionItemList() {
        val result = iHnWRepository.getHnWSurveillanceType()
        _mutableLiveData.value = result
    }
}