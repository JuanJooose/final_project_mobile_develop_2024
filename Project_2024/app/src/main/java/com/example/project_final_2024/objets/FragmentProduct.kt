package com.example.project_final_2024.objets

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.project_final_2024.R

class FragmentProduct : Fragment(R.layout.product_card_layout) {
    companion object {
        private const val ARG_PRODUCT_NAME = "productName"
        private const val ARG_PRODUCT_PRICE = "productPrice"

        fun newInstance(name: String, price: Double): FragmentProduct {
            val fragment = FragmentProduct()
            val args = Bundle().apply {
                putString(ARG_PRODUCT_NAME, name)
                putDouble(ARG_PRODUCT_PRICE, price)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val productName = arguments?.getString(ARG_PRODUCT_NAME)
        val productPrice = arguments?.getDouble(ARG_PRODUCT_PRICE)

//        // Configura los elementos de tu layout con los datos del producto
//        view.findViewById<TextView>(R.id.productPriceTextView).text = "$${productPrice.toString()}"
//        view.findViewById<TextView>(R.id.productNameTextView).text = productName
    }
}