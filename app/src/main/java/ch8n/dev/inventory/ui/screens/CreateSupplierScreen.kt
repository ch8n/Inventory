package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSupplierScreen() {

    val store = LocalAppStore.current
    val navigator = LocalNavigator.current
    var suppliersName by remember { mutableStateOf(ComposeStable(emptyList<String>())) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.sdp)
    ) {

        LazyColumn(
            modifier = Modifier.padding(bottom = 64.sdp),
            verticalArrangement = Arrangement.spacedBy(8.sdp)
        ) {

            item {
                Text(
                    text = "Create Supplier",
                    fontSize = 32.ssp,
                    color = Color.DarkGray
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }

            item {
                OutlinedButton(
                    onClick = {
                        val current = suppliersName.value
                        suppliersName = ComposeStable(current + "")
                    }
                ) {
                    Text(text = "+ Add Suppliers Option")
                }
            }

            itemsIndexed(suppliersName.value) { index, supplierName ->

                OutlinedTextField(
                    value = supplierName,
                    onValueChange = {
                        val current = suppliersName.value.toMutableList()
                        current.set(index, it)
                        suppliersName = ComposeStable(current)
                    },
                    label = { Text(text = "Option ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.sdp),
                    trailingIcon = {
                        IconButton(onClick = {
                            val current = suppliersName.value.toMutableList()
                            current.removeAt(index)
                            suppliersName = ComposeStable(current)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )

            }

        }

        OutlinedButton(
            onClick = {
                store.createSuppliers.execute(suppliersName.value)
                navigator.back()
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "+ Add Suppliers")
        }
    }
}
