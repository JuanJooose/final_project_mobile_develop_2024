package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.project_final_2024.Utilities.DatabaseHelper

class CreateProductActivity : AppCompatActivity() {
    private lateinit var btnBackToMain: Button;
    private lateinit var btnAddCategory: Button;
    private lateinit var btnAddInvoice: Button;
    private lateinit var btnCreateProduct: Button;
    private lateinit var spnnrCategories: Spinner;
    private lateinit var dbHelper: DatabaseHelper;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_product)

        dbHelper = DatabaseHelper(this)
        spnnrCategories = findViewById(R.id.spinner)
        btnBackToMain = findViewById(R.id.btn_add_product)
        btnAddCategory = findViewById(R.id.btn_add_category)
        btnAddInvoice = findViewById(R.id.btn_add_invoice)
        btnCreateProduct = findViewById(R.id.btn_add)

        btnBackToMain.visibility = View.VISIBLE
        btnAddCategory.visibility = View.GONE
        btnAddInvoice.visibility = View.GONE

        loadCategoriesIntoSpinner()
    }

    private fun loadCategoriesIntoSpinner() {
        val categories = dbHelper.obtenerCategorias()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories.map { it.category })
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spnnrCategories.adapter = adapter
    }


    override fun onStart() {
        super.onStart()
        btnBackToMain.setText("Volver")

        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }


        btnCreateProduct.setOnClickListener {
            // Obtener los valores de los campos
            val nombreProducto = findViewById<EditText>(R.id.product_name).text.toString().trim()
            val precioProducto = findViewById<EditText>(R.id.product_price).text.toString().trim()
            val categoriaSeleccionada = spnnrCategories.selectedItem.toString() // Nombre de la categoría
            val loteProducto = findViewById<EditText>(R.id.product_container).text.toString().trim()
            val imagenProducto = findViewById<EditText>(R.id.product_image).text.toString().trim()

            // Validar los campos requeridos
            if (nombreProducto.isEmpty() || precioProducto.isEmpty() || loteProducto.isEmpty() || imagenProducto.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convertir el precio y lote a números
            val precio = precioProducto.toDoubleOrNull()
            val lote = loteProducto.toIntOrNull()

            if (precio == null || lote == null) {
                Toast.makeText(this, "El precio y el lote deben ser valores numéricos válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Obtener el ID de la categoría seleccionada
            val categoriaId = dbHelper.obtenerIdCategoriaPorNombre(categoriaSeleccionada)
            if (categoriaId == null) {
                Toast.makeText(this, "Categoría inválida. Por favor selecciona una categoría válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Insertar el producto en la base de datos
            val exito = dbHelper.insertarProducto(
                id = "", // Dejar vacío, será autogenerado
                nombre = nombreProducto,
                precio = precio.toString(),
                categoriaId = categoriaId.toString(),
                imagen = imagenProducto,
                lote = lote
            )

            if (exito) {
                Toast.makeText(this, "Producto creado exitosamente", Toast.LENGTH_SHORT).show()
                // Volver a la actividad principal
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Opcional: cerrar esta actividad para evitar volver a ella con el botón atrás
            } else {
                Toast.makeText(this, "Error al crear el producto. Intenta nuevamente", Toast.LENGTH_SHORT).show()
            }
        }

    }

}