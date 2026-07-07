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
fun NotificationsScreen(onNavigateBack: () -> Unit) {
    var pushEnabled by remember { mutableStateOf(true) }
    var emailEnabled by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Notificaciones", onBack = onNavigateBack)

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Preferencias", fontWeight = FontWeight.Bold, color = TextDark, modifier = Modifier.padding(bottom = 8.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Notificaciones Push", fontWeight = FontWeight.Medium)
                            Text("Avisos de ventas y alertas", color = Color.Gray, fontSize = 12.sp)
                        }
                        Switch(checked = pushEnabled, onCheckedChange = { pushEnabled = it })
                    }
                    HorizontalDivider(color = Color.LightGray.copy(0.3f))
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Correos Electrónicos", fontWeight = FontWeight.Medium)
                            Text("Resúmenes diarios y noticias", color = Color.Gray, fontSize = 12.sp)
                        }
                        Switch(checked = emailEnabled, onCheckedChange = { emailEnabled = it })
                    }
                }
            }
        }
    }
}