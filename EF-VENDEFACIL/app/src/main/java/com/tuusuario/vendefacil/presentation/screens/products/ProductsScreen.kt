package com.tuusuario.vendefacil.presentation.screens.products

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.tuusuario.vendefacil.presentation.components.PrimaryButton
import com.tuusuario.vendefacil.presentation.components.TopBarApp
import com.tuusuario.vendefacil.presentation.theme.BackgroundLight
import com.tuusuario.vendefacil.presentation.theme.VendeFacilBlue

@Composable
fun ProductsScreen(appContainer: AppContainer, onNavigateBack: () -> Unit) {
    val viewModel = remember { ProductsViewModel(appContainer.productUseCases) }
    val products by viewModel.products.collectAsState()
    val showDialog by viewModel.showAddDialog.collectAsState()
    val editingProduct by viewModel.selectedProductToEdit.collectAsState()

    if (showDialog) {
        AddProductDialog(
            onDismiss = { viewModel.showAddDialog.value = false },
            onAdd = { n, c, p, s -> viewModel.addProduct(n, c, p, s) }
        )
    }

    if (editingProduct != null) {
        EditProductDialog(
            product = editingProduct!!,
            onDismiss = { viewModel.selectedProductToEdit.value = null },
            onUpdate = { n, c, p, s -> viewModel.updateProduct(editingProduct!!.id, n, c, p, s) },
            onDelete = { viewModel.deleteProduct(editingProduct!!.id) }
        )
    }

    Column(modifier = Modifier.fillMaxSize().background(BackgroundLight)) {
        TopBarApp("Productos", onBack = onNavigateBack)
        Column(modifier = Modifier.padding(16.dp)) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(products.size.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = VendeFacilBlue); Text("Total", color = Color.Gray) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(products.count { it.stock > 0 }.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF22C55E)); Text("En stock", color = Color.Gray) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(products.count { it.stock == 0 }.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Red); Text("Stock bajo", color = Color.Gray) }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton("+ Agregar Producto", onClick = { viewModel.showAddDialog.value = true })
            Spacer(modifier = Modifier.height(16.dp))
            Text("Todos los productos", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            LazyColumn {
                items(products) { p ->
                    ProductRow(p) { viewModel.openEdit(p) }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ProductRow(p: Product, onClick: () -> Unit) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), modifier = Modifier.fillMaxWidth().clickable { onClick() }) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(p.name, fontWeight = FontWeight.Bold)
                Text(p.category, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row {
                    Text("$${String.format("%.2f", p.price)}", color = VendeFacilBlue, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Stock: ${p.stock}", color = Color(0xFF22C55E), fontSize = 12.sp, modifier = Modifier.background(Color(0xFFDCFCE7)).padding(horizontal = 4.dp))
                }
            }
            Text("Editar", color = VendeFacilBlue, fontSize = 12.sp)
        }
    }
}


@Composable
fun EditProductDialog(product: Product, onDismiss: () -> Unit, onUpdate: (String, String, String, String) -> Unit, onDelete: () -> Unit) {
    var name by remember { mutableStateOf(product.name) }
    var cat by remember { mutableStateOf(product.category) }
    var price by remember { mutableStateOf(product.price.toString()) }
    var stock by remember { mutableStateOf(product.stock.toString()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Editar Producto") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = cat, onValueChange = { cat = it }, label = { Text("Categoría") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio ($)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it }, label = { Text("Stock") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = { Button(onClick = { onUpdate(name, cat, price, stock) }) { Text("Guardar") } },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TextButton(onClick = onDelete, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) { Text("Eliminar") }
                TextButton(onClick = onDismiss) { Text("Cancelar") }
            }
        }
    )
}

@Composable
fun AddProductDialog(onDismiss: () -> Unit, onAdd: (String, String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var cat by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo Producto") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it.filter { char -> char.isLetterOrDigit() || char.isWhitespace() } }, label = { Text("Nombre") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = cat, onValueChange = { cat = it.filter { char -> char.isLetter() || char.isWhitespace() } }, label = { Text("Categoría (solo letras)") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Precio ($)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = stock, onValueChange = { stock = it.filter { char -> char.isDigit() } }, label = { Text("Stock (solo números)") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
            }
        },
        confirmButton = { Button(onClick = { onAdd(name, cat, price, stock) }) { Text("Agregar") } },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
