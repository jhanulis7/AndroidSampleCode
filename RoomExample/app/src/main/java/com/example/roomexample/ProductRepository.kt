package com.example.roomexample

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productDao: ProductDao) {
    suspend fun insertProduct(newProduct: Product) = productDao.insertProduct(newProduct)
    suspend fun updateProduct(newProduct: Product) = productDao.updateProduct(newProduct)
    suspend fun deleteProduct(name: String) = productDao.deleteProduct(name)
    suspend fun deleteAll() = productDao.deleteAll()
    fun getAllProducts() : Flow<List<Product>> = productDao.getAllProducts()
        .flowOn(Dispatchers.IO)
        .conflate()

    fun getSearchResult(name: String) : Flow<List<Product>> = productDao.searchProducts(name)
        .flowOn(Dispatchers.IO)
        .conflate()

}