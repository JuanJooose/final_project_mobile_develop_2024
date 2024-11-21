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
import com.example.project_final_2024.adapters.InvoiceDetailAdapter
import com.example.project_final_2024.objets.Producto

class InvoiceDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerInvoiceDetail: RecyclerView
    private lateinit var tvTotalDetail: TextView
    private lateinit var btnConfirmInvoice: Button
    private lateinit var btnBackToMain: Button

    private var selectedProducts: MutableMap<Producto, Int> = mutableMapOf()
    private var total = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invoice_detail)

        dbHelper = DatabaseHelper(this)

        btnBackToMain = findViewById(R.id.btn_add_product)
        recyclerInvoiceDetail = findViewById(R.id.recycler_invoice_detail)
        tvTotalDetail = findViewById(R.id.tv_total_detail)
        btnConfirmInvoice = findViewById(R.id.btn_confirm_invoice)

        // Recuperar datos pasados desde la actividad anterior
        val receivedMap = intent.getSerializableExtra("selectedProducts") as? HashMap<Producto, Int>
        if (receivedMap != null) {
            selectedProducts = receivedMap
        } else {
            // Manejo del caso en que no se recibieron productos, tal vez lanzar un error o mensaje
        }

        // Configurar RecyclerView
        recyclerInvoiceDetail.layoutManager = LinearLayoutManager(this)
        recyclerInvoiceDetail.adapter = InvoiceDetailAdapter(selectedProducts)

        // Calcular total
        calculateTotal()
    }

    private fun calculateTotal() {
        total = selectedProducts.entries.sumOf { it.key.price * it.value }
        tvTotalDetail.text = "Total: $$total"
    }

    private fun confirmInvoice() {
        // Actualizar el stock en la base de datos
        selectedProducts.forEach { (producto, cantidad) ->
            dbHelper.facturarStock(producto.id, cantidad)
        }

        // Volver a MainActivity después de confirmar la factura
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finaliza la actividad actual
    }

    override fun onStart() {
        super.onStart()

        btnBackToMain.setText("Volver")
        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnConfirmInvoice.setOnClickListener {
            confirmInvoice()
        }
    }
}

