package com.example.myapplication.data.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.myapplication.lib.RetroFitConfig
import retrofit2.Call

class Product : ViewModel() {

    // Definir las propiedades
    val id: Int = 0
    val title: String = ""
    val price: Double = 0.0
    val category: String = ""
    val description: String = ""
    val image: String = ""
    val Lote: Int = 0

    // Función suspendida para obtener los productos de la API
    suspend fun fetchUsers(): Call<List<Product>> {
        // Llama a tu API aquí para obtener la lista de productos
        // Esto es un ejemplo usando Retrofit (ajustalo a tu caso)
        return RetroFitConfig.connectionApi.getProducts()
    }
}
