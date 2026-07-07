package com.tuusuario.vendefacil.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.presentation.components.TopBarApp
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.TextDark

@Composable
fun HelpSupportScreen(onNavigateBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Ayuda y Soporte", onBack = onNavigateBack)

        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(24.dp))

            Text("¿En qué podemos ayudarte?", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(24.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Preguntas Frecuentes", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• ¿Cómo edito una venta?", color = Color.DarkGray, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• ¿Dónde actualizo mi stock?", color = Color.DarkGray, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedButton(onClick = { /* Lógica futura */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Contactar al Soporte Técnico")
            }

            Spacer(modifier = Modifier.weight(1f))
            Text("VendeFácil v1.0.0", color = Color.Gray, fontSize = 12.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}