package com.example.mvvmstructure.data.repository.search

import com.example.mvvmstructure.data.model.Product
import com.example.mvvmstructure.utils.Resource
import io.reactivex.rxjava3.core.Single

interface SearchRepository {
    fun searchForProducts(searchQuery: String): Single<List<Product>>
}