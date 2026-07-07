package com.tuusuario.vendefacil.core.network

import com.tuusuario.vendefacil.data.remote.dto.ApiResponse
import com.tuusuario.vendefacil.data.remote.dto.LoginRequest
import com.tuusuario.vendefacil.data.remote.dto.LoginResponse
import com.tuusuario.vendefacil.data.remote.dto.ProductDto
import com.tuusuario.vendefacil.data.remote.dto.RegisterRequest
import com.tuusuario.vendefacil.data.remote.dto.TransactionDto
import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.model.Transaction
// Importa aquí tu ApiResponse y los modelos de Producto/Venta
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<ApiResponse<LoginResponse>>

    @POST("usuarios")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse<LoginResponse>>

    @GET("productos")
    suspend fun getProductos(): Response<ApiResponse<List<ProductDto>>>

    @POST("productos")
    suspend fun createProducto(@Body product: ProductDto): Response<ApiResponse<ProductDto>>

    @PUT("productos/{id}")
    suspend fun updateProducto(
        @Path("id") id: String,
        @Body product: ProductDto
    ): Response<ApiResponse<ProductDto>>

    @DELETE("productos/{id}")
    suspend fun deleteProducto(@Path("id") id: String): Response<Void>

    @GET("ventas")
    suspend fun getVentas(): Response<ApiResponse<List<TransactionDto>>>

    @POST("ventas")
    suspend fun createVenta(@Body transaction: TransactionDto): Response<ApiResponse<TransactionDto>>

    @PUT("ventas/{id}")
    suspend fun updateVenta(
        @Path("id") id: String,
        @Body transaction: TransactionDto
    ): Response<ApiResponse<TransactionDto>>

    @DELETE("ventas/{id}")
    suspend fun deleteVenta(@Path("id") id: String): Response<Void>
}