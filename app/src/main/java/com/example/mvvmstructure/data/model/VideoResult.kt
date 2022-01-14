package com.example.mvvmstructure.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoResult(
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

    @Expose
    @SerializedName("videos")
    val videos: Videos
) {
    data class Videos(
        @Expose
        @SerializedName("large")
        val large: Video,

        @Expose
        @SerializedName("medium")
        val medium: Video,

        @Expose
        @SerializedName("small")
        val small: Video,

        @Expose
        @SerializedName("tiny")
        val tiny: Video,
    )
}