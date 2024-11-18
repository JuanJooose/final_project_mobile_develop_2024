package com.example.project_final_2024

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateProductActivity : AppCompatActivity() {
    private lateinit var btnBackToMain: Button;
    private lateinit var btnCreateProduct: Button;
    private lateinit var spnnrCategories: Spinner;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        window.statusBarColor = ContextCompat.getColor(this, R.color.bg_header)

        val options = listOf("men's clothing", "jewelery", "electronics", "women's clothing")
        spnnrCategories = findViewById(R.id.spinner)
        btnBackToMain = findViewById(R.id.btn_add_product);
        btnCreateProduct = findViewById(R.id.btn_add)


        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnnrCategories.adapter = adapter;
    }


    override fun onStart() {
        super.onStart()
        btnBackToMain.setText("Volver")
        btnBackToMain.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent)
        }


        btnCreateProduct.setOnClickListener {
//            Aquí se llama a la función crear producto, deberia retornar un True o false para devolverse a la vista que muestra los productos
        }
    }

}