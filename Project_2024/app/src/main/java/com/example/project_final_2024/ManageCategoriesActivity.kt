package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.project_final_2024.Utilities.DatabaseHelper

class ManageCategoriesActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var listView: ListView
    private lateinit var btnAddCategory: Button
    private lateinit var btnAddCategory2: Button
    private lateinit var btnAddInvoice: Button
    private lateinit var edtCategoryName: EditText
    private lateinit var btnBackToMain: Button;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_categories)

        dbHelper = DatabaseHelper(this)

        listView = findViewById(R.id.listViewCategories)
        btnAddCategory = findViewById(R.id.btnAddCategory)
        btnAddCategory2 = findViewById(R.id.btn_add_category)
        btnAddInvoice = findViewById(R.id.btn_add_invoice)
        edtCategoryName = findViewById(R.id.edtCategoryName)
        btnBackToMain = findViewById(R.id.btn_add_product)

        btnBackToMain.visibility = View.VISIBLE
        btnAddCategory2.visibility = View.GONE
        btnAddInvoice.visibility = View.GONE

        loadCategories()

        btnAddCategory.setOnClickListener {
            val categoryName = edtCategoryName.text.toString().trim()
            if (categoryName.isNotEmpty()) {
                val newId = (dbHelper.obtenerMaximoIdCategoria() ?: 20) + 1 // ID desde 21 en adelante
                dbHelper.insertarCategoria(newId, categoryName)
                Toast.makeText(this, "Categoría agregada", Toast.LENGTH_SHORT).show()
                edtCategoryName.text.clear()
                loadCategories()
            } else {
                Toast.makeText(this, "Por favor ingresa un nombre de categoría", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadCategories() {
        val categories = dbHelper.obtenerCategorias()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories.map { it.category})
        listView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        btnBackToMain.setText("Volver")
        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }
    }
}
