package ch8n.dev.inventory.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.rememberModalBottomSheetState
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.data.usecase.ItemOrder
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CreateOrderScreen() {

    val scope = rememberCoroutineScope()
    val store = LocalAppStore.current
    val navigator = LocalNavigator.current
    var searchQuery by rememberMutableState(init = "")
    var selectedCategory by rememberMutableState(init = InventoryCategory.Empty)
    val items by store.getItems.filter(searchQuery, selectedCategory)
        .collectAsState(initial = emptyList())
    var shortlistItem by rememberMutableState(init = mapOf<String, Int>())
    var selectedOrderStatus by rememberMutableState<OrderStatus>(init = OrderStatus.NEW_ORDER)

    BottomSheetSelectedOrders(
        content = { bottomSheet ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.sdp)
            ) {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.sdp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.9f)
                ) {
                    item {
                        Text(
                            text = "Create Order Screen",
                            fontSize = 32.ssp,
                            color = Color.DarkGray
                        )
                    }

                    item {
                        Divider(modifier = Modifier.padding(bottom = 24.sdp))
                    }

                    item {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = {
                                searchQuery = it
                            },
                            label = { Text(text = "Search Item") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.DarkGray
                            ),
                            trailingIcon = {
                                IconButton(onClick = {
                                    searchQuery = ""
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

                    itemsIndexed(items) { index, item ->

                        val totalQuantity = item.itemQuantity - (shortlistItem.get(item.id) ?: 0)

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
                                    Text(text = "Color : ${item.itemColor}")
                                    Text(text = "Size : ${item.itemSize}")
                                    Text(text = "Weight : ${item.weight}")
                                    Text(text = "Total Quantity : $totalQuantity")
                                    Text(text = "Selling : ${item.sellingPrice}")
                                }
                            }

                            Row(
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.align(Alignment.End)
                            ) {

                                IconButton(onClick = {
                                    val orderQty = shortlistItem.get(item.id) ?: 0
                                    val updated = orderQty + 1
                                    val current = shortlistItem.toMutableMap()
                                    current.put(item.id, updated)
                                    shortlistItem = current
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowUp,
                                        contentDescription = null
                                    )
                                }

                                Text(
                                    text = (shortlistItem.get(item.id) ?: 0).toString(),
                                    color = Color.DarkGray,
                                    fontSize = 14.ssp
                                )

                                IconButton(onClick = {
                                    val orderQty = shortlistItem.get(item.id) ?: 0
                                    val updated = orderQty - 1
                                    val current = shortlistItem.toMutableMap()
                                    current.put(item.id, updated)
                                    shortlistItem = current
                                }) {
                                    Icon(
                                        imageVector = Icons.Rounded.KeyboardArrowDown,
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }

                OutlinedButton(
                    onClick = {
                        scope.launch {
                            bottomSheet.show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "+ Check Order Items")
                }
            }
        }
    ) { bottomSheet ->

        var clientName by rememberMutableState(init = "")
        var clientContact by rememberMutableState(init = "")
        var orderComment by rememberMutableState(init = "")

        val totalPrice = shortlistItem.entries.map { (key, value) ->
            val found = items.find { it.id == key } ?: return@map 0
            found.sellingPrice * value
        }.sum()

        val totalWeight = shortlistItem.entries.map { (key, value) ->
            val found = items.find { it.id == key } ?: return@map 0.0
            found.weight * value
        }.sum()

        LazyColumn(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
        ) {
            item {
                Column(
                    Modifier
                        .padding(vertical = 16.sdp)
                        .fillMaxWidth()
                        .height(300.sdp)
                        .border(2.sdp, Color.DarkGray)
                        .padding(24.sdp),
                    verticalArrangement = Arrangement.spacedBy(8.sdp)
                ) {

                    Text(
                        text = "Order Summary",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text(text = "Client Name") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkGray
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                clientName = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    OutlinedTextField(
                        value = clientContact,
                        onValueChange = { clientContact = it },
                        label = { Text(text = "Contact Number") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkGray
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                clientContact = ""
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Clear,
                                    contentDescription = null
                                )
                            }
                        }
                    )

                    RowSummaryText(key = "Item Total Price", value = "$totalPrice")
                    RowSummaryText(key = "Item Total Weight", value = "$totalWeight")
                }
            }

            item {
                val orderStatus = OrderStatus.values()

                OptionDropDown(
                    title = "Order Status ${selectedOrderStatus.name}",
                    dropdownOptions = ComposeStable(orderStatus.map { it.name }),
                    onSelected = { index ->
                        selectedOrderStatus = orderStatus.get(index)
                    }
                )
            }

            itemsIndexed(shortlistItem.entries.toList()) { index, (itemId, orderQty) ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.sdp, Color.Gray)
                        .padding(8.sdp)
                ) {

                    val item = remember(itemId) { items.find { it.id == itemId } }

                    if (item != null) {

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
                                Text(text = "Color : ${item.itemColor}")
                                Text(text = "Size : ${item.itemSize}")
                                Text(text = "Weight : ${item.weight}")
                                Text(
                                    text = "Total Quantity : ${
                                        item.itemQuantity - (shortlistItem.get(
                                            item.id
                                        ) ?: 0)
                                    }"
                                )
                                Text(text = "Selling : ${item.sellingPrice}")
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.align(Alignment.End)
                        ) {

                            IconButton(onClick = {
                                val updated = orderQty + 1
                                val current = shortlistItem.toMutableMap()
                                if (updated > 0) {
                                    current.put(itemId, updated)
                                } else {
                                    current.remove(itemId)
                                }
                                shortlistItem = current
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowUp,
                                    contentDescription = null
                                )
                            }

                            Text(
                                text = orderQty.toString(),
                                color = Color.DarkGray,
                                fontSize = 14.ssp
                            )

                            IconButton(onClick = {
                                val updated = orderQty - 1
                                val current = shortlistItem.toMutableMap()
                                if (updated > 0) {
                                    current.put(itemId, updated)
                                } else {
                                    current.remove(itemId)
                                }
                                shortlistItem = current
                            }) {
                                Icon(
                                    imageVector = Icons.Rounded.KeyboardArrowDown,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }


            item {
                OutlinedTextField(
                    value = orderComment,
                    onValueChange = { orderComment = it },
                    label = { Text(text = "Any Special Comment/Notes?") },
                    modifier = Modifier
                        .padding(16.sdp)
                        .fillMaxWidth(),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkGray
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            orderComment = ""
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item {
                OutlinedButton(
                    onClick = {
                        store.createOrder.execute(
                            clientName = clientName,
                            contact = clientContact,
                            comment = orderComment,
                            totalPrice = totalPrice,
                            totalWeight = totalWeight,
                            itemsIds = shortlistItem.entries.map { (key, value) ->
                                ItemOrder(key, value)
                            },
                            orderStatus = selectedOrderStatus
                        )
                        navigator.back()
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


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomSheetSelectedOrders(
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true
    ),
    content: @Composable (sheetState: ModalBottomSheetState) -> Unit,
    sheetContent: @Composable ColumnScope.(sheetState: ModalBottomSheetState) -> Unit,
) {

    ModalBottomSheetLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.sdp, topEnd = 16.sdp),
        sheetContent = {
            sheetContent(sheetState)
        },
        content = {
            content.invoke(sheetState)
        }
    )
}
