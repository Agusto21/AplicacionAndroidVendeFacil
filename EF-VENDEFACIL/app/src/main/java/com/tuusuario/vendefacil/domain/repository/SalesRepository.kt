package com.tuusuario.vendefacil.domain.repository

import com.tuusuario.vendefacil.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface SalesRepository {
    fun getTransactions(): Flow<List<Transaction>>
    suspend fun fetchTransactions() // NUEVO: Para refrescar desde AWS
    suspend fun addTransaction(transaction: Transaction)
    suspend fun updateTransaction(transaction: Transaction)
    suspend fun deleteTransaction(transactionId: String) // NUEVO: Para eliminar

    fun clearCache()
}