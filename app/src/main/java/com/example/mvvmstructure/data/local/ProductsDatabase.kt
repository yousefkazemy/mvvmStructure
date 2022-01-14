package com.example.mvvmstructure.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvvmstructure.data.model.ProductEntity

@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ProductsDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}