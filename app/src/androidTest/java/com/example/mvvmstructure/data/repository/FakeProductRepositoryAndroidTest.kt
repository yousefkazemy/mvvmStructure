package com.example.mvvmstructure.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.data.model.Video
import com.example.mvvmstructure.data.model.VideoResult
import com.example.mvvmstructure.data.remote.responses.VideoResponse
import com.example.mvvmstructure.data.repository.product.ProductRepository
import com.example.mvvmstructure.utils.Resource

class FakeProductRepositoryAndroidTest : ProductRepository {

    private val productItems = mutableListOf<ProductEntity>()

    private val observableProductItems = MutableLiveData<List<ProductEntity>>(productItems)
    private val observableProductItem = MutableLiveData<ProductEntity>()

    private var shouldReturnNetworkError = false
    private var remoteProductItems: List<Product> = emptyList()
    private var remoteVideosItems: List<VideoResult> = emptyList()

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    fun setRemoteProductItems(remoteProductItems: List<Product>) {
        this.remoteProductItems = remoteProductItems
    }

    fun setRemoteVideosItems(remoteVideosItems: List<VideoResult>) {
        this.remoteVideosItems = remoteVideosItems
    }

    override suspend fun insertProduct(product: ProductEntity) {
        productItems.add(product)
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        productItems.remove(product)
    }

    override suspend fun observeAllProducts(): List<ProductEntity> {
        return productItems
    }

    override fun observeBookmarkedProducts(): LiveData<List<ProductEntity>> {
        return observableProductItems
    }

    override fun observeProduct(id: Long): LiveData<ProductEntity> {
        val product = productItems.filter {
            return@filter it.id == id
        }
        observableProductItem.postValue(product[0])
        return observableProductItem
    }


    override suspend fun fetchProducts(): Resource<List<Product>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(remoteProductItems)
        }
    }

    override suspend fun fetchVideos(): Resource<VideoResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(
                VideoResponse(
                    totalVideos = 5,
                    videos = remoteVideosItems
                )
            )
        }
    }

    fun createProductData(id: Long, isBookmarked: Boolean = false): Product =
        Product(
            id = id,
            type = "image",
            tags = "images product",
            previewImageUrl = "",
            previewImageWidth = 800,
            previewImageHeight = 800,
            largeImageURL = "",
            largeImageWidth = 800,
            largeImageHeight = 800,
            views = 1123486,
            likes = 112348,
            userId = id,
            username = "Yousef",
            imageUrl = "",
            isBookmarked = isBookmarked
        )

    fun createVideoData(id: Long): VideoResult =
        VideoResult(
            id = id,
            type = "image",
            tags = "images product",
            views = 1123486,
            likes = 112348,
            userId = id,
            username = "Yousef",
            imageUrl = "",
            videos = VideoResult.Videos(
                large = Video("", 0, 0, 0),
                medium = Video("", 0, 0, 0),
                small = Video("", 0, 0, 0),
                tiny = Video("", 0, 0, 0)
            )
        )
}