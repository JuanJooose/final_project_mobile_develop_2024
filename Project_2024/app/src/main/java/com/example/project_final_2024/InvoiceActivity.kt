package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final_2024.Utilities.DatabaseHelper
import com.example.project_final_2024.adapters.InvoiceProductAdapter
import com.example.project_final_2024.objets.Producto

class InvoiceActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerInvoice: RecyclerView
    private lateinit var tvTotal: TextView
    private lateinit var btnGenerateInvoice: Button
    private lateinit var btnAddCategory: Button
    private lateinit var btnAddInvoice: Button
    private lateinit var btnBackToMain: Button;

    private var selectedProducts = mutableMapOf<Producto, Int>()
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_product)

        dbHelper = DatabaseHelper(this)

        btnBackToMain = findViewById(R.id.btn_add_product)
        btnAddCategory = findViewById(R.id.btn_add_category)
        btnAddInvoice = findViewById(R.id.btn_add_invoice)

        recyclerInvoice = findViewById(R.id.recycler_invoice)
        tvTotal = findViewById(R.id.tv_total)
        btnGenerateInvoice = findViewById(R.id.btn_generate_invoice)

        btnBackToMain.visibility = View.VISIBLE
        btnAddCategory.visibility = View.GONE
        btnAddInvoice.visibility = View.GONE

        // Configurar RecyclerView
        val productos = dbHelper.obtenerProductos()
        recyclerInvoice.layoutManager = LinearLayoutManager(this)
        recyclerInvoice.adapter = InvoiceProductAdapter(productos) { producto, cantidad ->
            selectedProducts[producto] = cantidad
            calculateTotal()
        }

    }

    private fun calculateTotal() {
        total = selectedProducts.entries.sumOf { it.key.price * it.value }
        tvTotal.text = "Total: $$total"
    }

    override fun onStart() {
        super.onStart()
        btnBackToMain.setText("Volver")
        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
            finish() // Cierra la actividad actual para evitar volver a ella con el botón "Atrás"
        }

        // Generar factura
        btnGenerateInvoice.setOnClickListener {

            // Aquí estamos enviando el mapa de productos seleccionados a la siguiente actividad
            val intent = Intent(this, InvoiceDetailActivity::class.java)
            intent.putExtra("selectedProducts", HashMap(selectedProducts)) // Convertimos el Map a HashMap
            startActivity(intent)
        }

    }
}
