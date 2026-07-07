package com.tuusuario.vendefacil.presentation.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.domain.model.TransactionItem
import com.tuusuario.vendefacil.domain.usecase.ProductUseCases
import com.tuusuario.vendefacil.domain.usecase.SalesUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(private val salesUseCases: SalesUseCases, private val productUseCases: ProductUseCases) : ViewModel() {
    private val allTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    val searchQuery = MutableStateFlow("")
    val allProducts = MutableStateFlow<List<Product>>(emptyList())

    val transactions = searchQuery.combine(allTransactions) { query, list ->
        if (query.isBlank()) list else list.filter { it.clientName.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val totalSales = allTransactions.combine(searchQuery) { list, _ -> list.sumOf { it.total } }.stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    init {
        viewModelScope.launch {
            salesUseCases.getTransactions().collect { allTransactions.value = it }
        }
        viewModelScope.launch {
            productUseCases.getProducts().collect { allProducts.value = it }
        }

        viewModelScope.launch {
            salesUseCases.fetchTransactions()
        }
        viewModelScope.launch {
            productUseCases.fetchProducts()
        }
    }
    fun removeItemFromTransaction(t: Transaction, itemToRemove: TransactionItem) {
        val updatedItems = t.items.toMutableList()
        updatedItems.remove(itemToRemove)

        val newTotal = updatedItems.sumOf { it.subtotal }
        val updatedTransaction = t.copy(items = updatedItems, total = newTotal)

        viewModelScope.launch {
            // 1. Actualizamos la venta en AWS
            salesUseCases.updateTransaction(updatedTransaction)

            productUseCases.updateStock(itemToRemove.product.id, -itemToRemove.quantity)
        }
    }

    fun addItemToTransaction(t: Transaction, product: Product, quantity: Int) {
        val updatedItems = t.items.toMutableList()
        val existing = updatedItems.find { it.product.id == product.id }

        if (existing != null) {
            existing.quantity += quantity
            existing.subtotal = existing.quantity * product.price
        } else {
            updatedItems.add(TransactionItem(product, quantity, product.price * quantity))
        }

        val newTotal = updatedItems.sumOf { it.subtotal }
        val updatedTransaction = t.copy(items = updatedItems, total = newTotal)

        viewModelScope.launch {
            // 1. Actualizamos la venta en AWS
            salesUseCases.updateTransaction(updatedTransaction)

            // 2. ¡DESCONTAMOS EL STOCK DEL INVENTARIO!
            productUseCases.updateStock(product.id, quantity)
        }
    }
}