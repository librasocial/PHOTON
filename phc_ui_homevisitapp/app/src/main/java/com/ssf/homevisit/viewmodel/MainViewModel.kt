package com.ssf.homevisit.viewmodel

import androidx.lifecycle.ViewModel
import com.ssf.homevisit.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun logOut() {
        mainRepository.clearLoginAuth()
    }

}