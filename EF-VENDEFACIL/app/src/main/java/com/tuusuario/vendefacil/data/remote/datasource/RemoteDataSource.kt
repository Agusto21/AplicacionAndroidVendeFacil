package com.tuusuario.vendefacil.data.remote.datasource

import com.tuusuario.vendefacil.core.network.ApiService
import com.tuusuario.vendefacil.data.remote.dto.LoginRequest
import com.tuusuario.vendefacil.data.remote.dto.LoginResponse
import com.tuusuario.vendefacil.data.remote.dto.ProductDto
import com.tuusuario.vendefacil.data.remote.dto.RegisterRequest
import com.tuusuario.vendefacil.data.remote.dto.TransactionDto
import kotlinx.coroutines.delay

class RemoteDataSource(private val apiService: ApiService) {
    // Dentro de tu RemoteDataSource.kt
    suspend fun login(request: LoginRequest) = apiService.login(request)
    suspend fun register(request: RegisterRequest) = apiService.register(request)

    suspend fun createVenta(transactionDto: TransactionDto) = apiService.createVenta(transactionDto)
    suspend fun getVentas() = apiService.getVentas()
    suspend fun updateVenta(id: String, transactionDto: TransactionDto) = apiService.updateVenta(id, transactionDto)
    suspend fun deleteVenta(id: String) = apiService.deleteVenta(id)

    suspend fun getProductos() = apiService.getProductos()
    suspend fun createProducto(productDto: ProductDto) = apiService.createProducto(productDto)
    suspend fun updateProducto(id: String, productDto: ProductDto) = apiService.updateProducto(id, productDto)
    suspend fun deleteProducto(id: String) = apiService.deleteProducto(id)
}