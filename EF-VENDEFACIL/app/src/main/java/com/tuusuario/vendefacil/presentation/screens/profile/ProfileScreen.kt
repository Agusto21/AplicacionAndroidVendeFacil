package com.tuusuario.vendefacil.presentation.screens.profile

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.di.AppContainer
import com.tuusuario.vendefacil.presentation.components.TopBarApp
import com.tuusuario.vendefacil.presentation.theme.*

@Composable
fun ProfileScreen(
    appContainer: AppContainer,
    onNavigateBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToSecurity: () -> Unit,
    onNavigateToHelp: () -> Unit
) {
    val currentUserState by appContainer.authUseCases.getCurrentUser().collectAsState()

    // Mantenemos el estado de usuario congelado para evitar el parpadeo al cerrar sesión
    var displayUser by remember { mutableStateOf(currentUserState) }
    LaunchedEffect(currentUserState) {
        if (currentUserState != null) displayUser = currentUserState
    }

    val user = displayUser

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Mi Perfil", onBack = onNavigateBack)

        Box(modifier = Modifier.fillMaxWidth().background(VendeFacilBlue).padding(bottom = 24.dp), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(modifier = Modifier.size(80.dp).background(Color.White, CircleShape), contentAlignment = Alignment.Center) {
                    Text(user?.name?.take(1) ?: "U", fontSize = 32.sp, color = VendeFacilBlue)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(user?.name ?: "Usuario", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)

                if(!user?.businessName.isNullOrEmpty()) {
                    Text(user?.businessName ?: "", color = Color.White.copy(0.8f))
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text("Información Personal", fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Email", color = Color.Gray, fontSize = 12.sp)
                    Text(user?.email ?: "Sin correo", fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Teléfono", color = Color.Gray, fontSize = 12.sp)
                    Text(user?.phone ?: "Sin teléfono", fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Configuración", fontWeight = FontWeight.Bold, color = TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Column {
                    SettingsRowItem(title = "Notificaciones", onClick = onNavigateToNotifications)
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    SettingsRowItem(title = "Seguridad", onClick = onNavigateToSecurity)
                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
                    SettingsRowItem(title = "Ayuda y Soporte", onClick = onNavigateToHelp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    appContainer.authUseCases.logout()
                    onLogout()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DeleteRed)
            ) {
                Text("Cerrar Sesión", color = Color.White)
            }
        }
    }
}

@Composable
fun SettingsRowItem(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, fontWeight = FontWeight.Medium, color = TextDark)
        Text(">", color = Color.LightGray, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}