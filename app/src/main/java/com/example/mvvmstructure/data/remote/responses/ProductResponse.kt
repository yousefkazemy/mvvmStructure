package com.example.mvvmstructure.data.remote.responses

import com.example.mvvmstructure.data.model.Product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductResponse(
        @Expose
        @SerializedName("totalHits")
        val totalProducts: Long,

        @Expose
        @SerializedName("hits")
        val products: List<Product>
)