package com.example.myapplication.lib

import com.example.myapplication.network.service.FakeStoreAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitConfig {

    val BASE_URL = "https://fakestoreapi.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    val connectionApi: FakeStoreAPI = retrofit.create(FakeStoreAPI::class.java)
}