package com.timmy.gogolook.api

import com.timmy.gogolook.api.model.PicModel
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiService {

    @Headers("Accept: application/json")
    @GET("api")
    suspend fun getData( @Query("key") key: String = "23663801-007fbd680d72d2053f6a32276"): PicModel

}