package com.timmy.gogolook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.timmy.gogolook.api.ApiService
import com.timmy.gogolook.api.model.Hit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

/**跟API溝通的Repository*/
class APIRepository @Inject constructor() {

    @Inject
    lateinit var retrofit: Retrofit

    private val apiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    private val result by lazy { MutableLiveData<MutableList<Hit>>() }

    fun getLiveDataByAPI(): LiveData<MutableList<Hit>> = result

    fun getDefaultDataFromAPI() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseBody = apiService.getDefalutData()

            result.postValue(responseBody.hits)
        }
    }

    fun searchFromAPI(search: String?) {
        CoroutineScope(Dispatchers.IO).launch {
            val responseBody = apiService.search(search)

            result.postValue(responseBody.hits)
        }
    }

}
