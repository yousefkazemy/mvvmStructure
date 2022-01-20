package com.example.mvvmstructure.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.data.remote.responses.VideoResponse
import com.example.mvvmstructure.data.repository.product.ProductRepository
import com.example.mvvmstructure.utils.Resource
import com.example.mvvmstructure.utils.Status
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CancellationException

class HomeViewModel @ViewModelInject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> = _products

    private val _recyclerPosition = MutableLiveData<Int>()
    val recyclerPosition: LiveData<Int> = _recyclerPosition

    private var productsArrayList = ArrayList<Product>()

    private lateinit var job: Job

    init {
        fetchAllProducts()
    }

    fun setRecyclerPosition(position: Int) {
        _recyclerPosition.value = position
    }

    /**
     * This method calls 2 api connections(products api(images), videos api).
     * Should handle 2 api connections on different coroutine scopes and merge it with
     * bookmarked items.
     */
    fun fetchAllProducts() {
        if (productsArrayList.size == 0) {
            _products.value = Resource.loading(null)
        }

        val productsList = ArrayList<Product>()
        val videosList = ArrayList<Product>()
        var bookmarkedList: List<ProductEntity> = emptyList()

        /**
         * parent job for handling child jobs like api calls and fetching bookmarked item from db
         * if one child job cancel this parent job, all child will be cancelled
         */
        job = viewModelScope.launch(IO) {
            launch {
                val response = fetchProducts()
                if (response.status == Status.SUCCESS) {
                    productsList.addAll(response.data!!)
                } else {
                    job.cancel(CancellationException("error"))
                }
            }

            launch {
                val response = fetchVideos()
                if (response.status == Status.SUCCESS) {
                    videosList.addAll(
                        response.data!!.videos.map {
                            Product(
                                id = it.id,
                                type = "video",
                                tags = it.tags,
                                previewImageUrl = "",
                                previewImageWidth = 0,
                                previewImageHeight = 0,
                                largeImageURL = "",
                                largeImageWidth = it.videos.tiny.width,
                                largeImageHeight = it.videos.tiny.height,
                                views = it.views,
                                likes = it.likes,
                                userId = it.userId,
                                username = it.username,
                                imageUrl = it.imageUrl,
                                videoUrl = it.videos.tiny.url
                            )
                        }
                    )
                } else {
                    job.cancel(CancellationException("error"))
                }
            }

            launch {
                bookmarkedList = fetchBookmarkedProducts()
            }
        }

        // invokeOnCompletion waits for all jobs to be completed
        job.invokeOnCompletion {
            it?.let {
                // jobs not completed because of exception
                // handle exception
                _products.postValue(Resource.error("An unknown error occurred", null))
            } ?: run {
                // All jobs successfully completed
                productsList.addAll(videosList)
                videosList.clear()
                bookmarkedList.forEach { productEntity ->
                    productsList.forEachIndexed { productIndex, product ->
                        if (product.id == productEntity.id) {
                            product.isBookmarked = true
                            productsList[productIndex] = product
                            return@forEachIndexed
                        }
                    }
                }

                productsArrayList.addAll(productsList)
                _products.postValue(Resource.success(productsArrayList))
            }
        }
    }

    suspend fun fetchProducts(): Resource<List<Product>> {
        return repository.fetchProducts()
    }

    suspend fun fetchVideos(): Resource<VideoResponse> {
        return repository.fetchVideos()
    }

    suspend fun fetchBookmarkedProducts(): List<ProductEntity> {
        return repository.observeAllProducts()
    }

    fun changeProductBookmark(itemPosition: Int) {
        val product = productsArrayList[itemPosition].copy()
        if (product.isBookmarked) {
            deleteProductFromDB(product, itemPosition)
        } else {
            insertProductToDB(product, itemPosition)
        }
    }

    fun insertProductToDB(product: Product, itemPosition: Int) {
        viewModelScope.launch {
            repository.insertProduct(convertProductToProductEntity(product))
            updateProducts(product, itemPosition, true)
        }
    }

    fun deleteProductFromDB(product: Product, itemPosition: Int) {
        viewModelScope.launch {
            repository.deleteProduct(convertProductToProductEntity(product))
            updateProducts(product, itemPosition, false)
        }
    }

    private suspend fun updateProducts(product: Product, itemPosition: Int, isBookmarked: Boolean) {
        withContext(Main) {
            product.isBookmarked = isBookmarked
            productsArrayList[itemPosition] = product
            _products.value = Resource.success(productsArrayList)
        }
    }

    private fun convertProductToProductEntity(product: Product): ProductEntity =
        ProductEntity(
            id = product.id,
            username = product.username,
            imageUrl = product.largeImageURL,
            imageWidth = product.largeImageWidth,
            imageHeight = product.largeImageHeight,
            views = product.views,
            likes = product.likes,
            tags = product.tags
        )
}