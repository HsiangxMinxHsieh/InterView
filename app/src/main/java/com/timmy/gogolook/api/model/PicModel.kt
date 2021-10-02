package com.timmy.gogolook.api.model
import com.google.gson.annotations.SerializedName


data class PicModel(
    @SerializedName("hits")
    val hits: MutableList<Hit> = mutableListOf(),
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("totalHits")
    val totalHits: Int = 0
)

data class Hit(
    @SerializedName("collections")
    val collections: Int = 0,
    @SerializedName("comments")
    val comments: Int = 0,
    @SerializedName("downloads")
    val downloads: Int = 0,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("imageHeight")
    val imageHeight: Int = 0,
    @SerializedName("imageSize")
    val imageSize: Int = 0,
    @SerializedName("imageWidth")
    val imageWidth: Int = 0,
    @SerializedName("largeImageURL")
    val largeImageURL: String = "",
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("pageURL")
    val pageURL: String = "",
    @SerializedName("previewHeight")
    val previewHeight: Int = 0,
    @SerializedName("previewURL")
    val previewURL: String = "",
    @SerializedName("previewWidth")
    val previewWidth: Int = 0,
    @SerializedName("tags")
    val tags: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("user")
    val user: String = "",
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("userImageURL")
    val userImageURL: String = "",
    @SerializedName("views")
    val views: Int = 0,
    @SerializedName("webformatHeight")
    val webformatHeight: Int = 0,
    @SerializedName("webformatURL")
    val webformatURL: String = "",
    @SerializedName("webformatWidth")
    val webformatWidth: Int = 0
)