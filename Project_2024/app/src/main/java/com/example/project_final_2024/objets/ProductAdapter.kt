package com.example.project_final_2024.objets

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project_final_2024.R
import com.squareup.picasso.Picasso

class ProductAdapter(private val productList: List<Producto>) :

    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_card_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]
        holder.productName.setText(product.title)
        holder.productCategory.setText("Categoria: ${product.categoryId}")
        holder.productId.setText("Id: ${product.id}")
        holder.productContainer.setText("Lote: ${product.lote}")
        Picasso.get()
            .load(product.image)  // Aquí pones la URL de la imagen
            .placeholder(R.drawable.rounded_subheader)  // Imagen de placeholder mientras carga // Imagen a mostrar si hay un error
            .into(holder.productImage)

    }

    override fun getItemCount(): Int {
        return productList.size
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName: TextView = itemView.findViewById(R.id.product_name);
        var productCategory: TextView = itemView.findViewById(R.id.product_category)
        var productId: TextView = itemView.findViewById(R.id.product_id)
        var productStock: TextView = itemView.findViewById(R.id.product_stock)
        var productContainer: TextView = itemView.findViewById(R.id.product_container)
        var productImage: ImageView = itemView.findViewById(R.id.product_image)
    }
}