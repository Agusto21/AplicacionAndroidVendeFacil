package com.tuusuario.vendefacil.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.presentation.components.TopBarApp
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.TextDark

@Composable
fun SecurityScreen(onNavigateBack: () -> Unit) {
    var biometricsEnabled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Seguridad", onBack = onNavigateBack)

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Protección de la cuenta", fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 8.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Huella Dactilar / Face ID", fontWeight = FontWeight.Medium)
                            Text("Para iniciar sesión más rápido", color = Color.Gray, fontSize = 12.sp)
                        }
                        Switch(checked = biometricsEnabled, onCheckedChange = { biometricsEnabled = it })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { /* Lógica futura */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Cambiar Contraseña")
            }
        }
    }
}