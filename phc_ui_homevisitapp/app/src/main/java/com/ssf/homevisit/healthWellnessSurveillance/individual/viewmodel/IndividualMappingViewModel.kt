package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssf.homevisit.healthWellnessSurveillance.data.ChildrenRequestData
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.HouseHoldDataItem
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IndividualMappingViewModel @Inject constructor(private val repo: IGetSurveillance) :
    ViewModel() {

    var houseHoldList: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    var childrenList: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    var houseHoldListByName: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    var houseHoldListByLocation: MutableLiveData<MutableList<HouseHoldDataItem>> = MutableLiveData()
    val _placeDetails: MutableLiveData<String> = MutableLiveData()
    lateinit var citizenInfo: HouseHoldDataItem
    lateinit var surveillanceType: String

    fun getHouseHoldList(villageId: String, surveillanceType: String) {
        viewModelScope.launch {
            repo.getHouseholdNearVillage(villageId).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<HouseHoldDataItem> = mutableListOf()
                        var action = ""
                        when (surveillanceType) {
                            AppDefines.LEPROSY -> {
                                action = "Select HH for Leprosy Surveillance"
                            }
                            AppDefines.COVID -> {
                                action = "Select HH for COVID Surveillance"
                            }
                            AppDefines.IDSP_S_Form -> {
                                action = "Select HH for Leprosy Surveillance"
                            }
                            AppDefines.BLOOD_SMEAR -> {
                                action = "Select HH for Blood Smear Testing"
                            }
                            AppDefines.ACTIVE_CASE_FINDING -> {
                                action = "Select HH for ACF"
                            }
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
                        when (surveillanceType) {
                            AppDefines.LEPROSY -> {
                                action = "Select HH for Leprosy Surveillance"
                            }
                            AppDefines.URINE_SAMPLE -> {
                                action = "Select to Update Sample Details"
                            }
                            AppDefines.COVID -> {
                                action = "Select HH for COVID Surveillance"
                            }
                            AppDefines.IDSP_S_Form -> {
                                action = "Select HH for Leprosy Surveillance"
                            }
                            AppDefines.BLOOD_SMEAR -> {
                                action = "Select HH for Blood Smear Testing"
                            }
                            AppDefines.ACTIVE_CASE_FINDING -> {
                                action = "Select HH for ACF"
                            }
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


    fun getChildrenInVillage(villageId: String, requestData: ChildrenRequestData) {
        viewModelScope.launch {
            repo.getChildrenInVillage(villageId, requestData).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<HouseHoldDataItem> = mutableListOf()
                        val action = "Select to Update Sample Details"
                        it.baseResponseData.contents.forEach {
                            data.add(
                                HouseHoldDataItem(
                                    action = action,
                                    uuid = it.source.properties.uuid,
                                    name = it.source.properties.firstName,
                                    age = it.source.properties.age,
                                    phoneNumber = it.source.properties.contact,
                                    dob = it.source.properties.dateOfBirth,
                                    imageUrl = it.source.properties.imageUrls,
                                    gender = it.source.properties.gender
                                )
                            )
                        }
                        childrenList.postValue(data)
                    }
                    is DataState.Loading -> {}
                    is DataState.Error -> {}
                }

            }.launchIn(this)
        }
    }

    fun getHouseholdByName(villageId: String, name: String, surveillanceType: String) {
        viewModelScope.launch {
            repo.getHouseHoldByName(villageId, name).onEach {
                when (it) {
                    is DataState.Success -> {
                        val data: MutableList<HouseHoldDataItem> = mutableListOf()
                        var action = ""
                        when (surveillanceType) {
                            AppDefines.LEPROSY -> {
                                action = "Select HH for Leprosy Surveillance"
                            }
                            AppDefines.URINE_SAMPLE -> {
                                action = "Select to Update Sample Details"
                            }
                            AppDefines.COVID -> {
                                action = "Select HH for COVID Surveillance"
                            }
                            AppDefines.IDSP_S_Form -> {
                                action = "Select HH for Leprosy Surveillance"
                            }
                            AppDefines.BLOOD_SMEAR -> {
                                action = "Select HH for Blood Smear Testing"
                            }
                            AppDefines.ACTIVE_CASE_FINDING -> {
                                action = "Select HH for ACF"
                            }
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


}