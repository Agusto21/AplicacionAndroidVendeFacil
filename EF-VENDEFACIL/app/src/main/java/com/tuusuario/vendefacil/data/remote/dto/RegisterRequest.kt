package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nombre")
    val fullName: String,
    @SerializedName("negocio")
    val businessName: String,
    @SerializedName("correo")
    val email: String,
    @SerializedName("telefono")
    val phone: String,
    @SerializedName("contrasena")
    val password: String
)