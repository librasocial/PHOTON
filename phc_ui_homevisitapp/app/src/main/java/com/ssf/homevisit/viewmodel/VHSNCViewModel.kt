package com.ssf.homevisit.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.ssf.homevisit.activity.VHSNCActivity
import com.ssf.homevisit.models.AllPhcResponse
import com.ssf.homevisit.models.SubCVillResponse
import com.ssf.homevisit.models.SubcentersFromPHCResponse

class VHSNCViewModel(handle: SavedStateHandle): ViewModel() {
    val village = handle.getLiveData<SubCVillResponse.Content>(VHSNCActivity.keyVillage)
    val subCenter = handle.getLiveData<SubcentersFromPHCResponse.Content>(VHSNCActivity.keySubCenter)
    val vhc = handle.getLiveData<AllPhcResponse.Content>(VHSNCActivity.keyVhc)
    val vhsncOrVhd = handle.getLiveData<String>(VHSNCActivity.keyVhsncOrVhd)

    val isLoading = MutableLiveData(false)
}