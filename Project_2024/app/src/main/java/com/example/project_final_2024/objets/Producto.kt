package com.example.project_final_2024.objets

data class Producto(
    val id: Int,
    val title: String,
    val price: Double,
    val categoryId: Int,
    val image: String,
    val lote: Int
)
