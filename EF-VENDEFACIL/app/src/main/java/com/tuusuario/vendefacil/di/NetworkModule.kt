package com.tuusuario.vendefacil.di

import android.content.Context
import com.tuusuario.vendefacil.core.network.ApiService
import com.tuusuario.vendefacil.core.network.RetrofitClient
import com.tuusuario.vendefacil.core.utils.SessionManager

// Añadimos el Context al constructor principal
class NetworkModule(private val context: Context) {

    // Instanciamos el SessionManager
    val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }

    // Le pasamos el SessionManager al RetrofitClient
    val apiService: ApiService by lazy {
        RetrofitClient.createRetrofit(sessionManager).create(ApiService::class.java)
    }
}