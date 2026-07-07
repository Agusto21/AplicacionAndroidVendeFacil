package com.tuusuario.vendefacil.di

import android.content.Context
import com.tuusuario.vendefacil.core.utils.SessionManager
import com.tuusuario.vendefacil.data.remote.datasource.RemoteDataSource
import com.tuusuario.vendefacil.data.repository.AuthRepositoryImpl
import com.tuusuario.vendefacil.data.repository.ProductRepositoryImpl
import com.tuusuario.vendefacil.data.repository.SalesRepositoryImpl
import com.tuusuario.vendefacil.domain.usecase.AuthUseCases
import com.tuusuario.vendefacil.domain.usecase.ProductUseCases
import com.tuusuario.vendefacil.domain.usecase.SalesUseCases
import com.tuusuario.vendefacil.core.network.RetrofitClient

// 1. Pedimos el Context en el constructor
class AppContainer(private val context: Context) {

    // 2. Instanciamos el SessionManager usando ese context
    val sessionManager = SessionManager(context)

    // 3. Le pasamos el sessionManager a Retrofit
    val apiService = RetrofitClient.createRetrofit(sessionManager).create(com.tuusuario.vendefacil.core.network.ApiService::class.java)

    val remoteDataSource = RemoteDataSource(apiService)

    // 4. Le pasamos el sessionManager al repositorio de autenticación
    val authRepository = AuthRepositoryImpl(remoteDataSource, sessionManager)

    val productRepository = ProductRepositoryImpl(remoteDataSource)
    val salesRepository = SalesRepositoryImpl(remoteDataSource)

    val authUseCases = AuthUseCases(authRepository)
    val productUseCases = ProductUseCases(productRepository)
    val salesUseCases = SalesUseCases(salesRepository, productRepository)
}