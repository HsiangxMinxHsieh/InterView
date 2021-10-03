package com.timmy.gogolook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.timmy.gogolook.di.*
import javax.inject.Inject

/**跟Firebase RemoteConfig Repository(挑戰1)*/
class RemoteRepository @Inject constructor() {

    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    private val liveIsListLayout = MutableLiveData<Boolean>()

    fun getLiveLLayoutType() = liveIsListLayout // 因為viewModel要改這個值，所以不能回傳LiveData

    private val liveGridRowsCount = MutableLiveData<Int>()

    fun getLiveLGridLayoutCount(): LiveData<Int> = liveGridRowsCount

    fun fetchConfig() {
        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                liveIsListLayout.postValue(remoteConfig.getBoolean(constantKeyIsList))
                liveGridRowsCount.postValue(remoteConfig.getLong(constantKeySpanCount).toInt())
            }
        }
    }

}
