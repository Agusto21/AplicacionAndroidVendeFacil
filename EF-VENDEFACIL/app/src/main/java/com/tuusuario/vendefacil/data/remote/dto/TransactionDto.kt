package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TransactionDto(
    @SerializedName("venta_id") val ventaId: String? = null, 
    @SerializedName("fecha") val fecha: String? = null,     
    @SerializedName("cliente") val cliente: String,
    @SerializedName("metodopago") val metodoPago: String,
    @SerializedName("total") val total: Double,
    @SerializedName("detalle") val detalle: List<TransactionItemDto>
)
