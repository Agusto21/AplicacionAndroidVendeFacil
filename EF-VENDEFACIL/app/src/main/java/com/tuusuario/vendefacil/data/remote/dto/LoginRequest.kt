package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("correo")
    val email: String,
    @SerializedName("contrasena")
    val password: String
)