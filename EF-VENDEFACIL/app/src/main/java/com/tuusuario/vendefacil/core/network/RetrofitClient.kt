package com.tuusuario.vendefacil.core.network

import com.tuusuario.vendefacil.core.utils.Constants
import com.tuusuario.vendefacil.core.utils.SessionManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    fun createRetrofit(sessionManager: SessionManager): Retrofit {

        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            val userId = sessionManager.obtenerUsuarioId()
            if (!userId.isNullOrEmpty()) {
                requestBuilder.addHeader("x-usuario-id", userId)
            }
            chain.proceed(requestBuilder.build())
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}