package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalNavigator
import ch8n.dev.inventory.*


@Composable
fun HomeContent() {
    val navigator = LocalNavigator.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.sdp)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.sdp)
        ) {
            item {
                Text(
                    text = "HomeScreen",
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
                        navigator.goto(ManageSupplierScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Manage Suppliers")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(ManageCategoryScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Manage Category")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(ManageItemScreen())
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Manage Inventory Item")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(CreateOrderScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Create Order")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(ManageOrdersScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Manage Order")
                }
            }

        }
    }
}