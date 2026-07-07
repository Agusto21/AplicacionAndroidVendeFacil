package com.tuusuario.vendefacil.presentation.screens.charge

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tuusuario.vendefacil.di.AppContainer
import com.tuusuario.vendefacil.presentation.components.PrimaryButton
import com.tuusuario.vendefacil.presentation.components.TopBarApp
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.SuccessGreen
import com.tuusuario.vendefacil.presentation.theme.VendeFacilBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChargeScreen(appContainer: AppContainer, onNavigateBack: () -> Unit, onNavigateToSuccess: (Double) -> Unit) {
    val viewModel = remember { ChargeViewModel(appContainer.productUseCases, appContainer.salesUseCases) }
    val products by viewModel.products.collectAsState()
    val cartItems by viewModel.cartItems.collectAsState()
    val client by viewModel.client.collectAsState()
    val method by viewModel.paymentMethod.collectAsState()

    var errorMsg by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        AddToCartDialog(
            products = products,
            onDismiss = { showAddDialog = false },
            onAdd = { p, q -> viewModel.addItemToCart(p, q); showAddDialog = false }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Cobrar Venta", onBack = onNavigateBack)

        LazyColumn(modifier = Modifier.padding(16.dp).weight(1f)) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Cliente y Pago", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = client, onValueChange = { viewModel.client.value = it }, label = { Text("Cliente (opcional)") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            listOf("Tarjeta", "Transfer", "Efectivo").forEach { m ->
                                OutlinedButton(
                                    onClick = { viewModel.paymentMethod.value = m },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(containerColor = if (method == m) VendeFacilBlue.copy(alpha = 0.1f) else Color.Transparent)
                                ) { Text(m, color = if (method == m) VendeFacilBlue else Color.Gray) }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text("Productos a cobrar", fontWeight = FontWeight.Bold)
                    TextButton(onClick = { showAddDialog = true }) { Text("+ Agregar") }
                }
            }

            items(cartItems) { item ->
                Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text(item.product.name, fontWeight = FontWeight.Bold)
                            Text("Cant: ${item.quantity} x $${item.product.price}", color = Color.Gray, fontSize = 12.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("$${String.format("%.2f", item.subtotal)}", fontWeight = FontWeight.Bold)
                            IconButton(onClick = { viewModel.removeItemFromCart(item.product) }) {
                                Text("X", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            if (errorMsg.isNotEmpty()) {
                Text(errorMsg, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(bottom = 8.dp))
            }
            Box(modifier = Modifier.fillMaxWidth().background(VendeFacilBlue, RoundedCornerShape(12.dp)).padding(16.dp)) {
                Column {
                    Text("Total a cobrar", color = Color.White.copy(0.8f))
                    Text("$${String.format("%.2f", viewModel.getTotal())}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton("Procesar Pago", onClick = { viewModel.processPayment(onNavigateToSuccess, { errorMsg = it }) }, color = SuccessGreen, textColor = Color.White)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddToCartDialog(products: List<com.tuusuario.vendefacil.domain.model.Product>, onDismiss: () -> Unit, onAdd: (com.tuusuario.vendefacil.domain.model.Product, Int) -> Unit) {
    var selected by remember { mutableStateOf<com.tuusuario.vendefacil.domain.model.Product?>(null) }
    var qtyStr by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar al carrito") },
        text = {
            Column {
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                    OutlinedTextField(
                        value = selected?.name ?: "Elige un producto",
                        onValueChange = {}, readOnly = true, modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        products.forEach { p ->
                            if (p.stock > 0) {
                                DropdownMenuItem(text = { Text("${p.name} (Disp: ${p.stock})") }, onClick = { selected = p; expanded = false })
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(value = qtyStr, onValueChange = { qtyStr = it.filter { c->c.isDigit() } }, label = { Text("Cantidad") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                val q = qtyStr.toIntOrNull() ?: 0
                if (selected != null && q > 0) onAdd(selected!!, q)
            }) { Text("Agregar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}