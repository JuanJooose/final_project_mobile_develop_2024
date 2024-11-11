package com.example.project_final_2024.network

import com.example.project_final_2024.objets.Producto
import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun obtenerProductos(): List<Producto>

    @GET("products/categories")
    suspend fun ObtenerCategorias(): List<String>
}