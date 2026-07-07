package com.tuusuario.vendefacil.domain.usecase

import com.tuusuario.vendefacil.domain.repository.AuthRepository

class AuthUseCases(private val repository: AuthRepository) {
    suspend fun login(email: String, pass: String) = repository.login(email, pass)
    suspend fun register(name: String, business: String, email: String, phone: String, pass: String) = repository.register(name, business, email, phone, pass)
    fun getCurrentUser() = repository.getCurrentUser()
    fun logout() = repository.logout()
}