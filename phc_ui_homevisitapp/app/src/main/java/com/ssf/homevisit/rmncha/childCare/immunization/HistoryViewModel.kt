package com.ssf.homevisit.rmncha.childCare.immunization

import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.models.ChildMotherDetailResponse
import com.ssf.homevisit.models.RMNCHACoupleDetailsRequest
import com.ssf.homevisit.models.RMNCHACoupleDetailsResponse
import com.ssf.homevisit.repository.VillageLevelMappingRepository
import com.ssf.homevisit.requestmanager.ApiClient
import com.ssf.homevisit.requestmanager.ApiInterface
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryViewModel() : ViewModel() {

    val apiInterface: ApiInterface = ApiClient.getClient(AppDefines.BASE_URL).create<ApiInterface?>(ApiInterface::class.java)
    private val villageLevelMappingRepository = VillageLevelMappingRepository()
    lateinit var childMotherDetail: ChildMotherDetailResponse
    var coupleData = MutableLiveData<List<RMNCHACoupleDetailsResponse.Content>?>()

    fun getHistory(childId: String): MutableLiveData<LinkedHashMap<String, List<Immunization>>> {
        return villageLevelMappingRepository.getChildImmunizationHistory(childId)
    }


    fun getRMNCHACoupleDetailsLiveData(uuid: String?): MutableLiveData<List<RMNCHACoupleDetailsResponse.Content>?> {
        apiInterface.getRMNCHACoupleDetailsRequest(
            RMNCHACoupleDetailsRequest(uuid),
            Util.getHeader()
        ).enqueue(object : Callback<RMNCHACoupleDetailsResponse?> {
            override fun onResponse(
                call: Call<RMNCHACoupleDetailsResponse?>,
                response: Response<RMNCHACoupleDetailsResponse?>
            ) {
                AppController.getInstance().mainActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                if (response.code() == 200) {
                    val responseData = response.body()
                    if (responseData != null) {
                        coupleData.setValue(responseData.content)
                    } else {
                        coupleData.setValue(null)
                    }
                } else {
                    coupleData.setValue(null)
                }
            }

            override fun onFailure(call: Call<RMNCHACoupleDetailsResponse?>, t: Throwable) {
                coupleData.value = null
            }
        })
        return coupleData
    }


}