package com.ssf.homevisit.features.hnwvillage.data

interface IHnWRepository {
    fun getHnWVillageLevelData()
    fun getHnWSurveillanceType(): List<SurveillanceDataType>
}