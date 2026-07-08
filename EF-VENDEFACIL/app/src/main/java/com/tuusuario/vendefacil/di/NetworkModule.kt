package com.tuusuario.vendefacil.di

import android.content.Context
import com.tuusuario.vendefacil.core.network.ApiService
import com.tuusuario.vendefacil.core.network.RetrofitClient
import com.tuusuario.vendefacil.core.utils.SessionManager


class NetworkModule(private val context: Context) {


    val sessionManager: SessionManager by lazy {
        SessionManager(context)
    }


    val apiService: ApiService by lazy {
        RetrofitClient.createRetrofit(sessionManager).create(ApiService::class.java)
    }
}
