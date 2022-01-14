package com.example.mvvmstructure.data.repository

import androidx.lifecycle.LiveData
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.data.remote.responses.VideoResponse
import com.example.mvvmstructure.data.repository.product.ProductRepository
import com.example.mvvmstructure.utils.Resource

class FakeProductRepository : ProductRepository {

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertProduct(product: ProductEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun observeAllProducts(): List<ProductEntity> {
        TODO("Not yet implemented")
    }

    override fun observeBookmarkedProducts(): LiveData<List<ProductEntity>> {
        TODO("Not yet implemented")
    }

    override fun observeProduct(id: Long): LiveData<ProductEntity> {
        TODO("Not yet implemented")
    }


    override suspend fun fetchProducts(): Resource<List<Product>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(
                listOf(
                    createProductData(1),
                    createProductData(2),
                    createProductData(3),
                    createProductData(4),
                    createProductData(5),
                )
            )
        }
    }

    override suspend fun fetchVideos(): Resource<VideoResponse> {
        TODO("Not yet implemented")
    }

    private fun createProductData(id: Long): Product =
        Product(
            id = id,
            type = "image",
            tags = "images product",
            previewImageUrl = "",
            previewImageWidth = 800,
            previewImageHeight = 800,
            largeImageURL = "",
            largeImageWidth = 2400,
            largeImageHeight = 2400,
            views = 1123486,
            likes = 112348,
            userId = id,
            username = "Yousef",
            imageUrl = "",
        )
}