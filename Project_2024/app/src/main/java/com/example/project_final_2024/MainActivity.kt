package com.example.project_final_2024

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.network.FakeStoreApi
import com.example.project_final_2024.objets.Categoria
import com.example.project_final_2024.objets.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper // Asegúrate de que la variable se llame 'dbHelper'
    private val api: FakeStoreApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(FakeStoreApi::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this) // Asegúrate de que se inicialice como 'dbHelper'

        // Insertar datos de la API en la base de datos (en segundo plano)
        obtenerProductosDeAPI()
        obtenerCategoriasDeAPI()

        // Obtener productos de la base de datos para ver si se insertaron
        val productos = dbHelper.obtenerProductos()  // Usa 'dbHelper' aquí
        mostrarProductosEnConsola(productos)
    }

    private fun obtenerCategoriasDeAPI() {
        // Usamos coroutines para llamar a la API de manera asíncrona
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Realizamos la llamada de forma suspendida
                val categorias = api.obtenerCategorias() // Llamada suspendida a la API

                // Imprimimos las categorías obtenidas para la depuración
                Log.d("Inventario", "Categorías obtenidas de la API: $categorias")

                // Verificamos y agregamos las categorías a la base de datos si no existen
                categorias.forEachIndexed { index, categoriaNombre ->
                    val categoria = Categoria(id = index + 1, category = categoriaNombre)

                    // Imprimimos cada categoría antes de agregarla a la base de datos
                    Log.d("Inventario", "Procesando categoría: $categoria")

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

    private fun obtenerProductosDeAPI() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Ahora, obtiene los productos de la API
                val productos = api.obtenerProductos()

                // Insertar los productos en la base de datos
                for (producto in productos) {
                    // Obtener el id de la categoría a partir del nombre de la categoría
                    val categoriaId = dbHelper.obtenerIdCategoriaPorNombre(producto.categoryId.toString())

                    // Generar lote aleatorio
                    val loteAleatorio = (1..10).random()

                    // Verificar que el producto tenga datos completos
                    if (categoriaId != null && producto.title != null && producto.price != null && producto.image != null) {
                        dbHelper.insertarProducto(
                            producto.id.toString(),
                            producto.title,
                            producto.price.toString(),
                            categoriaId.toString(),
                            producto.image,
                            loteAleatorio
                        )
                    } else {
                        Log.e("Inventario", "Producto con datos incompletos: $producto")
                    }
                }

                // Mostrar productos en consola
                mostrarProductosEnConsola(productos)

            } catch (e: Exception) {
                Log.e("Inventario", "Error al obtener productos de la API", e)
            }
        }
    }


    private fun mostrarProductosEnConsola(productos: List<Producto>) {
        for (producto in productos) {
            Log.d("Inventario", "Producto: ${producto.title}, Precio: ${producto.price}, CategoriaID: ${producto.categoryId}, Imagen: ${producto.image}, Lote: ${producto.lote}")
        }
    }

}
