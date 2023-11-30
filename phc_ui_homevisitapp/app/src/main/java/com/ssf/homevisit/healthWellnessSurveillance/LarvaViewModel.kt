package com.ssf.homevisit.healthWellnessSurveillance

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LarvaViewModel @Inject constructor() : ViewModel() {

     lateinit var flowType:String
     lateinit var villageName:String
     lateinit var userName:String
     lateinit var userId:String
     lateinit var villageLgdCode:String
     lateinit var villageUuid:String
     lateinit var houseHoldId:String

}