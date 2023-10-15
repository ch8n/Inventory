package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircle
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageSupplierScreen() {

    val store = LocalAppStore.current
    val navigator = LocalNavigator.current

    val suppliers by store.getSupplier.value.collectAsState()
    var newSupplier by rememberMutableState(init = "")

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
                    text = "Manage Supplier",
                    fontSize = 32.ssp,
                    color = Color.DarkGray
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }

            item {
                OutlinedTextField(
                    value = newSupplier,
                    onValueChange = {
                        newSupplier = it
                    },
                    label = { Text(text = "+ Add New Supplier") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.sdp),
                    trailingIcon = {
                        IconButton(onClick = {
                            store.createSuppliers.execute(newSupplier)
                            newSupplier = ""
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.AddCircle,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }


            itemsIndexed(suppliers) { index, supplier ->

                OutlinedTextField(
                    value = supplier.name,
                    onValueChange = {},
                    label = { Text(text = "Supplier ${index + 1}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.sdp),
                    trailingIcon = {
                        IconButton(onClick = {
                            store.deleteSupplier.execute(supplier)
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    ),
                    readOnly = true
                )

            }

        }
    }
}
