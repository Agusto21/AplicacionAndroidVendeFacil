package com.tuusuario.vendefacil.presentation.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.tuusuario.vendefacil.domain.model.Product
import com.tuusuario.vendefacil.domain.model.Transaction
import com.tuusuario.vendefacil.presentation.components.TopBarApp
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.SuccessGreen
import com.tuusuario.vendefacil.presentation.theme.VendeFacilBlue

@Composable
fun HistoryScreen(appContainer: AppContainer, onNavigateBack: () -> Unit) {
    val viewModel = remember { HistoryViewModel(appContainer.salesUseCases, appContainer.productUseCases) }
    val transactions by viewModel.transactions.collectAsState()
    val total by viewModel.totalSales.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val allProducts by viewModel.allProducts.collectAsState()

    var selectedTransactionId by remember { mutableStateOf<String?>(null) }
    val selectedTransaction = transactions.find { it.id == selectedTransactionId }

    if (selectedTransaction != null) {
        EditTransactionDialog(
            transaction = selectedTransaction,
            allProducts = allProducts,
            onDismiss = { selectedTransactionId = null },
            onRemoveItem = { itemToRemove ->
                viewModel.removeItemFromTransaction(selectedTransaction, itemToRemove)
            },
            onAddItem = { product, quantity ->
                viewModel.addItemToTransaction(selectedTransaction, product, quantity)
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Historial", onBack = onNavigateBack)
        Column(modifier = Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.searchQuery.value = it },
                label = { Text("Buscar por cliente...") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                shape = RoundedCornerShape(12.dp)
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Total Ventas", color = Color.Gray, fontSize = 12.sp)
                        Text("$${String.format("%.2f", total)}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VendeFacilBlue)
                    }
                }
                Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Transacciones", color = Color.Gray, fontSize = 12.sp)
                        Text(transactions.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = SuccessGreen)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                // key = { it.id } ayuda a Compose a reciclar y actualizar elementos más eficientemente
                items(transactions, key = { it.id }) { t ->
                    HistoryRow(t) { selectedTransactionId = t.id }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun HistoryRow(t: Transaction, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(t.productName, fontWeight = FontWeight.Bold)
                    Text("${t.timestamp} • ${t.clientName}", color = Color.Gray, fontSize = 12.sp)
                }
                Text("$${String.format("%.2f", t.total)}", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(t.paymentMethod, color = Color.Gray, fontSize = 12.sp)
                Row {
                    Text("Cant: ${t.quantity}", color = Color.Gray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(t.status, color = SuccessGreen, fontSize = 12.sp, modifier = Modifier.background(Color(0xFFDCFCE7)).padding(horizontal = 4.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionDialog(
    transaction: Transaction,
    allProducts: List<Product>,
    onDismiss: () -> Unit,
    onRemoveItem: (com.tuusuario.vendefacil.domain.model.TransactionItem) -> Unit,
    onAddItem: (Product, Int) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        com.tuusuario.vendefacil.presentation.screens.charge.AddToCartDialog(
            products = allProducts,
            onDismiss = { showAddDialog = false },
            onAdd = { p, q -> onAddItem(p, q); showAddDialog = false }
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Venta - ${transaction.clientName}") },
        text = {
            Column {
                Text("Nuevo Total: $${String.format("%.2f", transaction.total)}", fontWeight = FontWeight.Bold, color = VendeFacilBlue)
                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {

                    items(transaction.items, key = { it.product.id }) { item ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(item.product.name, fontSize = 14.sp)
                                Text("x${item.quantity} - $${item.subtotal}", fontSize = 12.sp, color = Color.Gray)
                            }
                            IconButton(onClick = { onRemoveItem(item) }) { Text("X", color = Color.Red) }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = { showAddDialog = true }) { Text("+ Agregar Producto") }
            }
        },

        confirmButton = { Button(onClick = onDismiss) { Text("Cerrar") } }
    )
}