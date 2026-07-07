package com.tuusuario.vendefacil.core.utils

import android.content.Context
import androidx.core.content.edit

class SessionManager(context: Context) {
    // Mantenemos tu nombre de archivo original
    private val prefs = context.getSharedPreferences("vende_facil_prefs", Context.MODE_PRIVATE)

    // Usamos el bloque 'edit' de KTX que ya tenías
    fun guardarUsuario(id: String, nombre: String, negocio: String, email: String, telefono: String) {
        prefs.edit {
            putString("USUARIO_ID", id)
            putString("USUARIO_NOMBRE", nombre)
            putString("USUARIO_NEGOCIO", negocio)
            putString("USUARIO_EMAIL", email)
            putString("USUARIO_TELEFONO", telefono)
        }
    }

    fun obtenerUsuarioId(): String? = prefs.getString("USUARIO_ID", null)
    fun obtenerNombre(): String = prefs.getString("USUARIO_NOMBRE", "Usuario") ?: "Usuario"
    fun obtenerNegocio(): String = prefs.getString("USUARIO_NEGOCIO", "Negocio") ?: "Negocio"
    fun obtenerEmail(): String = prefs.getString("USUARIO_EMAIL", "Sin correo") ?: "Sin correo"
    fun obtenerTelefono(): String = prefs.getString("USUARIO_TELEFONO", "Sin teléfono") ?: "Sin teléfono"

    fun cerrarSesion() {
        prefs.edit { clear() }
    }
}