package com.example.mvvmstructure.ui.bookmark

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvvmstructure.data.repository.product.ProductRepository

class BookmarkViewModel @ViewModelInject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    val products = repository.observeBookmarkedProducts()
}