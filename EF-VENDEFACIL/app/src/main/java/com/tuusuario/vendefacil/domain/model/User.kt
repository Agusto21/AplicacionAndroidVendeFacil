package com.tuusuario.vendefacil.domain.model

data class User(
    val id: String,
    val name: String,
    val businessName: String,
    val email: String,
    val phone: String,
    val token: String
)