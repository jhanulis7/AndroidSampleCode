package com.example.roomexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ProductRepository
) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Product>>(listOf())
    val searchResults = _searchResults.asStateFlow()
    private val _allProducts = MutableStateFlow<List<Product>>(listOf())
    val allProducts = _allProducts.asStateFlow()

    init {
        getAllProjects()
    }
    fun insertProduct(product: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertProduct(product)
    }

    fun updateProduct(name: Product) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateProduct(name)
    }

    fun searchProducts(name: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.getSearchResult(name).collect {
            _searchResults.emit(it)
        }
    }

    fun deleteProduct(name: String) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteProduct(name)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
    }

    fun getAllProjects() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllProducts().distinctUntilChanged().collect {
            _allProducts.emit(it)
        }
    }
}