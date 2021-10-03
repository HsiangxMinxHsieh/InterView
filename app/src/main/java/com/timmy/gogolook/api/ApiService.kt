package com.timmy.gogolook.api

import com.timmy.gogolook.api.model.PicModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(".")
    suspend fun getDefalutData(): PicModel

    @GET(".")
    suspend fun search( @Query("q") search: String? = "" ): PicModel

}