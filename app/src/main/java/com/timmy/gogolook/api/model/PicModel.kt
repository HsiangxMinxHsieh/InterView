package com.timmy.gogolook.api.model
import com.google.gson.annotations.SerializedName


data class PicModel(
    @SerializedName("hits")
    val hits: MutableList<Hit> = mutableListOf()
)

data class Hit(
    @SerializedName("largeImageURL")
    val largeImageURL: String = "",
)