package com.tuusuario.vendefacil.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.di.AppContainer
import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.SuccessGreen
import com.tuusuario.vendefacil.presentation.theme.VendeFacilBlue

@Composable
fun DashboardScreen(appContainer: AppContainer, onNavigateToCharge: () -> Unit, onNavigateToProducts: () -> Unit, onNavigateToHistory: () -> Unit, onNavigateToProfile: () -> Unit) {
    val viewModel = remember { DashboardViewModel(appContainer.authUseCases, appContainer.salesUseCases) }
    val user by viewModel.user.collectAsState()
    val earnings by viewModel.earnings.collectAsState()
    val recent by viewModel.recentSales.collectAsState()
    val period by viewModel.selectedPeriod.collectAsState()
    val showSeeAll by viewModel.showSeeAll.collectAsState()

    if (showSeeAll) {
        SeeAllScreen(viewModel = viewModel, onBack = { viewModel.showSeeAll.value = false })
        return
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        Box(modifier = Modifier.fillMaxWidth().background(VendeFacilBlue, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)).padding(24.dp)) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column {
                        Text("Bienvenido,", color = Color.White.copy(0.8f))
                        Text(user?.name ?: "Usuario", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        if (!user?.businessName.isNullOrBlank()) {
                            Text(user?.businessName ?: "", color = Color.White.copy(0.8f))
                        }
                    }
                    Box(modifier = Modifier.size(40.dp).background(Color.White.copy(0.2f), CircleShape).clickable { onNavigateToProfile() }, contentAlignment = Alignment.Center) {
                        Text("P", color = Color.White)
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            var expanded by remember { mutableStateOf(false) }
                            Box {
                                Text("Ventas de ${period.lowercase()} ▼", color = Color.Gray, modifier = Modifier.clickable { expanded = true })
                                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                    DropdownMenuItem(text = { Text("Hoy") }, onClick = { viewModel.setPeriod("Hoy"); expanded = false })
                                    DropdownMenuItem(text = { Text("Semana") }, onClick = { viewModel.setPeriod("Semana"); expanded = false })
                                    DropdownMenuItem(text = { Text("Mes") }, onClick = { viewModel.setPeriod("Mes"); expanded = false })
                                }
                            }
                        }
                        Text("$${String.format("%.2f", earnings.total)}", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Text("${earnings.transactionCount} transacciones", color = Color.Gray)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Acciones Rápidas", fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onNavigateToCharge, modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).height(50.dp), colors = ButtonDefaults.buttonColors(containerColor = VendeFacilBlue)) {
            Text("Cobrar Venta", color = Color.White)
        }
        Row(modifier = Modifier.fillMaxWidth().padding(24.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Card(modifier = Modifier.weight(1f).clickable { onNavigateToProducts() }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Box(modifier = Modifier.padding(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Productos") }
            }
            Card(modifier = Modifier.weight(1f).clickable { onNavigateToHistory() }, colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Box(modifier = Modifier.padding(16.dp).fillMaxWidth(), contentAlignment = Alignment.Center) { Text("Historial") }
            }
        }

        Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Últimas Ventas", fontWeight = FontWeight.Bold)
            if (viewModel.allSales.value.size > 10) {
                Text("Ver Todo", color = VendeFacilBlue, fontSize = 12.sp, modifier = Modifier.clickable { viewModel.openSeeAll() })
            }
        }

        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
            items(recent) { item ->
                TransactionRow(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllScreen(viewModel: DashboardViewModel, onBack: () -> Unit) {
    val paginatedSales by viewModel.paginatedSales.collectAsState()
    val allSales by viewModel.allSales.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopAppBar(
            title = { Text("Últimas Ventas", color = Color.White, fontWeight = FontWeight.Bold) },
            navigationIcon = { IconButton(onClick = onBack) { Text("<", color = Color.White, fontSize = 24.sp) } },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = VendeFacilBlue)
        )

        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(paginatedSales) { item ->
                TransactionRow(item)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (paginatedSales.size < allSales.size && paginatedSales.size < viewModel.maxLimit) {
                item {
                    TextButton(onClick = { viewModel.loadMore() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Ver Más Ventas", color = VendeFacilBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionRow(t: Transaction) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Text(t.productName, fontWeight = FontWeight.Bold)
                Text(t.timestamp, color = Color.Gray, fontSize = 12.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("$${String.format("%.2f", t.total)}", fontWeight = FontWeight.Bold)
                Text(t.status, color = SuccessGreen, fontSize = 12.sp)
            }
        }
    }
}