package com.example.myapplication.data.models

import android.content.Context
import com.example.myapplication.classDB.DatabaseHelper
import com.example.myapplication.lib.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class ProductRepository(context: Context) {
    private val dbHelper = DatabaseHelper(context)

    // Inserta una categoría si no existe y devuelve su ID
    fun insertCategoryIfNotExists(categoryName: String): Long {
        // Primero, intentamos obtener el ID de la categoría si ya existe
        val existingCategoryId = dbHelper.getCategoryIdByName(categoryName)
        if (existingCategoryId != null) {
            return existingCategoryId
        }
        // Si no existe, la insertamos y obtenemos el nuevo ID
        return dbHelper.insertCategory(categoryName)
    }

    // Inserta un producto en la base de datos
    fun insertProduct(product: Product) {
        // Inserta la categoría si no existe y obtiene el ID
        val categoryId = insertCategoryIfNotExists(product.category)
        // Inserta el producto con el ID de categoría
        dbHelper.insertProduct(product, categoryId)
    }

    // Obtiene una lista de productos de una categoría específica
    fun getProductsByCategory(categoryName: String): List<Product> {
        return dbHelper.getProductsByCategory(categoryName)
    }
}
