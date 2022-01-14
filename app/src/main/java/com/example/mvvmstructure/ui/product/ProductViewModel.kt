package com.example.mvvmstructure.ui.product

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmstructure.data.model.ProductEntity
import com.example.mvvmstructure.data.repository.product.ProductRepository

class ProductViewModel @ViewModelInject constructor(
    private val repository: ProductRepository
) : ViewModel() {

    lateinit var product: LiveData<ProductEntity>

    fun fetchProduct(id: Long) {
        product = repository.observeProduct(id)
    }
}