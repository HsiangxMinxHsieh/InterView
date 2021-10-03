package com.timmy.gogolook.viewmodel

import androidx.lifecycle.MutableLiveData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.timmy.gogolook.api.ApiService
import com.timmy.gogolook.api.model.Hit
import com.timmy.gogolook.di.DIRemoteModule
import com.timmy.gogolook.di.constantKeyIsList
import com.timmy.gogolook.di.constantKeySpanCount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import timber.log.Timber
import javax.inject.Inject

class PicRepository @Inject constructor() {

    @Inject
    lateinit var retrofit: Retrofit


    private val apiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val result by lazy { MutableLiveData<MutableList<Hit>>() }

    fun getLiveDataByAPI() = result

    fun getDefaultDataFromAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseBody = apiService.search()

            result.postValue(responseBody.hits)
        }
    }

    fun searchFromAPI(search: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseBody = apiService.search(search)

            result.postValue(responseBody.hits)
        }
    }


    fun getIsListLayout() = DIRemoteModule.liveIsListLayout


    @Inject
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig

    fun getDataFromRemote() {
        val isListLayout = firebaseRemoteConfig.getBoolean(constantKeyIsList)
//        Timber.e("要post的value是=>$isListLayout")
//        Timber.e("收到的數值是=>${firebaseRemoteConfig.getLong(constantKeySpanCount)}")
//        liveIsListLayout.postValue(firebaseRemoteConfig.getBoolean(constantKeyIsList))
    }

}
