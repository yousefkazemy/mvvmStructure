package com.example.mvvmstructure.data.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("user_id")
    val userId: Long,

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("user")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("userImageURL")
    val imageUrl: String
)