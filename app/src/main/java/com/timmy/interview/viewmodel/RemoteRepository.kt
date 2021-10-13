package com.timmy.interview.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.timmy.interview.di.*
import timber.log.Timber
import javax.inject.Inject

/** 跟Firebase RemoteConfig Repository(挑戰1) */
class RemoteRepository @Inject constructor() {

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    private val liveIsListLayout = MutableLiveData<Boolean>()

    fun getLiveLLayoutType() = liveIsListLayout //因為viewModel要改這個值，所以不能回傳LiveData

    private val liveGridRowsCount = MutableLiveData<Int>()

    fun getLiveLGridLayoutCount() = liveGridRowsCount  //因為viewModel「有可能」要改這個值，所以不能回傳LiveData

    fun fetchConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                liveIsListLayout.postValue(remoteConfig.getBoolean(constantKeyIsList))
                liveGridRowsCount.postValue(remoteConfig.getLong(constantKeySpanCount).toInt())
            }
        }
    }

}
