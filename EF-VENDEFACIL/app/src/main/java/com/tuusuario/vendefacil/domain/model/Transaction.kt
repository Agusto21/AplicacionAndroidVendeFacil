package com.tuusuario.vendefacil.domain.model

data class TransactionItem(
    val product: Product,
    var quantity: Int,
    var subtotal: Double
)

data class Transaction(
    val id: String = "",
    var items: List<TransactionItem>,
    val timestamp: String,
    val clientName: String,
    val paymentMethod: String,
    var total: Double,
    val status: String = "Completado"
) {
    val productName: String
        get() = if (items.size == 1) items.first().product.name else "Varios Productos (${items.size})"
    val quantity: Int
        get() = items.sumOf { it.quantity }
}
