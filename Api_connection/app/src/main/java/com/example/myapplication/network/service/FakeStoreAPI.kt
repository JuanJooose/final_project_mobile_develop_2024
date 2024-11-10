package com.example.myapplication.network.service

import com.example.myapplication.data.models.Product
import retrofit2.http.GET

interface FakeStoreAPI {

    @GET("Products/1")
    suspend fun getProducts(): Product
}