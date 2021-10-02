package com.timmy.hiltmvvm.api

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Accept: application/json")
    @GET("v2/top-headlines")
    suspend fun getNews(@Query("country") countryCode: String = "us", @Query("apiKey") key: String = "6c7cda920b034282ae2b5abda6904fcc"): SampleDataFromAPI

}