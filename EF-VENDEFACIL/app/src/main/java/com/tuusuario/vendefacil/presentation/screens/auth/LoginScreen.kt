package com.tuusuario.vendefacil.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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

class LoginViewModel(private val authUseCases: AuthUseCases) : ViewModel() {
    val isLoading = MutableStateFlow(false)
    val loginSuccess = MutableStateFlow(false)
    fun login(email: String, pass: String) {
        viewModelScope.launch {
            isLoading.value = true
            loginSuccess.value = authUseCases.login(email, pass)
            isLoading.value = false
        }
    }
}

@Composable
fun LoginScreen(appContainer: AppContainer, onNavigateToDashboard: () -> Unit, onNavigateToRegister: () -> Unit) {
    val viewModel = remember { LoginViewModel(appContainer.authUseCases) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val success by viewModel.loginSuccess.collectAsState()

    LaunchedEffect(success) { if (success) onNavigateToDashboard() }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Box(modifier = Modifier.size(80.dp).background(VendeFacilBlue, CircleShape), contentAlignment = Alignment.Center) {
            Text("$", fontSize = 40.sp, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(40.dp))
        Text("Iniciar Sesión", fontSize = 32.sp, color = Color.Black, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Text("Accede a tu cuenta de VendeFácil", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email, onValueChange = { email = it }, label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password, onValueChange = { password = it }, label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(32.dp))

        if (isLoading) CircularProgressIndicator(color = VendeFacilBlue)
        else PrimaryButton("Ingresar", onClick = { viewModel.login(email, password) })

        Spacer(modifier = Modifier.height(24.dp))
        Row {
            Text("¿No tienes cuenta? ", color = Color.Gray)
            Text("Regístrate aquí", color = VendeFacilBlue, fontWeight = FontWeight.Bold, modifier = Modifier.clickable { onNavigateToRegister() })
        }
    }
}