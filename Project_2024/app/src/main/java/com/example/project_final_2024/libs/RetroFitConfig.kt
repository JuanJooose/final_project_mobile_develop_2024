package com.example.project_final_2024.libs

import com.example.project_final_2024.network.FakeStoreApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitConfig {
    // Usamos Retrofit.Builder() para crear la instancia de Retrofit
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://fakestoreapi.com/") // Establecer la URL base
        .addConverterFactory(GsonConverterFactory.create()) // Convertir las respuestas JSON a objetos de Kotlin
        .build() // Crear la instancia de Retrofit

    val api: FakeStoreApi = retrofit.create(FakeStoreApi::class.java) // Crear la instancia de la API
}
