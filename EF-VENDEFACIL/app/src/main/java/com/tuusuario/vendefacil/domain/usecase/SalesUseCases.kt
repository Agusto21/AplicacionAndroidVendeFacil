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

    suspend fun fetchTransactions() = salesRepository.fetchTransactions()

    suspend fun processSale(items: List<TransactionItem>, total: Double, client: String, method: String) {
        val transaction = Transaction(
            id = "", 
            items = items,
            timestamp = "",
            clientName = client.ifEmpty { "Cliente General" },
            paymentMethod = method,
            total = total
        )

        salesRepository.addTransaction(transaction)

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
