package com.example.project_final_2024.adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final_2024.R
import com.example.project_final_2024.objets.Producto

class InvoiceProductAdapter(
    private val productos: List<Producto>,
    private val onQuantityChanged: (Producto, Int) -> Unit
) : RecyclerView.Adapter<InvoiceProductAdapter.ViewHolder>() {

    private val selectedQuantities = mutableMapOf<Int, Int>() // Map para rastrear cantidades seleccionadas

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvStock: TextView = itemView.findViewById(R.id.tv_stock)
        val tvPrice: TextView = itemView.findViewById(R.id.tv_price)
        val etQuantity: EditText = itemView.findViewById(R.id.et_quantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_invoice_product, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val producto = productos[position]
        holder.tvProductName.text = producto.title
        holder.tvStock.text = "Stock: ${producto.lote} unds"
        holder.tvPrice.text = "$${producto.price}"

        holder.etQuantity.setText(selectedQuantities[producto.id]?.toString() ?: "")

        // Manejar cambios en la cantidad ingresada
        holder.etQuantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val quantity = s?.toString()?.toIntOrNull() ?: 0
                if (quantity <= producto.lote) {
                    selectedQuantities[producto.id] = quantity
                    onQuantityChanged(producto, quantity)
                } else {
                    holder.etQuantity.error = "Cantidad excede el stock disponible"
                }
            }
        })
    }

    override fun getItemCount(): Int = productos.size
}
