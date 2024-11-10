package com.example.myapplication.utilities

import android.content.Context
import android.util.Log
import com.example.myapplication.data.models.Product
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Date

class Utilities {
    val date: Date = Date();
    private val product: Product = Product();

    fun saveDataInSharedPref(context: Context) {
        val sharedPreferences =
            context.getSharedPreferences("MyPrefsProducts", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        product.fetchUsers(
            onSuccess = { product ->
                println("Productos obtenidos: $product")
                // Asegúrate de que 'products' es un objeto que contiene las propiedades title y description
                editor.putString("title", product.title)
                editor.putString("description", product.description)
                editor.putString("dateReviewed", date.toString()) // Guarda la fecha actual

                // Aplica los cambios en SharedPreferences
                editor.apply()
            },
            onError = { e ->
                Log.d("Error", "Error al obtener productos: ${e.message}")
            }
        )
    }

    fun getDataBySharedPref(context: Context, key: String): String? {
        val sharedPreferences =
            context.getSharedPreferences("MyPrefsProducts", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "No existe clave con este nombre");
    }


    fun saveDataInFile(context: Context) {
        val filename = "storage.txt";

        product.fetchUsers(
            onSuccess = { product ->
                context.openFileOutput(filename, Context.MODE_PRIVATE)
                    .use { outputStream ->
                        outputStream.write(
                            product.title.toByteArray()
                        )
                    }
            },
            onError = { e ->
                Log.d("Error", "Error al obtener productos: ${e.message}")
            }
        )
    }


    fun getDataByFile(context: Context): String {
        val filename = "storage.txt";

        return context.openFileInput(filename).bufferedReader()
            .useLines { line -> line.fold("") { some, text -> "$some\n\n\n$text\n\n" } }
    }



    fun createFileIfNotExists(context: Context) {
        val filename = "storage.txt"
        val file = context.getFileStreamPath(filename)
        if (!file.exists()) {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use { outputStream ->
                outputStream.write("".toByteArray())  // Crea el archivo vacío
            }
        }
    }
}