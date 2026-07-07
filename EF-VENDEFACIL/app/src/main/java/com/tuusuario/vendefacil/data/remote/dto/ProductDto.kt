package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("producto_id") val productoId: String? = null, // AWS lo genera al crear
    @SerializedName("nombre") val nombre: String,
    @SerializedName("categoria") val categoria: String? = null,
    @SerializedName("precio") val precio: Double,
    @SerializedName("stock") val stock: Int
)