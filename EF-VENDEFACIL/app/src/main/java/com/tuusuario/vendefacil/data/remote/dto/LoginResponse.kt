package com.tuusuario.vendefacil.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("message")
    val message: String?,

    @SerializedName("data")
    val userData: UserDataDto
)

data class UserDataDto(
    @SerializedName("usuario_id")
    val userId: String,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("negocio")
    val businessName: String,

    @SerializedName("correo")
    val email: String,

    @SerializedName("telefono")
    val phone: String
)