package com.example.myapplication.network.service

import com.example.myapplication.data.models.Product
import retrofit2.Call
import retrofit2.http.GET

interface FakeStoreAPI {

    @GET("products")
    fun getProducts(): Call<List<Product>>
}