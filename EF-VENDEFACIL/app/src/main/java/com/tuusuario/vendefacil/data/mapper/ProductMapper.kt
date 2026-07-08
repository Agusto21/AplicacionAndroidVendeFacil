package com.tuusuario.vendefacil.data.mapper

import com.tuusuario.vendefacil.data.remote.dto.ProductDto
import com.tuusuario.vendefacil.domain.model.Product


fun ProductDto.toDomain() = Product(
    id = this.productoId ?: "",
    name = this.nombre,
    category = this.categoria ?: "Sin categoría",
    price = this.precio,
    stock = this.stock
)


fun Product.toDto() = ProductDto(
    productoId = if (this.id.isEmpty()) null else this.id, 
    nombre = this.name,
    categoria = if (this.category.isBlank() || this.category == "Sin categoría") null else this.category,
    precio = this.price,
    stock = this.stock
)

fun Product.toUpdateDto() = ProductDto(
    productoId = null,
    nombre = this.name,
    categoria = if (this.category.isBlank() || this.category == "Sin categoría") null else this.category,
    precio = this.price,
    stock = this.stock
)
