package com.example.project_final_2024.data_binder

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso


class loadImage {

    @BindingAdapter("imageUrl")
    fun CargarImagen(view: ImageView, url: String?) {
            url?.let {
                Picasso.get().load(it).into(view)
            }
    }
}