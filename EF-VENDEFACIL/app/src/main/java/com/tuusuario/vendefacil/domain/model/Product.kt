package com.tuusuario.vendefacil.domain.model

data class Product(
    val id: String = "",
    val name: String,
    val category: String,
    val price: Double,
    val stock: Int
)