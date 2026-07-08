package com.tuusuario.vendefacil.presentation.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.presentation.theme.VendeFacilBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToLogin: () -> Unit) {
    var loadingText by remember { mutableStateOf("Cargando") }

    LaunchedEffect(Unit) {
        var dotCount = 0
        while (dotCount < 6) {
            delay(500)
            dotCount++
            loadingText = "Cargando" + ".".repeat(dotCount % 4)
        }
        onNavigateToLogin()
    }
    Column(
        modifier = Modifier.fillMaxSize().background(VendeFacilBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier.size(100.dp).background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("$", fontSize = 60.sp, color = VendeFacilBlue, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("VendeFácil", fontSize = 36.sp, color = Color.White, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Acepta pagos digitales y gestiona", color = Color.White.copy(alpha = 0.8f))
        Text("tu negocio desde tu celular", color = Color.White.copy(alpha = 0.8f))
        Spacer(modifier = Modifier.height(40.dp))
        Text(loadingText, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
