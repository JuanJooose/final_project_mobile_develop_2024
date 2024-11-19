package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.network.FakeStoreService
import com.example.project_final_2024.objets.ProductAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper;
    private lateinit var fakeStoreService: FakeStoreService;
    private lateinit var btnAddProduct: Button;
    private lateinit var btnAddCategory: Button;
    private lateinit var btnAddInvoice: Button;

    private val REQUEST_CODE_INVOICE = 100  // Código de solicitud para InvoiceActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_header)

        dbHelper = DatabaseHelper(this)
        fakeStoreService = FakeStoreService(dbHelper);

        btnAddProduct = findViewById(R.id.btn_add_product);
        btnAddProduct.setText("+ Producto");

        btnAddCategory = findViewById(R.id.btn_add_category);
        btnAddCategory.setText("+ Categoría");

        btnAddInvoice = findViewById(R.id.btn_add_invoice);
        btnAddInvoice.setText("+ Factura");

        btnAddProduct.visibility = View.VISIBLE
        btnAddCategory.visibility = View.VISIBLE
        btnAddInvoice.visibility = View.VISIBLE

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

        btnAddCategory.setOnClickListener {
            val intent = Intent(this, ManageCategoriesActivity::class.java);
            startActivity(intent);
        }

        btnAddInvoice.setOnClickListener {
            val intent = Intent(this, InvoiceActivity::class.java);
            startActivity(intent);
        }
    }
}
