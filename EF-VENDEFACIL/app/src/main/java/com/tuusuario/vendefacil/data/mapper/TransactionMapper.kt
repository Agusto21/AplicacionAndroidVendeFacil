package com.tuusuario.vendefacil.data.mapper

import com.tuusuario.vendefacil.data.remote.dto.TransactionDto
import com.tuusuario.vendefacil.data.remote.dto.TransactionItemDto
import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.domain.model.TransactionItem
import com.tuusuario.vendefacil.domain.model.Product

fun TransactionItemDto.toDomain() = TransactionItem(
    product = Product(
        id = this.productoId,
        name = this.nombre,
        category = "Sin categoría",
        price = if (this.cantidad > 0) this.subtotal / this.cantidad else 0.0,
        stock = 0
    ),
    quantity = this.cantidad,
    subtotal = this.subtotal
)

fun TransactionDto.toDomain() = Transaction(
    id = this.ventaId ?: "", 
    items = this.detalle.map { it.toDomain() },
    timestamp = this.fecha ?: "", 
    clientName = this.cliente,
    paymentMethod = this.metodoPago,
    total = this.total
)


fun Transaction.toDto() = TransactionDto(

    ventaId = if (this.id.isEmpty() || this.id.toLongOrNull() != null) null else this.id,
    fecha = this.timestamp.ifEmpty { null },
    cliente = this.clientName,
    metodoPago = this.paymentMethod,
    total = this.total,
    detalle = this.items.map {
        TransactionItemDto(
            productoId = it.product.id,
            nombre = it.product.name,
            cantidad = it.quantity,
            subtotal = it.subtotal
        )
    }
)

fun Transaction.toUpdateDto() = TransactionDto(
    ventaId = null,
    fecha = null,
    cliente = this.clientName,
    metodoPago = this.paymentMethod,
    total = this.total,
    detalle = this.items.map {
        TransactionItemDto(
            productoId = it.product.id,
            nombre = it.product.name,
            cantidad = it.quantity,
            subtotal = it.subtotal
        )
    }
)
