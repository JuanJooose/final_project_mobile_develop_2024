package com.example.myapplication.data.models

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.myapplication.lib.RetroFitConfig
import com.example.myapplication.utilities.Utilities
import java.util.Date

class Product : ViewModel() {

    val id: Int = 0;
    val title: String = "";
    val price: Double = 0.0;
    val category: String = "";
    val description: String = "";

    fun fetchUsers(onSuccess: (Product) -> Unit, onError: (Exception) -> Unit) {
        viewModelScope.launch {
            try {
                // Llama directamente a la función suspend
                val products = RetroFitConfig.connectionApi.getProducts()
                //                // Lama a la función de éxito con los productos obtenidos
                onSuccess(products)

            } catch (e: Exception) {
                // Llama a la función de error si ocurre una excepción
                onError(e)
            }
        }
    }
}