package com.tuusuario.vendefacil.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuusuario.vendefacil.di.AppContainer
import com.tuusuario.vendefacil.domain.usecase.AuthUseCases
import com.tuusuario.vendefacil.presentation.components.PrimaryButton
import com.tuusuario.vendefacil.presentation.theme.VendeFacilBlue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(private val authUseCases: AuthUseCases) : ViewModel() {
    val isLoading = MutableStateFlow(false)
    val success = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun register(name: String, bus: String, email: String, phone: String, pass: String, hasBusiness: Boolean) {
        if (name.isBlank() || email.isBlank() || phone.isBlank() || pass.isBlank()) {
            errorMessage.value = "Por favor llena todos los campos obligatorios."
            return
        }
        if (hasBusiness && bus.isBlank()) {
            errorMessage.value = "Ingresa el nombre de tu negocio."
            return
        }

        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""
            val finalBusName = if (hasBusiness) bus else ""
            val res = authUseCases.register(name, finalBusName, email, phone, pass)
            if (res) {
                success.value = true
            } else {
                errorMessage.value = "Error al crear la cuenta. Intenta de nuevo."
            }
            isLoading.value = false
        }
    }
}

@Composable
fun RegisterScreen(appContainer: AppContainer, onNavigateToLogin: () -> Unit) {
    val viewModel = remember { RegisterViewModel(appContainer.authUseCases) }
    var name by remember { mutableStateOf("") }
    var hasBusiness by remember { mutableStateOf(false) }
    var business by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isLoading by viewModel.isLoading.collectAsState()
    val success by viewModel.success.collectAsState()
    val errorMsg by viewModel.errorMessage.collectAsState()

    LaunchedEffect(success) { if (success) onNavigateToLogin() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(60.dp).background(VendeFacilBlue, CircleShape), contentAlignment = Alignment.Center) {
            Text("$", fontSize = 30.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Crear Cuenta", fontSize = 28.sp, color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre completo *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Checkbox(checked = hasBusiness, onCheckedChange = { hasBusiness = it })
            Text("¿Tienes un negocio?")
        }

        if (hasBusiness) {
            OutlinedTextField(value = business, onValueChange = { business = it }, label = { Text("Nombre del negocio *") }, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo electrónico *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono *") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña *") }, visualTransformation = PasswordVisualTransformation(), modifier = Modifier.fillMaxWidth())

        if (errorMsg.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(errorMsg, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
        if (isLoading) CircularProgressIndicator(color = VendeFacilBlue)
        else PrimaryButton("Crear cuenta", onClick = { viewModel.register(name, business, email, phone, password, hasBusiness) })

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Text("¿Ya tienes cuenta? ", color = Color.Gray)
            Text("Inicia sesión", color = VendeFacilBlue, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToLogin() })
        }
    }
}