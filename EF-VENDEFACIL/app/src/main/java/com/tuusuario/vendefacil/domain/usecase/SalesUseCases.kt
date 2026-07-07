package com.tuusuario.vendefacil.domain.usecase

import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.domain.model.TransactionItem
import com.tuusuario.vendefacil.domain.repository.ProductRepository
import com.tuusuario.vendefacil.domain.repository.SalesRepository

class SalesUseCases(
    private val salesRepository: SalesRepository,
    private val productRepository: ProductRepository
) {
    fun getTransactions() = salesRepository.getTransactions()

    // NUEVO: Exponemos el método para refrescar ventas al entrar a la pantalla de Historial
    suspend fun fetchTransactions() = salesRepository.fetchTransactions()

    suspend fun processSale(items: List<TransactionItem>, total: Double, client: String, method: String) {
        // Tu lógica actual de fechas locales está bien para mostrar algo rápido en UI
        // antes de que AWS responda, aunque AWS sobreescribirá esto.
        val transaction = Transaction(
            id = "", // Lo dejamos vacío porque toDto() lo limpiará y AWS generará el UUID real
            items = items,
            timestamp = "",
            clientName = client.ifEmpty { "Cliente General" },
            paymentMethod = method,
            total = total
        )

        // 1. Guardamos la venta en AWS
        salesRepository.addTransaction(transaction)

        // 2. Descontamos stock localmente para UI
        // (Nota: En una app de producción estricta, el Lambda de AWS debería descontar el stock en DynamoDB automáticamente)
        items.forEach { item ->
            productRepository.updateStock(item.product.id, item.quantity)
        }
    }

    suspend fun updateTransaction(transaction: Transaction) {
        salesRepository.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transactionId: String) {
        salesRepository.deleteTransaction(transactionId)
    }

    fun clearCache() = salesRepository.clearCache()
}