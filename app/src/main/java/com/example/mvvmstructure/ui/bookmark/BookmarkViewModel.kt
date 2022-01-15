package com.example.mvvmstructure.ui.bookmark

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.mvvmstructure.data.repository.product.ProductRepository

class BookmarkViewModel @ViewModelInject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    /**
     * Fetch all bookmarked list from database. The return type is LiveData<T>/
     * When new item inserted, deleted or updated this liveData will be triggered.
     * Also view will be updated.
     */
    val products = repository.observeBookmarkedProducts()
}