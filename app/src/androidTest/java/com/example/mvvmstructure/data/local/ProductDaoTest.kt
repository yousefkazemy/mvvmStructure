package com.example.mvvmstructure.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.mvvmstructure.data.model.ProductEntity
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ProductDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: ProductsDatabase
    private lateinit var dao: ProductDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.productDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertProductsItem() = runBlockingTest {
        val productEntity = ProductEntity(
            id = 1,
            username = "yousef",
            imageUrl = "imageUrl",
            views = 123456,
            likes = 21452,
            tags = "image, nature"
        )
        dao.insertProduct(productEntity)

        val allShoppingItems = dao.observeAllProducts()
        assertThat(allShoppingItems).contains(productEntity)
    }

    @Test
    fun deleteShoppingItem() = runBlockingTest {
        val productEntity = ProductEntity(
            id = 1,
            username = "yousef",
            imageUrl = "imageUrl",
            views = 123456,
            likes = 21452,
            tags = "image, nature"
        )
        dao.insertProduct(productEntity)
        dao.deleteProduct(productEntity)

        val allShoppingItems = dao.observeAllProducts()

        assertThat(allShoppingItems).doesNotContain(productEntity)
    }
}