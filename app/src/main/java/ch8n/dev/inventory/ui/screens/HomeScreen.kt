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
import ch8n.dev.inventory.Destinations
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@Composable
fun HomeScreen() {

    val store = LocalAppStore.current
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
                        navigator.goto(Destinations.CreateSupplierScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Create Suppliers")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(Destinations.CreateCategoryScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Create Category")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(Destinations.CreateItemScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Create/Update Item")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(Destinations.CreateOrderScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Create Order")
                }
            }

            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(Destinations.ManageOrdersScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Manage Order")
                }
            }

        }
    }
}