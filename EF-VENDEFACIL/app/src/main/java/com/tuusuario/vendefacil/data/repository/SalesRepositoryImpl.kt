package com.tuusuario.vendefacil.data.repository

import android.util.Log
import com.tuusuario.vendefacil.data.mapper.toDomain
import com.tuusuario.vendefacil.data.mapper.toDto
import com.tuusuario.vendefacil.data.mapper.toUpdateDto
import com.tuusuario.vendefacil.data.remote.datasource.RemoteDataSource
import com.tuusuario.vendefacil.data.remote.dto.TransactionDto
import com.tuusuario.vendefacil.data.remote.dto.TransactionItemDto
import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.domain.repository.SalesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class SalesRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : SalesRepository {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    override fun getTransactions(): Flow<List<Transaction>> = _transactions

    // NUEVO: Método para descargar el historial desde AWS
    override suspend fun fetchTransactions() {
        try {
            val response = remoteDataSource.getVentas()
            if (response.isSuccessful && response.body()?.success == true) {
                // Obtenemos la lista de DTOs y mapeamos cada uno a Dominio
                val dtoList = response.body()!!.data
                _transactions.value = dtoList.map { it.toDomain() }
            } else {
                Log.e("API_VENTAS", "Error obteniendo ventas: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_VENTAS", "Fallo de red: ${e.message}")
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        try {
            val response = remoteDataSource.createVenta(transaction.toDto())
            if (response.isSuccessful && response.body()?.success == true) {
                // Volvemos a descargar todo de AWS para asegurar consistencia
                fetchTransactions()
            } else {
                Log.e("API_VENTAS", "Error creando venta: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_VENTAS", "Fallo de red al crear: ${e.message}")
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        if (transaction.id.isEmpty() || transaction.id.toLongOrNull() != null) return

        try {
            val response = remoteDataSource.updateVenta(transaction.id, transaction.toUpdateDto())
            if (response.isSuccessful && response.body()?.success == true) {
                fetchTransactions()
            } else {
                Log.e("API_VENTAS", "AWS Rechazó edición: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun deleteTransaction(transactionId: String) {
        try {
            val response = remoteDataSource.deleteVenta(transactionId)
            if (response.isSuccessful) {
                fetchTransactions() // Recargamos la lista
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun clearCache() {
        _transactions.value = emptyList()
    }
}