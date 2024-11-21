package com.example.project_final_2024.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final_2024.R
import com.example.project_final_2024.objets.Producto

class InvoiceDetailAdapter(
    private val selectedProducts: MutableMap<Producto, Int>
) : RecyclerView.Adapter<InvoiceDetailAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvProductQuantity: TextView = itemView.findViewById(R.id.tv_product_quantity)
        val tvProductTotal: TextView = itemView.findViewById(R.id.tv_product_total)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_invoice_detail, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = selectedProducts.keys.toList()[position]
        val quantity = selectedProducts[product] ?: 0
        val total = product.price * quantity

        holder.tvProductName.text = product.title
        holder.tvProductQuantity.text = "Cantidad: $quantity"
        holder.tvProductTotal.text = "Total: $$total"
    }

    override fun getItemCount(): Int {
        return selectedProducts.size
    }
}
