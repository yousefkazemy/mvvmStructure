package com.example.mvvmstructure.data.repository.product

import androidx.lifecycle.LiveData
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.data.remote.responses.VideoResponse
import com.example.mvvmstructure.utils.Resource

interface ProductRepository {

    suspend fun insertProduct(product: ProductEntity)

    suspend fun deleteProduct(product: ProductEntity)

    suspend fun observeAllProducts(): List<ProductEntity>

    fun observeBookmarkedProducts(): LiveData<List<ProductEntity>>

    fun observeProduct(id: Long): LiveData<ProductEntity>

    suspend fun fetchProducts(): Resource<List<Product>>

    suspend fun fetchVideos(): Resource<VideoResponse>
}