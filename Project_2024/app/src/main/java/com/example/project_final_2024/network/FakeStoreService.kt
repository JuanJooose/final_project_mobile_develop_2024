package com.example.project_final_2024.network

import android.util.Log
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.objets.Categoria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FakeStoreService(val dbHelper: DatabaseHelper) {

    private val api: FakeStoreApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FakeStoreApi::class.java)
    }

     fun obtenerCategoriasDeAPI() {
        // Usamos coroutines para llamar a la API de manera asíncrona
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Realizamos la llamada de forma suspendida
                val categorias = api.obtenerCategorias() // Llamada suspendida a la API

                // Imprimimos las categorías obtenidas para la depuración
//                Log.d("Inventario", "Categorías obtenidas de la API: $categorias")

                // Verificamos y agregamos las categorías a la base de datos si no existen
                categorias.forEachIndexed { index, categoriaNombre ->
                    val categoria = Categoria(id = index + 1, category = categoriaNombre)

                    // Imprimimos cada categoría antes de agregarla a la base de datos
//                    Log.d("Inventario", "Procesando categoría: $categoria")

                    // Verificamos si la categoría ya está en la base de datos
                    if (!dbHelper.existeCategoria(categoria.id)) {
                        dbHelper.insertarCategoria(categoria.id, categoria.category)
                        Log.d("Inventario", "Categoría insertada: $categoria")
                    }
                }

            } catch (e: Exception) {
                Log.e("Inventario", "Error al obtener categorías de la API", e)
            }
        }
    }

     fun obtenerProductosDeAPI() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Ahora, obtiene los productos de la API
                val products = api.getProducts()

                // Insertar los productos en la base de datos
                for (producto in products) {
                    // Obtener el id de la categoría a partir del nombre de la categoría
                    val categoriaId = dbHelper.obtenerIdCategoriaPorNombre(producto.category)

                    // Generar lote aleatorio
                    val loteAleatorio = (1..10).random()

                    // Verificar que el producto tenga datos completos
                    if (categoriaId != null && producto.title != "" && producto.price != 0.0 && producto.image != "") {
                        dbHelper.insertarProducto(
                            producto.id.toString(),
                            producto.title,
                            producto.price.toString(),
                            categoriaId.toString(),
                            producto.image,
                            loteAleatorio
                        )
                        Log.d(
                            "Inventario",
                            "Producto: ${producto.title}, Precio: ${producto.price}, CategoriaID: ${categoriaId}, Imagen: ${producto.image}, Lote: ${producto.lote}"
                        )
                    } else {
                        Log.e("Inventario", "Producto con datos incompletos: $producto")
                    }
                }

                // Mostrar productos en consola
//                mostrarProductosEnConsola(products)

            } catch (e: Exception) {
                Log.e("Inventario", "Error al obtener productos de la API", e)
            }
        }
    }
}