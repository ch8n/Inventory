package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
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
import ch8n.dev.inventory.Destinations
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen() {

    val store = LocalAppStore.current
    val navigator = LocalNavigator.current
    var searchQuery by remember { mutableStateOf("") }
    val items by store.getItems.value.collectAsState(initial = ComposeStable(emptyList()))

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
                    text = "OrderScreen",
                    fontSize = 32.ssp,
                    color = Color.DarkGray
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }


            item {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .height(150.sdp)
                        .border(2.sdp, Color.DarkGray)
                        .padding(24.sdp),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {
                    Text(
                        text = "Order Summary",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    RowSummaryText(key = "Item Total Price", value = "0")
                    RowSummaryText(key = "Item Total Weight", value = "0")
                }
            }

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text(text = "Search Item") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    )
                )
            }

            itemsIndexed(items.value) { index, item ->
                Column(
                    modifier = Modifier
                        .padding(24.sdp)
                        .fillMaxWidth()
                        .border(2.sdp, Color.Gray)
                        .padding(8.sdp)
                ) {
                    Text(text = item.name)
                }
            }


            item {
                OutlinedButton(
                    onClick = {
                        navigator.goto(Destinations.CreateItemScreen)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Create Order")
                }
            }

        }
    }
}

@Composable
fun RowSummaryText(
    key: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = key,
            color = Color.DarkGray,
            fontSize = 16.ssp
        )
        Text(
            text = value,
            color = Color.DarkGray,
            fontSize = 16.ssp
        )
    }
}