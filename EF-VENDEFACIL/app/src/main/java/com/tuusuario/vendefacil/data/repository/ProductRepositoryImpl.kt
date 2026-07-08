package com.tuusuario.vendefacil.data.repository

import android.util.Log
import com.tuusuario.vendefacil.data.mapper.toDomain
import com.tuusuario.vendefacil.data.mapper.toDto
import com.tuusuario.vendefacil.data.mapper.toUpdateDto
import com.tuusuario.vendefacil.data.remote.datasource.RemoteDataSource
import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class ProductRepositoryImpl(
    private val remoteDataSource: RemoteDataSource 
) : ProductRepository {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    override fun getProducts(): Flow<List<Product>> = _products


    override suspend fun fetchProducts() {
        try {
            val response = remoteDataSource.getProductos()
            if (response.isSuccessful && response.body()?.success == true) {
                val dtoList = response.body()!!.data
                _products.value = dtoList.map { it.toDomain() }
            } else {
                Log.e("API_PRODUCTOS", "Error al obtener: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_PRODUCTOS", "Fallo de red: ${e.message}")
        }
    }

    override suspend fun addProduct(product: Product) {
        try {
            val response = remoteDataSource.createProducto(product.toDto())
            if (response.isSuccessful && response.body()?.success == true) {
                fetchProducts() 
            }
        } catch (e: Exception) {
            Log.e("API_PRODUCTOS", "Fallo al crear: ${e.message}")
        }
    }

    override suspend fun updateProduct(product: Product) {
        if (product.id.isEmpty()) return

        try {
            val response = remoteDataSource.updateProducto(product.id, product.toUpdateDto())
            if (response.isSuccessful && response.body()?.success == true) {
                fetchProducts()
            } else {
                Log.e("API_PRODUCTOS", "AWS Rechazó edición: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Log.e("API_PRODUCTOS", "Fallo al actualizar: ${e.message}")
        }
    }

    override suspend fun deleteProduct(productId: String) {
        try {
            val response = remoteDataSource.deleteProducto(productId)
            if (response.isSuccessful) {
                fetchProducts()
            }
        } catch (e: Exception) {
            Log.e("API_PRODUCTOS", "Fallo al eliminar: ${e.message}")
        }
    }

    override suspend fun updateStock(productId: String, quantityToSubtract: Int) {
        val p = _products.value.find { it.id == productId } ?: return

        val updatedProduct = p.copy(stock = p.stock - quantityToSubtract)

        updateProduct(updatedProduct)
    }

    override fun clearCache() {
        _products.value = emptyList()
    }
}
