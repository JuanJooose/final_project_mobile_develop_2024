package com.example.project_final_2024.network

import com.example.project_final_2024.objets.Producto
import com.example.project_final_2024.objets.CategoryByAPI
import retrofit2.http.GET

interface FakeStoreApi {
    @GET("products")
    suspend fun obtenerProductos(): List<Producto>

    @GET("products")
    suspend fun getProducts(): List<CategoryByAPI>

    @GET("products/categories")
    suspend fun obtenerCategorias(): List<String>
}