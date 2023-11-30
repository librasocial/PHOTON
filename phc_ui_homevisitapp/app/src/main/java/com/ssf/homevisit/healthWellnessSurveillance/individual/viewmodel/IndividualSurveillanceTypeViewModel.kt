package com.ssf.homevisit.healthWellnessSurveillance.individual.viewmodel

import androidx.lifecycle.ViewModel
import com.ssf.homevisit.healthWellnessSurveillance.data.repoInterface.IGetSurveillance
import com.ssf.homevisit.healthWellnessSurveillance.ui.village.models.Asset
import com.ssf.homevisit.requestmanager.AppDefines
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IndividualSurveillanceTypeViewModel @Inject constructor(private val repo: IGetSurveillance) :
    ViewModel() {


    var surveillanceTypeData: MutableList<Asset> = mutableListOf()

    fun getSurveillanceTypeData() {
        surveillanceTypeData = mutableListOf()
        surveillanceTypeData.add(
            Asset(
                false,
                AppDefines.IDSP_S_Form,
            )
        )
        surveillanceTypeData.add(
            Asset(
                false,
                AppDefines.ACTIVE_CASE_FINDING,
            )
        )
        surveillanceTypeData.add(
            Asset(
                false,
                AppDefines.BLOOD_SMEAR,
            )
        )
        surveillanceTypeData.add(
            Asset(
                false,
                AppDefines.URINE_SAMPLE,
            )
        )
        surveillanceTypeData.add(
            Asset(
                false,
                AppDefines.LEPROSY,
            )
        )
        surveillanceTypeData.add(
            Asset(
                false,
                AppDefines.COVID,
            )
        )
    }

}