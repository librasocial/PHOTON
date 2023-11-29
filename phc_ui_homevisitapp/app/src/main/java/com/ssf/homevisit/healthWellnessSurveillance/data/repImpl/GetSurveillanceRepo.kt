package com.ssf.homevisit.healthWellnessSurveillance.data.repImpl

import com.ssf.homevisit.healthWellnessSurveillance.data.*
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.network.baseResponse.BaseResponse
import com.ssf.homevisit.healthWellnessSurveillance.network.dataState.DataState
import com.ssf.homevisit.healthWellnessSurveillance.network.safeApiRequest.SafeApiRequest
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.CreateIodineSample
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.CreateSurveillanceResponse
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.LarvaVisitModelResponse
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Place
import com.ssf.homevisit.requestmanager.ApiService
import com.ssf.homevisit.utils.Util
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import javax.inject.Inject

class GetSurveillanceRepo @Inject constructor(
    private val safeApiRequest: SafeApiRequest, private val apiService: ApiService
) : IGetSurveillance {

    override suspend fun getPlace(
        villageUuid: String,
        assetType: String,
        placeRequest: PlaceRequest
    ): Flow<DataState<BaseResponse<List<Place>>>> = flow {
        emit(DataState.Loading)
        return@flow emit(safeApiRequest.apiRequest {
            val data = apiService.getPlace(
               placeRequest=placeRequest,
                token = "Bearer " + Util.getHeader()
            ).contents.map {
                it.toPlace()
            }
            BaseResponse(data = data, isError = false)
        })
    }


    private fun Content.toPlace(): Place {
        return Place(
            name = target.properties.name,
            imageUrl = target.properties.imgUrl,
            latitude = target.properties.latitude,
            longitude = target.properties.longitude,
            assetType = target.properties.assetType,
            requiredSurveys = target.properties.requiredSurveys,
            id = target.properties.uuid,
            label = target.labels.first(),
            assetSubType = target.properties.assetSubType
        )
    }

    override suspend fun checkLarvaCreated(
        checkIfSurveillanceIsCreated: CheckIfSurveillanceIsCreated
    ) = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.checkLarvaCreatedByFilter(
                authorization = "Bearer " + Util.getHeader(),
                checkIfSurveillanceIsCreated = checkIfSurveillanceIsCreated
            )
        })
    }

    override suspend fun getLarvaSurveillanceVisitsByFilter(surveillanceId: String): Flow<DataState<LarvaVisitModelResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getLarvaSurveillanceVisitsByFilter(
                surveillanceId = surveillanceId,
                authorization = "Bearer " + Util.getHeader(),
                page = 0,
                size = 50
            )
        })
    }

    override suspend fun getLarvaByFilter(
        villageId: String?,
        placeOrgId:String?,
        householdId: String?,
        page: Int,
        size: Int,
        placeType: String?
    ) = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getLarvaByFilter(
                villageId = villageId,
                placeOrgId = placeOrgId,
                householdId = householdId,
                page = 0,
                size = 10,
                placeType =placeType,
                authorization = "Bearer " + Util.getHeader()
            )
        })
    }

    override suspend fun saveSurveillanceVisit(saveSurveillance: SaveSurveillance) = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.saveSurveillanceVisit(
                authorization = "Bearer " + Util.getHeader(),
                saveSurveillance = saveSurveillance,
                surveillanceId = saveSurveillance.larvaSurveillanceId
            )
        })
    }

    override suspend fun getImageUrl(bucketKey: String) = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getImageUrl(
                bucketKey, authorization = "Bearer " + Util.getHeader()
            )
        })

    }

    override suspend fun getPlaceImageUrl(bucketKey: String) = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getPlaceImageUrl(
                bucketKey, authorization = "Bearer " + Util.getHeader()
            )
        })

    }

    override suspend fun createWaterSampleSurveillance(
        waterSampleData: WaterSampleContent, userId: String
    ): Flow<DataState<WaterSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.createWaterSampleSurveillance(
                userId = userId,
                authorization = "Bearer " + Util.getHeader(),
                waterRequest = waterSampleData
            )
        })

    }

    override suspend fun updateWaterSampleSurveillance(
        waterSampleData: WaterSampleContent, userId: String
    ): Flow<DataState<WaterSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.updateWaterSampleSurveillance(
                userId = userId,
                authorization = "Bearer " + Util.getHeader(),
                waterRequest = waterSampleData,
                surveillanceId = waterSampleData.id ?:""
            )
        })

    }

    override suspend fun getWaterSampleByFilter(
        dateOfSurvey: String?,
        villageId: String?,
        placeOrgId: String?,
        page: Int,
        size: Int,
        placeType: String?
    ): Flow<DataState<WaterSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getWaterSampleByFilter(
                villageId = villageId,
                placeOrgId = placeOrgId,
                page = page,
                size = size,
                placeType = placeType,
                token = "Bearer " + Util.getHeader(),
                dateOfSurvey = dateOfSurvey
            )
        })
    }

    override suspend fun getWaterSampleById(
        waterSampleId: String, userId: String
    ): Flow<DataState<WaterSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getWaterSampleById(
                id = waterSampleId, userId = userId, authorization = "Bearer " + Util.getHeader()
            )
        })
    }

    override suspend fun createIodineSurveillance(
        iodineData: CheckIfSurveillanceIsCreated, userId: String
    ): Flow<DataState<CreateSurveillanceResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.createIodineSurveillance(
                token = "Bearer " + Util.getHeader(), data = iodineData, userId = userId
            )
        })
    }

    override suspend fun getIodineSurveillanceByFilter(
        checkIfSurveillanceIsCreated: CheckIfSurveillanceIsCreated,
        userId: String
    ): Flow<DataState<CreateSurveillanceResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getIodineSurveillanceByFilter(
                token = "Bearer " + Util.getHeader(),
                villageId = checkIfSurveillanceIsCreated.villageId,
                placeType = checkIfSurveillanceIsCreated.placeType,
                householdId = checkIfSurveillanceIsCreated.householdId,
                placeOrgId = checkIfSurveillanceIsCreated.placeOrgId,
                page = 0,
                size = 10,
                userId = userId
            )
        })
    }



    override suspend fun createIodineSample(
        iodineSampleData: CreateIodineSample, userId: String
    ): Flow<DataState<IodineSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.createIodineSample(
                userId = userId,
                iodineSurveillanceId = iodineSampleData.iodineSurveillanceId,
                token = "Bearer " + Util.getHeader(),
                CreateIodineSample = iodineSampleData
            )
        })

    }

    override suspend fun updateIodineSample(
        iodineSampleData: CreateIodineSample, userId: String, iodineSampleId: String
    ): Flow<DataState<IodineSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.updateIodineSample(
                userId = userId,
                iodineSurveillanceId = iodineSampleData.iodineSurveillanceId,
                token = "Bearer " + Util.getHeader(),
                CreateIodineSample = iodineSampleData,
                iodineSampleId = iodineSampleId
            )
        })

    }

    override suspend fun getIodineSampleByFilter(
        userId: String, iodineSurveillanceId: String
    ): Flow<DataState<IodineSampleResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getIodineSampleByFilter(
                userId = userId,
                id = iodineSurveillanceId,
                token = "Bearer " + Util.getHeader(),
                size = 10,
                page = 0
            )
        })
    }

    override suspend fun getHouseholdNearVillage(villageId: String): Flow<DataState<HouseHoldResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.getHouseHoldNearVillage(
                    "Village",
                    villageId,
                    "CONTAINEDINPLACE",
                    "HouseHold",
                    0,
                    350,
                    authorization = "Bearer " + Util.getHeader()
                )
            })
        }

    override suspend fun getHouseHoldByName(
        villageLgdCode: String, name: String
    ): Flow<DataState<HouseHoldResponseByName>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getHouseHoldByName(
                name = name,
                authorization = "Bearer " + Util.getHeader(),
                body = HouseHoldByNameRequest(
                    type = "HouseHold", 0, 50, properties = Property(villageLgdCode)
                ),
                type = "Citizen"
            )
        })
    }

    override suspend fun getHouseHoldNearMe(
            villageId: String, latitude: Double, longitude: Double
    ): Flow<DataState<NearByHouseHoldResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getHouseHoldNearMe(
                authorization = "Bearer " + Util.getHeader(), body = HouseHoldByNameRequest(
                    type = "HouseHold",
                    0,
                    350,
                    properties = Property(villageId, latitude, longitude, 10000.0)
                )
            )
        })
    }

    override suspend fun getChildrenInVillage(
        villageId: String,
        requestData: ChildrenRequestData
    ): Flow<DataState<ChildrenResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.getChildrenInVillage(
                    authorization = "Bearer " + Util.getHeader(),
                    body = requestData
                )
            })

        }

    override suspend fun getPreSignedUrl(bucketKey: String): Flow<DataState<PresignedUrl>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getPreSignedUrl(
                token = "Bearer " + Util.getHeader(), bucketKey = bucketKey

            )
        })
    }

    override suspend fun putImageToS3Bucket(
        preSignedUrl: String, contentType: String, body: RequestBody
    ): Flow<DataState<Unit>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.putImageToS3(
                uploadUrl = preSignedUrl, contentType = contentType, body = body

            )
        })
    }

    override suspend fun saveCovidData(saveCovidData: CovidRequestData): Flow<DataState<SaveIndividualDataResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.saveCovidData(
                    requestData = saveCovidData,
                    token = "Bearer " + Util.getHeader()
                )
            })
        }

    override suspend fun saveLeprosyData(requestData: LeprosyRequestData): Flow<DataState<SaveIndividualDataResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.saveLeprosyData(
                    requestData = requestData,
                    token = "Bearer " + Util.getHeader()
                )
            })
        }

    override suspend fun saveUrineData(requestData: UrineRequestData): Flow<DataState<SaveIndividualDataResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.saveUrineData(
                    requestData = requestData,
                    token = "Bearer " + Util.getHeader()
                )
            })
        }

    override suspend fun saveAcfData(requestData: AcfRequestData): Flow<DataState<SaveIndividualDataResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.saveAcfData(
                requestData = requestData,
                token = "Bearer " + Util.getHeader()
            )
        })
    }

    override suspend fun saveBloodSmearData(requestData: BloodSmearRequestData): Flow<DataState<SaveIndividualDataResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.saveBloodSmearData(
                    requestData = requestData,
                    token = "Bearer " + Util.getHeader()
                )
            })
        }

    override suspend fun saveIdspData(requestData: IdspData): Flow<DataState<SaveIndividualDataResponse>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.saveIdspData(
                requestData = requestData,
                token = "Bearer " + Util.getHeader()
            )
        })
    }

    override suspend fun getIndividualSurveillanceData(flowType: String): Flow<DataState<Response>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.getIndividualData(
                    flowType = flowType,
                    authorization = "Bearer " + Util.getHeader(),
                )
            })
        }

    override suspend fun getCitizensInHouse(houseUuid: String): Flow<DataState<IndividualResponse>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.getCitizensInHouseHold(
                    "HouseHold",
                    houseUuid,
                    "RESIDESIN",
                    "Citizen",
                    0,
                    50,
                    authorization = "Bearer " + Util.getHeader()
                )
            })
        }

    override suspend fun updateAcfData(
        requestData: AcfRequestData, surveillanceId: String
    ): Flow<DataState<AcfRequestData>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.updateAcf(
                body = requestData,
                authorization = "Bearer " + Util.getHeader(),
                surveillanceId = surveillanceId
            )
        })
    }

    override suspend fun updateLeprosyData(
        requestData: LeprosyRequestData, surveillanceId: String
    ): Flow<DataState<LeprosyRequestData>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.updateLeprosy(
                body = requestData,
                authorization = "Bearer " + Util.getHeader(),
                surveillanceId = surveillanceId
            )
        })
    }

    override suspend fun updateUrineData(
        requestData: UrineRequestData, surveillanceId: String
    ): Flow<DataState<UrineRequestData>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.updateUrine(
                body = requestData,
                authorization = "Bearer " + Util.getHeader(),
                surveillanceId = surveillanceId
            )
        })
    }

    override suspend fun updateBloodSmearData(
        requestData: BloodSmearUpdateRequestData, surveillanceId: String
    ): Flow<DataState<BloodSmearUpdateRequestData>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.updateBloodSmear(
                body = requestData,
                authorization = "Bearer " + Util.getHeader(),
                surveillanceId = surveillanceId
            )
        })
    }

    override suspend fun getUrineData(citizenId: String): Flow<DataState<GetUrineFilterData>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getUrineData(
                authorization = "Bearer " + Util.getHeader(), citizenId = citizenId
            )
        })
    }


    override suspend fun getLeprosyData(citizenId: String): Flow<DataState<GetLeprosyFilterData>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.getLeprosyData(
                    authorization = "Bearer " + Util.getHeader(), citizenId = citizenId
                )
            })
        }

    override suspend fun getBloodSmearData(citizenId: String): Flow<DataState<GetBloodFilterData>> =
        flow {
            emit(DataState.Loading)
            emit(safeApiRequest.apiRequest {
                apiService.getBloodSmearData(
                    authorization = "Bearer " + Util.getHeader(), citizenId = citizenId
                )
            })
        }

    override suspend fun getAcfData(citizenId: String): Flow<DataState<GetAcfFilterData>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getAcfData(
                authorization = "Bearer " + Util.getHeader(), citizenId = citizenId
            )
        })
    }

    override suspend fun getLabResultStatus(
        url: String, citizenIdList: String
    ): Flow<DataState<List<LabResultStatusResponse>>> = flow {
        emit(DataState.Loading)
        emit(safeApiRequest.apiRequest {
            apiService.getLabResultStatus(
                url=url,authorization = "Bearer " + Util.getHeader(), citizenId = citizenIdList
            )
        })
    }
}

