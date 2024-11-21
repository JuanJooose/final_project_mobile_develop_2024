package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.network.FakeStoreService

class LoadingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper;
    private lateinit var fakeStoreService: FakeStoreService;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)

        dbHelper = DatabaseHelper(this)
        fakeStoreService = FakeStoreService(dbHelper);

        val productos = dbHelper.obtenerProductos()
        Log.e("Cuántos productos hay?", "${productos.size}")

        if (productos.isEmpty()) {
            // Si no hay productos, cargamos los datos desde la API
            fakeStoreService.obtenerCategoriasDeAPI();
            fakeStoreService.obtenerProductosDeAPI();
        } else {
            // Ya hay productos en la base de datos, puedes evitar volver a cargarlos
            Log.d("Inventario", "Los productos ya están cargados, no se volverán a insertar.")
        }

        // Configurar paddings para las barras del sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Navegar a la siguiente actividad después de 4 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            val animationIntent = Intent(this@LoadingActivity, MainActivity::class.java)
            startActivity(animationIntent)
            finish()
        }, 4000)
    }
}

