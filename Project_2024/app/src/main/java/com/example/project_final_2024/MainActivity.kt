package com.example.project_final_2024

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.network.FakeStoreApi
import com.example.project_final_2024.objets.Categoria
import com.example.project_final_2024.objets.FragmentProduct
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
import kotlin.math.log

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
        obtenerProductosDeAPI();
        obtenerCategoriasDeAPI();



        // Obtener productos de la base de datos para ver si se insertaron
        val productos = dbHelper.obtenerProductos();

//        getProductsbyDatabase()
        Log.e("Cauntos productos hay? ", "${productos.size}")
        for (product in productos) {
            val fragment = FragmentProduct.newInstance(product.title, product.price)
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_lyt, fragment)
                .addToBackStack(null)
                .commit()
        }
    }



    private fun obtenerCategoriasDeAPI() {
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

    private fun obtenerProductosDeAPI() {
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


    private fun mostrarProductosEnConsola(productos: List<Producto>) {
        for (producto in productos) {
            Log.d(
                "InventarioFunction",
                "Producto: ${producto.title}, Precio: ${producto.price}, CategoriaID: ${producto.categoryId}, Imagen: ${producto.image}, Lote: ${producto.lote}"
            )
        }
    }


//    private fun getProductsbyDatabase() {
//        val products = dbHelper.obtenerProductos()
//
////        for (p in products) {
////            Log.d("ProductByDB", "title: ${p.title}  CategoryId: ${p.categoryId}")
////        }
//    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_lyt, fragment)
        transaction.commit()
    }

}
