package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

// La 'T' es un tipo genérico. Significa que "data" puede ser 
// cualquier cosa: una lista de productos, los datos de un usuario, etc.
data class ApiResponse<T>(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: T
)