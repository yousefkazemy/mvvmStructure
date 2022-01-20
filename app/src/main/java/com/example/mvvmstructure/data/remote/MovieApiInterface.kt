package com.example.mvvmstructure.data.remote

import com.example.mvvmstructure.data.remote.responses.ProductResponse
import com.example.mvvmstructure.data.remote.responses.VideoResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiInterface {

    @GET(Endpoints.PRODUCTS)
    suspend fun getProducts(
        @Query("key") apiKey: String = Endpoints.API_KEY,
        @Query("page") page: Int = 1
    ): Response<ProductResponse>

    @GET(Endpoints.VIDEOS)
    suspend fun getVideos(
        @Query("key") apiKey: String = Endpoints.API_KEY,
        @Query("page") page: Int = 1
    ): Response<VideoResponse>

    @GET(Endpoints.PRODUCTS)
    fun searchForProducts(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = Endpoints.API_KEY,
        @Query("page") page: Int = 1
    ): Single<ProductResponse>
}