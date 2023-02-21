package com.example.roomexample

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products WHERE productName = :name")
    fun searchProducts(name: String): Flow<List<Product>>

    @Insert
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("DELETE FROM products WHERE productName = :name")
    suspend fun deleteProduct(name: String)

    @Query("DELETE FROM products")
    suspend fun deleteAll()
}