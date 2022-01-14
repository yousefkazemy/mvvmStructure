package com.example.mvvmstructure.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
    @Expose
    @SerializedName("id")
    val id: Long,

    @Expose
    @SerializedName("type")
    val type: String,

    @Expose
    @SerializedName("tags")
    val tags: String,

    @Expose
    @SerializedName("previewURL")
    val previewImageUrl: String,

    @Expose
    @SerializedName("previewWidth")
    val previewImageWidth: Int,

    @Expose
    @SerializedName("previewHeight")
    val previewImageHeight: Int,

    @Expose
    @SerializedName("largeImageURL")
    val largeImageURL: String,

    @Expose
    @SerializedName("imageWidth")
    val largeImageWidth: Int,

    @Expose
    @SerializedName("imageHeight")
    val largeImageHeight: Int,

    @Expose
    @SerializedName("views")
    val views: Long,

    @Expose
    @SerializedName("likes")
    val likes: Long,

    @Expose
    @SerializedName("user_id")
    val userId: Long,

    @Expose
    @SerializedName("user")
    val username: String,

    @Expose
    @SerializedName("userImageURL")
    val imageUrl: String,

    var videoUrl: String? = null,

    var isBookmarked: Boolean = false
)