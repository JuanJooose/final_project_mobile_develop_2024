package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.viewbinding.ViewBinding
import com.example.myapplication.data.models.Product
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utilities.Utilities
import java.util.Date

class MainActivity : ComponentActivity() {
    final lateinit var bindings: ViewBinding
    val utilities: Utilities = Utilities();


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()

        // Obtener datos guardados en SharedPreferences
        val productTitle: String? = utilities.getDataBySharedPref(this, "title")
        val productDescription: String? = utilities.getDataBySharedPref(this, "description")

        // Mostrar en Log los datos obtenidos
        Log.d("Titulo", "$productTitle\n")
        Log.d("Descripcion", "$productDescription\n")

        // Guardar los datos en SharedPreferences
        utilities.saveDataInSharedPref(this)

        // Crear el archivo si no existe
        utilities.createFileIfNotExists(this)

        // Guardar los datos en archivo
        utilities.saveDataInFile(this)

        // Obtener datos del archivo
        val contentByFile = utilities.getDataByFile(this)
        Log.d("Datos", "Contenido del archivo: $contentByFile\n")
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}