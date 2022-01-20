package com.example.mvvmstructure.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmstructure.data.repository.FakeProductRepository
import com.example.mvvmstructure.ui.MainCoroutineRule
import com.example.mvvmstructure.ui.getOrAwaitValue
import com.example.mvvmstructure.utils.Status
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel

    private lateinit var repository: FakeProductRepository

    @Before
    fun setUp() {
        repository = FakeProductRepository()
        viewModel = HomeViewModel(repository)
    }

    @Test
    fun `get products successfully`() = runBlockingTest {
        repository.setRemoteProductItems(
            listOf(
                repository.createProductData(1),
                repository.createProductData(2),
                repository.createProductData(3),
                repository.createProductData(4),
            )
        )
        val products = viewModel.fetchProducts()
        assertThat(products.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `get products with error`() = runBlockingTest {
        repository.setShouldReturnNetworkError(true)
        val products = viewModel.fetchProducts()
        assertThat(products.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `get videos successfully`() = runBlockingTest {
        repository.setRemoteVideosItems(
            listOf(
                repository.createVideoData(1),
                repository.createVideoData(2),
                repository.createVideoData(3),
                repository.createVideoData(4),
            )
        )
        val videos = viewModel.fetchVideos()
        assertThat(videos.status).isEqualTo(Status.SUCCESS)
    }

    @Test
    fun `get videos with error`() = runBlockingTest {
        repository.setShouldReturnNetworkError(true)
        val videos = viewModel.fetchVideos()
        assertThat(videos.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `get all products successfully`() {
        repository.setRemoteProductItems(
            listOf(
                repository.createProductData(1),
                repository.createProductData(2),
                repository.createProductData(3),
                repository.createProductData(4),
            )
        )
        repository.setRemoteVideosItems(
            listOf(
                repository.createVideoData(1),
                repository.createVideoData(2),
                repository.createVideoData(3),
                repository.createVideoData(4),
            )
        )
        viewModel.fetchAllProducts()
        val products = viewModel.products.getOrAwaitValue()
        assertThat(products.data!!.size).isEqualTo(8)
    }

    @Test
    fun `get all products with error`() {
        repository.setShouldReturnNetworkError(true)
        viewModel.fetchAllProducts()
        val products = viewModel.products.getOrAwaitValue()
        assertThat(products.data!!.size).isEqualTo(0)
    }

    @Test
    fun `insert product to db`() = runBlockingTest {
        repository.setRemoteProductItems(
            listOf(
                repository.createProductData(1),
                repository.createProductData(2),
                repository.createProductData(3),
                repository.createProductData(4),
            )
        )

        viewModel.fetchAllProducts()
        val products = viewModel.products.getOrAwaitValue().data!!
        viewModel.insertProductToDB(products[3], 3)

        val value = viewModel.fetchBookmarkedProducts()
        assertThat(value.filter { it.id == products[3].id }).isNotEmpty()
    }

    @Test
    fun `delete product from db`() = runBlockingTest {
        repository.setRemoteProductItems(
            listOf(
                repository.createProductData(1),
                repository.createProductData(2),
                repository.createProductData(3),
                repository.createProductData(4),
            )
        )

        viewModel.fetchAllProducts()
        val products = viewModel.products.getOrAwaitValue().data!!
        viewModel.insertProductToDB(products[3], 3)
        viewModel.deleteProductFromDB(products[3], 3)

        val value = viewModel.fetchBookmarkedProducts()
        assertThat(value.filter { it.id == products[3].id }).isEmpty()
    }

    @Test
    fun `change product item 3 to bookmark`() = runBlockingTest {
        repository.setRemoteProductItems(
            listOf(
                repository.createProductData(1),
                repository.createProductData(2),
                repository.createProductData(3),
                repository.createProductData(4),
            )
        )

        viewModel.fetchAllProducts()
        viewModel.changeProductBookmark(3)

        val products = viewModel.products.getOrAwaitValue().data!!
        assertThat(products[3].isBookmarked).isEqualTo(true)
    }

    @Test
    fun `change product item 3 to unBookmark`() = runBlockingTest {
        repository.setRemoteProductItems(
            listOf(
                repository.createProductData(1),
                repository.createProductData(2),
                repository.createProductData(3),
                repository.createProductData(4, isBookmarked = true),
            )
        )

        viewModel.fetchAllProducts()
        viewModel.changeProductBookmark(3)

        val products = viewModel.products.getOrAwaitValue().data!!
        assertThat(products[3].isBookmarked).isEqualTo(false)
    }
}