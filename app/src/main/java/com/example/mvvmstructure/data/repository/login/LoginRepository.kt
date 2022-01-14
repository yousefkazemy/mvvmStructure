package com.example.mvvmstructure.data.repository.login

import com.example.mvvmstructure.data.model.User
import com.example.mvvmstructure.utils.Resource

interface LoginRepository {
    suspend fun login(email: String, password: String): Resource<User>
}