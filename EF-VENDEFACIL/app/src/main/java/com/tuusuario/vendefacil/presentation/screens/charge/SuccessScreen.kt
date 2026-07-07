package com.tuusuario.vendefacil.presentation.screens.charge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.SuccessGreen

@Composable
fun SuccessScreen(amount: Double, onNavigateToDashboard: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().background(BackgroundLight).padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.size(100.dp).background(SuccessGreen, CircleShape), contentAlignment = Alignment.Center) {
            Text("✓", color = Color.White, fontSize = 60.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("¡Pago Exitoso!", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
        Text("La transacción se procesó correctamente", color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        Text("$${String.format("%.2f", amount)}", fontSize = 40.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(40.dp))
        Button(onClick = onNavigateToDashboard, modifier = Modifier.fillMaxWidth().height(50.dp)) {
            Text("Volver al inicio")
        }
    }
}