package com.example.mvvmstructure.data.repository

import com.example.mvvmstructure.data.model.User
import com.example.mvvmstructure.data.repository.login.LoginRepository
import com.example.mvvmstructure.utils.Resource

class FakeLoginRepository : LoginRepository {
    private var expectedEmail = ""
    private var expectedPassword = ""

    fun setExpectedEmail(email: String) {
        expectedEmail = email
    }

    fun setExpectedPassword(password: String) {
        expectedPassword = password
    }

    override suspend fun login(email: String, password: String): Resource<User> =
        if (email == expectedEmail && password == expectedPassword) {
            Resource.success(
                User(
                    userId = 1,
                    accessToken = "",
                    username = "",
                    email = "",
                    imageUrl = ""
                )
            )
        } else {
            Resource.error("The credentials are incorrect", null)
        }
}