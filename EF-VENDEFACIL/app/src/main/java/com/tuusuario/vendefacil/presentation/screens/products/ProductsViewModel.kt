package com.tuusuario.vendefacil.presentation.screens.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.usecase.ProductUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class ProductsViewModel(private val productUseCases: ProductUseCases) : ViewModel() {
    val products = MutableStateFlow<List<Product>>(emptyList())
    val showAddDialog = MutableStateFlow(false)
    val selectedProductToEdit = MutableStateFlow<Product?>(null)

    init {
        viewModelScope.launch {
            productUseCases.getProducts().collect { products.value = it }
        }
        viewModelScope.launch {
            productUseCases.fetchProducts()
        }
    }

    fun addProduct(name: String, cat: String, priceStr: String, stockStr: String) {
        val p = priceStr.toDoubleOrNull()
        val s = stockStr.toIntOrNull()
        if (name.isNotEmpty() && cat.isNotEmpty() && p != null && s != null) {
            viewModelScope.launch {
                productUseCases.addProduct(Product(name = name, category = cat, price = p, stock = s))
                showAddDialog.value = false
            }
        }
    }

    fun openEdit(product: Product) {
        selectedProductToEdit.value = product
    }

    fun updateProduct(id: String, name: String, cat: String, priceStr: String, stockStr: String) {
        val p = priceStr.toDoubleOrNull()
        val s = stockStr.toIntOrNull()
        if (name.isNotEmpty() && cat.isNotEmpty() && p != null && s != null) {
            viewModelScope.launch {
                productUseCases.updateProduct(Product(id = id, name = name, category = cat, price = p, stock = s))
                selectedProductToEdit.value = null
            }
        }
    }

    fun deleteProduct(id: String) {
        viewModelScope.launch {
            productUseCases.deleteProduct(id)
            selectedProductToEdit.value = null
        }
    }
}