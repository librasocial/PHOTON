package com.ssf.homevisit.models

import androidx.lifecycle.MutableLiveData

data class SpecialInvitee(
    val name: MutableLiveData<String>,
    val emailId: MutableLiveData<String>,
    val phoneNumber: MutableLiveData<String>
)
