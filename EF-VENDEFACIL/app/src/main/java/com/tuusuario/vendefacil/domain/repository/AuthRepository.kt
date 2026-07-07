package com.tuusuario.vendefacil.domain.repository

import com.tuusuario.vendefacil.domain.model.User
import kotlinx.coroutines.flow.StateFlow

interface AuthRepository {
    suspend fun login(email: String, pass: String): Boolean
    suspend fun register(name: String, business: String, email: String, phone: String, pass: String): Boolean
    fun getCurrentUser(): StateFlow<User?>
    fun logout()
}
