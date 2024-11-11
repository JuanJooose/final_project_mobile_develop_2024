package com.example.project_final_2024

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.network.FakeStoreApi
import com.example.project_final_2024.objets.Producto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

        // Obtener productos de la base de datos para ver si se insertaron
        val productos = dbHelper.obtenerProductos()  // Usa 'dbHelper' aquí
        mostrarProductosEnConsola(productos)
    }

    private fun obtenerProductosDeAPI() {
        // Usamos Coroutine para hacer la solicitud en segundo plano
        GlobalScope.launch(Dispatchers.Main) {
            try {
                // Hacer la llamada a la API para obtener los productos
                val productos = api.obtenerProductos()

                // Insertar productos en la base de datos
                for (producto in productos) {
                    // Generar un valor aleatorio para el lote entre 1 y 10
                    val loteAleatorio = (1..10).random()

                    // Insertamos el producto con el lote aleatorio
                    if (producto.title != null && producto.price != null && producto.categoryId != null && producto.image != null) {
                        dbHelper.insertarProducto(
                            producto.id.toString(),
                            producto.title,
                            producto.price.toString(),
                            producto.categoryId.toString(),
                            producto.image,
                            loteAleatorio
                        )
                    } else {
                        Log.e("Inventario", "Producto con datos nulos: $producto")
                    }
                }

                // Mostrar los productos en consola
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
