package com.example.mvvmstructure.data.repository.search

import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.data.remote.MovieApiInterface
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val movieApiInterface: MovieApiInterface
) : SearchRepository {

    override fun searchForProducts(searchQuery: String): Single<List<Product>> =
        movieApiInterface.searchForProducts(searchQuery).map {
            it.products
        }
}