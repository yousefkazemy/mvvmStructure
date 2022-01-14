package com.example.mvvmstructure.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvmstructure.data.repository.FakeLoginRepository
import com.example.mvvmstructure.ui.MainCoroutineRule
import com.example.mvvmstructure.ui.getOrAwaitValue
import com.example.mvvmstructure.utils.Status
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    private lateinit var viewModel: LoginViewModel

    private lateinit var loginRepository: FakeLoginRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp() {
        loginRepository = FakeLoginRepository()
        viewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `incorrect email and password return false`() {
        loginRepository.setExpectedEmail("yousefkazemi1@gmail.com")
        loginRepository.setExpectedPassword("123456")
        viewModel.login("yousefkazemi@gmail.com", "12345")

        val value = viewModel.loginStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `correct email and password return true`() {
        loginRepository.setExpectedEmail("yousefkazemi1@gmail.com")
        loginRepository.setExpectedPassword("123456")
        viewModel.login("yousefkazemi1@gmail.com", "123456")

        val value = viewModel.loginStatus.getOrAwaitValue()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }

    @After
    fun tearDown() {

    }
}