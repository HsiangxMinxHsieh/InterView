package com.timmy.gogolook.viewmodel

import android.view.View
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class PicViewModel @ViewModelInject constructor(private val picRepository: PicRepository) : ViewModel() {

    val liveLoadingOver: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() } // According this value To Show now Status

    fun getData() {
        liveLoadingOver.value = false
        picRepository.init()
        picRepository.getDataFromAPI()
    }

    fun getLiveDataByAPI() = picRepository.getLiveDataByAPI()


}