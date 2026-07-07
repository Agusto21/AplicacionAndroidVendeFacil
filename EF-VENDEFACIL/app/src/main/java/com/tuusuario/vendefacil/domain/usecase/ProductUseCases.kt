package com.tuusuario.vendefacil.domain.usecase

import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.repository.ProductRepository

class ProductUseCases(private val repository: ProductRepository) {
    fun getProducts() = repository.getProducts()
    suspend fun fetchProducts() = repository.fetchProducts()
    suspend fun addProduct(product: Product) = repository.addProduct(product)
    suspend fun updateProduct(product: Product) = repository.updateProduct(product)
    suspend fun deleteProduct(productId: String) = repository.deleteProduct(productId)
    suspend fun updateStock(productId: String, quantityToSubtract: Int) = repository.updateStock(productId, quantityToSubtract)

    fun clearCache() = repository.clearCache()
}