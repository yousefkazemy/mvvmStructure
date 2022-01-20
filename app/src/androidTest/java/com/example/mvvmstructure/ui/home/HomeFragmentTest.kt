package com.example.mvvmstructure.ui.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.MediumTest
import com.example.mvvmstructure.R
import com.example.mvvmstructure.data.repository.FakeProductRepositoryAndroidTest
import com.example.mvvmstructure.getOrAwaitValue
import com.example.mvvmstructure.launchFragmentInHiltContainer
import com.example.mvvmstructure.ui.RecyclerViewMatcher
import com.example.mvvmstructure.ui.clickChildViewWithId
import com.example.mvvmstructure.ui.home.adapters.ProductAdapter
import com.example.mvvmstructure.ui.withDrawable
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class HomeFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: FakeProductRepositoryAndroidTest

    @Before
    fun setup() {
        hiltRule.inject()
        repository = FakeProductRepositoryAndroidTest()
    }

    @Test
    fun checkRecyclerViewItem_loadUsernameCorrectly() {
        val productList = listOf(
            repository.createProductData(1)
        )
        launchFragmentInHiltContainer<HomeFragment> {
            productAdapter.setData(productList)
        }

        onView(
            RecyclerViewMatcher(R.id.recycler_products)
                .atPositionOnView(0, R.id.text_user_name)
        )
            .check(matches(withText(productList[0].username)))
    }

    @Test
    fun checkRecyclerViewItem_loadViewsCorrectly() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val productList = listOf(
            repository.createProductData(1)
        )
        launchFragmentInHiltContainer<HomeFragment> {
            productAdapter.setData(productList)
        }

        onView(
            RecyclerViewMatcher(R.id.recycler_products)
                .atPositionOnView(0, R.id.text_views_count)
        )
            .check(matches(withText("${productList[0].views} ${context.getString(R.string.views)}")))
    }

    @Test
    fun checkRecyclerViewItem_loadDescriptionCorrectly() {
        val productList = listOf(
            repository.createProductData(1)
        )
        launchFragmentInHiltContainer<HomeFragment> {
            productAdapter.setData(productList)
        }

        onView(
            RecyclerViewMatcher(R.id.recycler_products)
                .atPositionOnView(0, R.id.text_description)
        )
            .check(matches(withText(productList[0].tags)))
    }

    @Test
    fun clickOnItemBookmarkImageView_bookmarkItem() {
        repository.setRemoteProductItems(listOf(repository.createProductData(1)))
        val testViewModel = HomeViewModel(repository)
        testViewModel.fetchAllProducts()

        launchFragmentInHiltContainer<HomeFragment> {
            productAdapter.setData(listOf(repository.createProductData(1)))
            viewModel = testViewModel
        }

        onView(withId(R.id.recycler_products)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ProductViewHolder>(
                0,
                clickChildViewWithId(R.id.image_bookmark)
            )
        )

        val value = testViewModel.products.getOrAwaitValue().data!!
        assertThat(value[0].isBookmarked).isEqualTo(true)

    }

    @Test
    fun clickOnItemBookmarkImageView_unBookmarkItem() {
        repository.setRemoteProductItems(
            listOf(repository.createProductData(1, isBookmarked = true))
        )
        val testViewModel = HomeViewModel(repository)
        testViewModel.fetchAllProducts()

        launchFragmentInHiltContainer<HomeFragment> {
            productAdapter.setData(listOf(repository.createProductData(1, isBookmarked = true)))
            viewModel = testViewModel
        }

        onView(withId(R.id.recycler_products)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ProductAdapter.ProductViewHolder>(
                0,
                clickChildViewWithId(R.id.image_bookmark)
            )
        )

        val value = testViewModel.products.getOrAwaitValue().data!!
        assertThat(value[0].isBookmarked).isEqualTo(false)

    }
}