package com.tuusuario.vendefacil.data.repository

import android.util.Log
import com.tuusuario.vendefacil.core.utils.SessionManager
import com.tuusuario.vendefacil.data.remote.datasource.RemoteDataSource
import com.tuusuario.vendefacil.data.remote.dto.LoginRequest
import com.tuusuario.vendefacil.data.remote.dto.RegisterRequest
import com.tuusuario.vendefacil.domain.model.User
import com.tuusuario.vendefacil.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val sessionManager: SessionManager
) : AuthRepository {

    private val _currentUser = MutableStateFlow<User?>(
        if (sessionManager.obtenerUsuarioId() != null) {
            User(
                id = sessionManager.obtenerUsuarioId()!!,
                name = sessionManager.obtenerNombre(),
                businessName = sessionManager.obtenerNegocio(),
                email = sessionManager.obtenerEmail(),
                phone = sessionManager.obtenerTelefono(),
                token = ""
            )
        } else null
    )

    override suspend fun login(email: String, pass: String): Boolean {
        return try {
            val response = remoteDataSource.login(LoginRequest(email, pass))

            if (response.isSuccessful && response.body()?.success == true) {
                val userDto = response.body()!!.data.userData

                // Guardamos todo en el teléfono
                sessionManager.guardarUsuario(
                    id = userDto.userId,
                    nombre = userDto.name,
                    negocio = userDto.businessName,
                    email = userDto.email,
                    telefono = userDto.phone
                )
                _currentUser.value = User(
                    id = userDto.userId,
                    name = userDto.name,
                    businessName = userDto.businessName,
                    email = userDto.email,
                    phone = userDto.phone,
                    token = ""
                )

                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun register(name: String, business: String, email: String, phone: String, pass: String): Boolean {
        return try {
            val response = remoteDataSource.register(RegisterRequest(name, business, email, phone, pass))

            if (response.isSuccessful && response.body()?.success == true) {

                val userDto = response.body()!!.data.userData

                // Guardamos todo en el teléfono
                sessionManager.guardarUsuario(
                    id = userDto.userId,
                    nombre = userDto.name,
                    negocio = userDto.businessName,
                    email = userDto.email,
                    telefono = userDto.phone
                )
                _currentUser.value = User(
                    id = userDto.userId,
                    name = userDto.name,
                    businessName = userDto.businessName,
                    email = userDto.email,
                    phone = userDto.phone,
                    token = ""
                )
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun getCurrentUser(): StateFlow<User?> = _currentUser

    override fun logout() {
        sessionManager.cerrarSesion()
        _currentUser.value = null
    }
}