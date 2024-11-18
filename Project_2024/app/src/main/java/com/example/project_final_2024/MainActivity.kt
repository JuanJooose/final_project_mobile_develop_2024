package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.network.FakeStoreService
import com.example.project_final_2024.objets.ProductAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper; // Asegúrate de que la variable se llame 'dbHelper'
    private lateinit var fakeStoreService: FakeStoreService;
    private lateinit var btnAddProduct: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_header)

        dbHelper = DatabaseHelper(this)
        fakeStoreService = FakeStoreService(dbHelper);
        btnAddProduct = findViewById(R.id.btn_add_product);
        btnAddProduct.setText("Agregar producto");
        // Verificar si ya hay productos en la base de datos

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

        // Creación de tarjetas de productos dinamicos
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ProductAdapter(productos)
        recyclerView.adapter = adapter
    }


    override fun onStart() {
        super.onStart()

        btnAddProduct.setOnClickListener {
            val intent = Intent(this, CreateProductActivity::class.java);
            startActivity(intent);
        }
    }
}
