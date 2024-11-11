package com.example.myapplication.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Función para insertar un producto en la base de datos
    fun insertProduct(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertProduct(product)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Error al insertar el producto: ${e.message}"
                }
            }
        }
    }

    // Función para obtener productos por categoría
    fun getProductsByCategory(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val productsList = repository.getProductsByCategory(categoryName)
                withContext(Dispatchers.Main) {
                    _products.value = productsList
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _errorMessage.value = "Error al obtener productos: ${e.message}"
                }
            }
        }
    }
}
