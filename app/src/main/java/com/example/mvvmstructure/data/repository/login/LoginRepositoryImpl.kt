package com.example.mvvmstructure.data.repository.login

import com.example.mvvmstructure.data.model.User
import com.example.mvvmstructure.data.remote.MovieApiInterface
import com.example.mvvmstructure.utils.Resource
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    movieApiInterface: MovieApiInterface
) : LoginRepository {
    override suspend fun login(email: String, password: String): Resource<User> =
        //TODO ==> Should implemented with a real api
        if (email == "yousefkazemy1@gmail.com" && password == "123456") {
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