package com.ssf.homevisit.repository

import android.util.Log
import android.view.WindowManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import com.ssf.homevisit.controller.AppController
import com.ssf.homevisit.models.*
import com.ssf.homevisit.repository.MeetingRepository
import com.ssf.homevisit.requestmanager.ApiClient
import com.ssf.homevisit.requestmanager.ApiInterface
import com.ssf.homevisit.requestmanager.AppDefines
import com.ssf.homevisit.utils.AnalyticsEvents
import com.ssf.homevisit.utils.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MeetingRepository {
    private val apiInterface: ApiInterface =
        ApiClient.getClient(AppDefines.BASE_URL).create(ApiInterface::class.java)
    private val meetingData = MutableLiveData<Meeting?>()
    private val meetings = MutableLiveData<MeetingListResponse?>()

    fun postMeeting(
        meeting: Meeting
    ): LiveData<Meeting?> {
        AppController.getInstance().currentActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        apiInterface.postMeeting(
            meeting,
            Util.getHeader()
        ).enqueue(object : Callback<Meeting> {
            override fun onResponse(
                call: Call<Meeting>,
                response: Response<Meeting>
            ) {
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Log.d(TAG, "onResponse response:: $response")
                if (response.code() == 200) {
                    if (response.body() != null) {
                        meetingData.value = response.body()
                    }
                } else {
                    meetingData.value = null
                }
            }

            override fun onFailure(call: Call<Meeting>, t: Throwable) {
                Log.d("Post Meeting Error", t.message!!)
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                meetingData.value = null
            }
        })
        return meetingData
    }

    fun updateMeeting(
        id: String,
        updateMeeting: Any
    ): LiveData<Meeting?> {
        AppController.getInstance().currentActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        apiInterface.updateMeeting(
            id,
            updateMeeting,
            Util.getHeader()
        ).enqueue(object : Callback<Meeting> {
            override fun onResponse(
                call: Call<Meeting>,
                response: Response<Meeting>
            ) {
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Log.d(TAG, "onResponse response:: $response")
                if (response.code() == 200) {
                    if (response.body() != null) {
                        meetingData.value = response.body()
                    }
                } else {
                    meetingData.value = null
                }
            }

            override fun onFailure(call: Call<Meeting>, t: Throwable) {
                Log.d("Post Meeting Error", t.message!!)
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                meetingData.value = null
            }
        })
        return meetingData
    }

    fun getMeeting(
        meetingId: String
    ): LiveData<Meeting?> {
        AppController.getInstance().currentActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        apiInterface.getMeetingById(
            meetingId,
            Util.getHeader()
        ).enqueue(object : Callback<Meeting> {
            override fun onResponse(
                call: Call<Meeting>,
                response: Response<Meeting>
            ) {
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Log.d(TAG, "onResponse response:: $response")
                if (response.code() == 200) {
                    if (response.body() != null) {
                        meetingData.value = response.body()
                    }
                } else {
                    meetingData.value = null
                }
            }

            override fun onFailure(call: Call<Meeting>, t: Throwable) {
                Log.d("Post Meeting Error", t.message!!)
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                meetingData.value = null
            }
        })
        return meetingData
    }

    fun getMeetings(page:Int, size:Int, programType: String): LiveData<MeetingListResponse?> {
        AppController.getInstance().currentActivity.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        apiInterface.getMeetings(
            page,
            size,
            programType,
            Util.getHeader()
        ).enqueue(object : Callback<MeetingListResponse> {
            override fun onResponse(
                call: Call<MeetingListResponse>,
                response: Response<MeetingListResponse>
            ) {
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                Log.d(TAG, "onResponse response:: $response")
                if (response.code() == 200) {
                    if (response.body() != null) {
                        meetings.value = response.body()
                    }
                } else {
                    meetings.value = null
                }
            }

            override fun onFailure(call: Call<MeetingListResponse>, t: Throwable) {
                Log.d("Post Meeting Error", t.message!!)
                AppController.getInstance().currentActivity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                meetings.value = null
            }
        })
        return meetings
    }

    companion object {
        private val TAG = MeetingRepository::class.java.simpleName
    }

}