package com.example.mvvmstructure.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Long,
    val username: String,
    val imageUrl: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val views: Long,
    val likes: Long,
    val tags: String
)