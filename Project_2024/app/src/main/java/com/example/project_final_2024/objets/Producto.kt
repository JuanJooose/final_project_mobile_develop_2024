package com.example.project_final_2024.objets

import java.io.Serializable

data class Producto(
    val id: Int,
    val title: String,
    val price: Double,
    val categoryId: Int,
    val image: String,
    val lote: Int
) : Serializable
