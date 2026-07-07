package com.tuusuario.vendefacil.presentation.screens.charge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.model.TransactionItem
import com.tuusuario.vendefacil.domain.usecase.ProductUseCases
import com.tuusuario.vendefacil.domain.usecase.SalesUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ChargeViewModel(
    private val productUseCases: ProductUseCases,
    private val salesUseCases: SalesUseCases
) : ViewModel() {
    val products = MutableStateFlow<List<Product>>(emptyList())

    val cartItems = MutableStateFlow<List<TransactionItem>>(emptyList())

    val client = MutableStateFlow("")
    val paymentMethod = MutableStateFlow("Tarjeta")

    init {
        viewModelScope.launch {
            productUseCases.getProducts().collect { products.value = it }
        }

        viewModelScope.launch {
            productUseCases.fetchProducts()
        }
    }

    fun addItemToCart(product: Product, quantity: Int) {
        val currentCart = cartItems.value.toMutableList()
        val existingItem = currentCart.find { it.product.id == product.id }

        if (existingItem != null) {
            val newQty = existingItem.quantity + quantity
            if (newQty <= product.stock) {
                existingItem.quantity = newQty
                existingItem.subtotal = newQty * product.price
            }
        } else {
            if (quantity <= product.stock) {
                currentCart.add(TransactionItem(product, quantity, product.price * quantity))
            }
        }
        cartItems.value = currentCart
    }

    fun removeItemFromCart(product: Product) {
        val currentCart = cartItems.value.toMutableList()
        currentCart.removeAll { it.product.id == product.id }
        cartItems.value = currentCart
    }

    fun getTotal(): Double {
        return cartItems.value.sumOf { it.subtotal }
    }

    fun processPayment(onSuccess: (Double) -> Unit, onError: (String) -> Unit) {
        if (cartItems.value.isEmpty()) {
            onError("Agrega al menos un producto")
            return
        }

        val total = getTotal()
        viewModelScope.launch {
            salesUseCases.processSale(cartItems.value, total, client.value, paymentMethod.value)
            cartItems.value = emptyList() // Limpiar carrito
            onSuccess(total)
        }
    }
}