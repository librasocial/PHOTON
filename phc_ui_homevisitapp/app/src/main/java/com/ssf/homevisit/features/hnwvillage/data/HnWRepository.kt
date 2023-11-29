package com.ssf.homevisit.features.hnwvillage.data

import com.ssf.homevisit.R
import com.ssf.homevisit.requestmanager.ApiInterface
import javax.inject.Inject


class HnWRepository @Inject constructor(apiInterface: ApiInterface) : IHnWRepository {

    override fun getHnWVillageLevelData() {
        TODO("Not yet implemented")
    }

    override fun getHnWSurveillanceType(): List<SurveillanceDataType> {
        return listOf<SurveillanceDataType>(
            SurveillanceDataType("Temple", R.drawable.templeicon),
            SurveillanceDataType("Temple", R.drawable.templeicon),
            SurveillanceDataType("Temple", R.drawable.templeicon),
            SurveillanceDataType("Temple", R.drawable.templeicon)
        )
    }
}