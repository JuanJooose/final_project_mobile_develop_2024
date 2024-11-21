package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_final_2024.Utilities.DatabaseHelper

class UpdateStockActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var etProductId: EditText
    private lateinit var etNewStock: EditText
    private lateinit var btnUpdateStock: Button
    private lateinit var btnBackToMain: Button;
    private lateinit var btnAddCategory: Button;
    private lateinit var btnAddInvoice: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_stock)

        // Inicializar componentes
        btnBackToMain = findViewById(R.id.btn_add_product)
        btnAddCategory = findViewById(R.id.btn_add_category)
        btnAddInvoice = findViewById(R.id.btn_add_invoice)

        dbHelper = DatabaseHelper(this)
        etProductId = findViewById(R.id.et_product_id)
        etNewStock = findViewById(R.id.et_new_stock)
        btnUpdateStock = findViewById(R.id.btn_update_stock)

        btnBackToMain.visibility = View.VISIBLE
        btnAddCategory.visibility = View.GONE
        btnAddInvoice.visibility = View.GONE

    }

    override fun onStart() {
        super.onStart()
        btnBackToMain.setText("Volver")

        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }

        btnUpdateStock.setOnClickListener {
            val productId = etProductId.text.toString().trim()
            val newStock = etNewStock.text.toString().trim()

            if (productId.isEmpty() || newStock.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = productId.toIntOrNull()
            val stock = newStock.toIntOrNull()

            if (id == null || stock == null) {
                Toast.makeText(this, "Por favor, ingresa valores numéricos válidos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Actualizar el stock en la base de datos
            try {
                dbHelper.actualizarStock(id, stock)
                Toast.makeText(this, "Stock actualizado exitosamente", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Opcional: cerrar esta actividad para evitar volver a ella con el botón atrás
            } catch (e: Exception) {
                Toast.makeText(this, "Error al actualizar el stock", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
