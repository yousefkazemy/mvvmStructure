package com.example.mvvmstructure.data.remote.responses

import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.model.VideoResult
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class VideoResponse(
    @Expose
    @SerializedName("totalHits")
    val totalVideos: Long,

    @Expose
    @SerializedName("hits")
    val videos: List<VideoResult>
)