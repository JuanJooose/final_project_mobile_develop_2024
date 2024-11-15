package com.example.project_final_2024.objets

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.project_final_2024.R

class FragmentProduct : Fragment(R.layout.product_card_layout) {
    companion object {
        private const val ARG_PRODUCT_NAME = "productName"
        private const val ARG_PRODUCT_PRICE = "productPrice"
        private const val ARG_PRODUCT_STOCK = "productPrice"
        private const val ARG_PRODUCT_LOTE = "productPrice"

        fun newInstance(name: String, price: Double, stock: Int, lote: Int): FragmentProduct {
            val fragment = FragmentProduct()
            val args = Bundle().apply {
                putString(ARG_PRODUCT_NAME, name)
                putDouble(ARG_PRODUCT_PRICE, price)
                putInt(ARG_PRODUCT_STOCK, stock)
                putInt(ARG_PRODUCT_LOTE, lote)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productName = arguments?.getString(ARG_PRODUCT_NAME)
        val productPrice = arguments?.getDouble(ARG_PRODUCT_PRICE)
        val stock = arguments?.getInt(ARG_PRODUCT_STOCK)
        val lote  = arguments?.getInt(ARG_PRODUCT_LOTE)

//        // Configura los elementos de tu layout con los datos del producto
        view.findViewById<TextView>(R.id.name).text = productName
        view.findViewById<TextView>(R.id.idProduct).text = productName
        view.findViewById<TextView>(R.id.stock).text = "${stock}"
        view.findViewById<TextView>(R.id.lote).text = "${lote}"
    }
}