package com.tuusuario.vendefacil.domain.repository

import com.tuusuario.vendefacil.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    suspend fun fetchProducts()

    suspend fun addProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(productId: String)
    suspend fun updateStock(productId: String, quantityToSubtract: Int)

    fun clearCache()
}