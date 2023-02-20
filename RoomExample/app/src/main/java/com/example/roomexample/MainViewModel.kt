package com.example.roomexample

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(application: Application) : ViewModel() {
    val allProducts: LiveData<List<Product>> by lazy {
        repository.allProducts
    }
    val searchResults: MutableLiveData<List<Product>> by lazy {
        repository.searchResults
    }
    private val repository: ProductRepository by lazy {
        val productDb = ProductRoomDatabase.getInstance(application)
        val productDao = productDb.productDao()
        ProductRepository(productDao)
    }

//    init {
//        val productDb = ProductRoomDatabase.getInstance(application)
//        val productDao = productDb.productDao()
//        repository = ProductRepository(productDao)
//
//        allProducts =
//        searchResults = repository.searchResults
//    }

    fun insertProduct(product: Product) {
        repository.insertProduct(product)
    }

    fun findProduct(name: String) {
        repository.findProduct(name)
    }

    fun deleteProduct(name: String) {
        repository.deleteProduct(name)
    }
}