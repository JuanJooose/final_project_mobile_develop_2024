package com.example.myapplication.utilities

import android.content.Context
import android.util.Log
import com.example.myapplication.data.models.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Utilities {

    // Función para guardar los datos en SharedPreferences
    fun saveDataInSharedPref(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyPrefsProducts", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Usar un CoroutineScope para ejecutar operaciones asíncronas
        CoroutineScope(Dispatchers.Main).launch {
            try {
                // Suponiendo que 'fetchUsers' devuelve una lista de productos
                val product = Product() // Aquí deberías usar tu ViewModel para obtener los datos
                product.fetchUsers().let { fetchedProducts ->
                    if (fetchedProducts.isNotEmpty()) {
                        val fetchedProduct = fetchedProducts[0] // Tomar el primer producto de la lista

                        // Guarda los datos en SharedPreferences
                        editor.putString("title", fetchedProduct.title)
                        editor.putString("price", fetchedProduct.price.toString())
                        editor.putString("description", fetchedProduct.description)
                        editor.putString("category", fetchedProduct.category)
                        editor.putString("image", fetchedProduct.image)
                        editor.apply() // Guarda los cambios
                    } else {
                        Log.d("Error", "No products found")
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "Error en saveDataInSharedPref: ${e.message}")
            }
        }
    }

    // Función para guardar datos en un archivo
    fun saveDataInFile(context: Context) {
        val filename = "storage.txt"

        CoroutineScope(Dispatchers.Main).launch {
            try {
                val product = Product() // Aquí deberías usar tu ViewModel para obtener los datos
                product.fetchUsers().let { fetchedProducts ->
                    if (fetchedProducts.isNotEmpty()) {
                        val fetchedProduct = fetchedProducts[0] // Tomar el primer producto de la lista

                        // Guardar en el archivo
                        context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                            outputStream.write(fetchedProduct.title.toByteArray())
                        }
                    } else {
                        Log.d("Error", "No products found")
                    }
                }
            } catch (e: Exception) {
                Log.d("Error", "Error en saveDataInFile: ${e.message}")
            }
        }
    }

    // Función para crear archivo si no existe
    fun createFileIfNotExists(context: Context) {
        val filename = "storage.txt"
        val file = context.getFileStreamPath(filename)
        if (!file.exists()) {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write("".toByteArray())  // Crea el archivo vacío
            }
        }
    }

    // Función para obtener datos desde SharedPreferences
    fun getDataBySharedPref(context: Context, key: String): String? {
        val sharedPreferences = context.getSharedPreferences("MyPrefsProducts", Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "No existe clave con este nombre")
    }

    // Función para obtener datos desde el archivo
    fun getDataByFile(context: Context): String {
        val filename = "storage.txt"
        return context.openFileInput(filename).bufferedReader()
            .useLines { line -> line.fold("") { some, text -> "$some\n\n\n$text\n\n" } }
    }
}
