package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TransactionItemDto(
    @SerializedName("producto_id") val productoId: String,
    @SerializedName("producto") val nombre: String,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("subtotal") val subtotal: Double
)
