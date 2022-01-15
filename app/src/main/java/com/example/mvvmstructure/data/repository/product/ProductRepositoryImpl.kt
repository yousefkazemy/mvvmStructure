package com.example.mvvmstructure.data.repository.product

import androidx.lifecycle.LiveData
import com.example.mvvmstructure.data.local.ProductDao
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.data.remote.MovieApiInterface
import com.example.mvvmstructure.data.remote.responses.VideoResponse
import com.example.mvvmstructure.utils.Resource
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val movieApiInterface: MovieApiInterface,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun insertProduct(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    override suspend fun observeAllProducts(): List<ProductEntity> {
        return productDao.observeAllProducts()
    }

    override fun observeBookmarkedProducts(): LiveData<List<ProductEntity>> {
        return productDao.observeBookmarkedProducts()
    }

    override fun observeProduct(id: Long): LiveData<ProductEntity> {
        return productDao.observeProduct(id)
    }

    override suspend fun fetchProducts(): Resource<List<Product>> {
        //TODO ==> Errors messages should be replaced with strings resources id or general error
        // types to handle showing errors to users with all languages supported
        return try {
            val response = movieApiInterface.getProducts()
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.success(it.products)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server, Check your internet connection", null)
        }
    }

    override suspend fun fetchVideos(): Resource<VideoResponse> {
        //TODO ==> Errors messages should be replaced with strings resources id or general error
        // types to handle showing errors to users with all languages supported
        return try {
            val response = movieApiInterface.getVideos()
            if (response.isSuccessful) {
                response.body()?.let {
                    Resource.success(it)
                } ?: Resource.error("An unknown error occurred", null)
            } else {
                Resource.error("An unknown error occurred", null)
            }
        } catch (e: Exception) {
            Resource.error("Couldn't reach the server, Check your internet connection", null)
        }
    }
}