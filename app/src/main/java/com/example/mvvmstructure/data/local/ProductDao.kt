package com.example.mvvmstructure.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mvvmstructure.data.model.ProductEntity

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    /**
     * LiveData works with asynchronous and no need for suspend
     */
    @Query("SELECT * FROM products")
    suspend fun observeAllProducts(): List<ProductEntity>

    @Query("SELECT * FROM products")
    fun observeBookmarkedProducts(): LiveData<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id=:id")
    fun observeProduct(id: Long): LiveData<ProductEntity>
}