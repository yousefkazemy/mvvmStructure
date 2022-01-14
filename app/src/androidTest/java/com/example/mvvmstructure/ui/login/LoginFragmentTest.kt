package com.example.mvvmstructure.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.example.mvvmstructure.R
import com.example.mvvmstructure.data.repository.FakeLoginRepositoryAndroidTest
import com.example.mvvmstructure.getOrAwaitValue
import com.example.mvvmstructure.launchFragmentInHiltContainer
import com.example.mvvmstructure.utils.Status
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
class LoginFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var loginRepository: FakeLoginRepositoryAndroidTest

    @Before
    fun setup() {
        hiltRule.inject()
        loginRepository = FakeLoginRepositoryAndroidTest()
        loginRepository.setExpectedEmail("yousefkazemi1@gmail.com")
        loginRepository.setExpectedPassword("123456")
    }

    @Test
    fun clickLoginButton_shouldSucceedLogin() {
        val testViewModel = LoginViewModel(loginRepository)
        launchFragmentInHiltContainer<LoginFragment> {
            viewModel = testViewModel
        }

        onView(withId(R.id.username)).perform(replaceText("yousefkazemi1@gmail.com"))
        onView(withId(R.id.password)).perform(replaceText("123456"))
        onView(withId(R.id.login)).perform(click())

        assertThat(
            testViewModel.loginStatus.getOrAwaitValue().getContentIfNotHandled()!!.status
        ).isEqualTo(
            Status.SUCCESS
        )
    }

    @Test
    fun clickLoginButton_shouldNotSucceedLogin() {
        val testViewModel = LoginViewModel(loginRepository)
        launchFragmentInHiltContainer<LoginFragment> {
            viewModel = testViewModel
        }

        onView(withId(R.id.username)).perform(replaceText("yousefkazemy@gmail.com"))
        onView(withId(R.id.password)).perform(replaceText("123456"))
        onView(withId(R.id.login)).perform(click())

        assertThat(
            testViewModel.loginStatus.getOrAwaitValue().getContentIfNotHandled()!!.status
        ).isEqualTo(
            Status.ERROR
        )
    }
}