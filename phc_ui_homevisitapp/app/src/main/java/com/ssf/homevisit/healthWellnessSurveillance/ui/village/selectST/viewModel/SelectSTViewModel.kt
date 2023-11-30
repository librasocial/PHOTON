package com.ssf.homevisit.healthWellnessSurveillance.ui.village.selectST.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.SelectST
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectSTViewModel @Inject constructor(private val repo: IGetSurveillance) : ViewModel() {
    var surveillanceTypeData: MutableList<SelectST> = mutableListOf()
    var assetData: MutableList<Asset> = mutableListOf()
    var houseHoldList: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    var houseHoldListByName: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    var houseHoldListByLocation: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    val _placeDetails:MutableLiveData<String> = MutableLiveData()
     var surveillanceTypePosition: Int = -1
    var assetType:String=""
    fun getHouseHoldList(villageId: String, surveillanceType: String) {
        viewModelScope.launch {
            repo.getHouseholdNearVillage(villageId).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<HouseHoldDataItem> = mutableListOf()
                        var action = ""
                        if (surveillanceType == AppDefines.LARVA_TEST) {
                            action = AppDefines.LARVA_ACTION
                        }
                        if (surveillanceType ==AppDefines.IODINE_TEST) {
                            action = AppDefines.IODINE_ACTION
                        }
                        it.baseResponseData.contents.forEach {
                            data.add(
                                HouseHoldDataItem(
                                    action = action,
                                    houseHoldNumber = it.target.properties.houseID,
                                    nameHouseHoldHead = it.target.properties.houseHeadName,
                                    memberCount = it.target.properties.totalFamilyMembers.toString(),
                                    imageUrl = it.target.properties.houseHeadImageUrls,
                                    latitude = it.target.properties.latitude,
                                    longitude = it.target.properties.longitude,
                                    uuid = it.target.properties.uuid
                                )
                            )
                        }
                        houseHoldList.postValue(data)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {}
                }

            }.launchIn(this)
        }
    }

    fun getHouseHoldByLocation(
        latitude: Double,
        longitude: Double,
        villageId: String,
        surveillanceType: String
    ) {
        viewModelScope.launch {
            repo.getHouseHoldNearMe(villageId, latitude, longitude).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<HouseHoldDataItem> = mutableListOf()
                        var action = ""
                        if (surveillanceType == AppDefines.LARVA_TEST) {
                            action = AppDefines.LARVA_ACTION
                        }
                        if (surveillanceType == AppDefines.IODINE_TEST) {
                            action = AppDefines.IODINE_ACTION
                        }
                        it.baseResponseData.contents.forEach {
                            data.add(
                                HouseHoldDataItem(
                                    action = action,
                                    houseHoldNumber = it.houseID,
                                    nameHouseHoldHead = it.houseHeadName,
                                    memberCount = it.totalFamilyMembers.toString(),
                                    imageUrl = it.houseHeadImageUrls,
                                    uuid = it.uuid
                                )
                            )
                        }
                        houseHoldListByLocation.postValue(data)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {}
                }

            }.launchIn(this)
        }
    }

    fun getHouseholdByName(villageLgdCode :String, name: String, surveillanceType: String) {
        viewModelScope.launch {
            repo.getHouseHoldByName(villageLgdCode, name).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<HouseHoldDataItem> = mutableListOf()
                        var action = ""
                        if (surveillanceType == AppDefines.LARVA_TEST) {
                            action = AppDefines.LARVA_ACTION
                        }
                        if (surveillanceType == AppDefines.IODINE_TEST) {
                            action = AppDefines.IODINE_ACTION
                        }
                        it.baseResponseData.contents.forEach {
                            data.add(
                                HouseHoldDataItem(
                                    action = action,
                                    houseHoldNumber = it.properties.houseID,
                                    nameHouseHoldHead = it.properties.houseHeadName,
                                    memberCount = it.properties.totalFamilyMembers.toString(),
                                    imageUrl = it.properties.houseHeadImageUrls,
                                    latitude = it.properties.latitude,
                                    longitude = it.properties.longitude,
                                    uuid = it.properties.uuid
                                )
                            )
                        }
                        houseHoldListByName.postValue(data)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {}
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


    fun getAssetData(surveillanceType: String) {
        assetData = mutableListOf()
        when (surveillanceType) {
            AppDefines.IODINE_TEST -> {
                assetData.add(
                    Asset(
                        title = "Shop"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Hotel"
                    )
                )
                assetData.add(
                    Asset(
                        title = "School/collage"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Others"
                    )
                )
            }
            AppDefines.WATER_SAMPLE_TEST -> {
                assetData.add(
                    Asset(
                        title = "WaterBody"
                    )
                )
                assetData.add(
                    Asset(
                        title = "School/collage"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Hotel"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Others"
                    )
                )
            }
            else -> {
                assetData.add(
                    Asset(
                        title = "Temple"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Mosque"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Church"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Hotel"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Office"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Hospital"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Construction"
                    )
                )
                assetData.add(
                    Asset(
                        title = "School/collage"
                    )
                )
                assetData.add(
                    Asset(
                        title = "BusStop"
                    )
                )
                assetData.add(
                    Asset(
                        title = "WaterBody"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Shop"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Toilet"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Park"
                    )
                )
                assetData.add(
                    Asset(
                        title = "Others"
                    )
                )
            }
        }

    }

    fun getSurveillanceTypeData(flowType: String) {
        surveillanceTypeData = mutableListOf()
        when (flowType) {
            AppDefines.HNW_VILLAGE_SURVEILLANCE -> {
                surveillanceTypeData.add(
                    SelectST(
                        AppDefines.LARVA_TEST,
                    )
                )
                surveillanceTypeData.add(
                    SelectST(
                        AppDefines.WATER_SAMPLE_TEST,
                    )
                )
                surveillanceTypeData.add(
                    SelectST(
                        AppDefines.IODINE_TEST,
                    )
                )
            }
            AppDefines.HNW_HOUSEHOLD_SURVEILLANCE -> {
                surveillanceTypeData.add(
                    SelectST(
                        AppDefines.LARVA_TEST,
                    )
                )
                surveillanceTypeData.add(
                    SelectST(
                        AppDefines.IODINE_TEST,
                    )
                )
            }
        }

    }

}