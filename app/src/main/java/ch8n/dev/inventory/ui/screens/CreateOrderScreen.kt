package ch8n.dev.inventory.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
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
import ch8n.dev.inventory.Destinations
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateOrderScreen() {

    val store = LocalAppStore.current
    val navigator = LocalNavigator.current
    val searchQuery by store.query.collectAsState("")
    var selectedCategory by rememberMutableState(init = InventoryCategory.Empty)
    val items by store.getQueryItem.collectAsState(initial = ComposeStable(emptyList()))

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
                    onValueChange = {
                        store.query.tryEmit(it)
                    },
                    label = { Text(text = "Search Item") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            store.query.tryEmit("")
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item {
                val categories by store.getCategory
                    .value.collectAsState(initial = ComposeStable(emptyList()))

                val dropdownOptions = remember(categories.value) {
                    categories.value.map { it.name }.toList().let { ComposeStable(it) }
                }

                OptionDropDown(
                    title = "Select Category",
                    dropdownOptions = dropdownOptions,
                    onSelected = { index ->
                        selectedCategory = categories.value.get(index)
                    }
                )

                AnimatedVisibility(visible = selectedCategory != InventoryCategory.Empty) {
                    OutlinedTextField(
                        value = selectedCategory.name,
                        onValueChange = {},
                        label = { Text(text = "Option Selected") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.sdp),
                        readOnly = true,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkGray
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                selectedCategory = InventoryCategory.Empty
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            }
                        }
                    )
                }
            }

            itemsIndexed(items.value) { index, item ->


                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.sdp, Color.Gray)
                        .padding(8.sdp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.sdp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(150.sdp)
                                .border(2.sdp, Color.DarkGray)
                        )

                        Column {
                            Text(text = item.name)
                            Text(text = "Selling ${item.sellingPrice}")
                            Text(text = "Weight ${item.weight}")
                            Text(text = "Total Qty : ${item.totalQuantity}")
                        }
                    }

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.sdp, Color.Gray)
                                .padding(4.sdp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "color")
                            Text(text = "Stock")
                            Text(text = "Size")
                            Text(text = "Order")
                        }
                        item.itemVariant.forEach { varient ->

                            var quanitity by rememberMutableState(init = 0)

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(2.sdp, Color.Gray)
                                    .padding(4.sdp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = varient.color)
                                Text(text = "${varient.quantity}")
                                Text(text = "${varient.size}")

                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    IconButton(onClick = { quanitity += 1 }) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowUp,
                                            contentDescription = null
                                        )
                                    }

                                    Text(
                                        text = quanitity.toString(),
                                        color = Color.DarkGray,
                                        fontSize = 14.ssp
                                    )

                                    IconButton(onClick = { quanitity -= 1 }) {
                                        Icon(
                                            imageVector = Icons.Rounded.KeyboardArrowDown,
                                            contentDescription = null
                                        )
                                    }
                                }
                            }
                        }
                    }
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