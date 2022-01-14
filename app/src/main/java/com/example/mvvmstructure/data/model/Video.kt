package com.example.mvvmstructure.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Video(
    @Expose
    @SerializedName("url")
    val url: String,

    @Expose
    @SerializedName("width")
    val width: Int,

    @Expose
    @SerializedName("height")
    val height: Int,

    @Expose
    @SerializedName("size")
    val size: Long
)