package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import ch8n.dev.inventory.ImagePreviewScreen
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventorySupplier
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.data.usecase.ItemOrder
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.toast
import ch8n.dev.inventory.ui.LocalNavigator
import ch8n.dev.inventory.ui.LocalUseCaseProvider
import coil.compose.AsyncImage
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun UpdateOrderContent(
    originalOrder: Order,
    shortlistedItem: Map<String, Int>,
    updateShortListedItem: (updated: Map<String, Int>) -> Unit,
    selectedOrderStatus: OrderStatus,
    onUpdateOrderStatus: (updated: OrderStatus) -> Unit,
    clientName: String,
    updateClientName: (String) -> Unit,
    clientContact: String,
    updateClientContact: (String) -> Unit,
    orderComment: String,
    updateOrderComment: (String) -> Unit,
    searchQuery: String,
    updateSearchQuery: (updated: String) -> Unit,
    selectedCategory: InventoryCategory,
    updateSelectedCategory: (updated: InventoryCategory) -> Unit,
    selectedSupplier: InventorySupplier,
    updateSelectedSupplier: (updated: InventorySupplier) -> Unit,
    initialScrollPosition: Int,
    onScrollPositionChanged: (position: Int) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val userCaseProvider = LocalUseCaseProvider.current
    val navigator = LocalNavigator.current
    val items by userCaseProvider.getItems
        .filter(searchQuery, selectedCategory, selectedSupplier)
        .collectAsState(initial = emptyList())

    val context = LocalContext.current

    BottomSheet(
        sheetContent = { bottomSheet ->
            SearchItemBottomSheetContent(
                onSelect = { item ->
                    val current = shortlistedItem.toMutableMap()
                    current.put(item.uid, 0)
                    updateShortListedItem.invoke(current)
                    scope.launch {
                        bottomSheet.hide()
                    }
                },
                onDelete = { item ->
                    // nothing will happen
                },
                searchQuery = searchQuery,
                updateSearchQuery = updateSearchQuery,
                selectedCategory = selectedCategory,
                updateSelectedCategory = updateSelectedCategory,
                initialScrollPosition = initialScrollPosition,
                onScrollPositionChanged = onScrollPositionChanged,
                selectedSupplier = selectedSupplier,
                updateSelectedSupplier = updateSelectedSupplier
            )
        },
        backgroundContent = { bottomSheet ->

            val totalPrice = shortlistedItem.entries.map { (key, value) ->
                val found = items.find { it.uid == key } ?: return@map 0
                found.itemSellingPrice * value
            }.sum()

            val totalWeight = shortlistedItem.entries.map { (key, value) ->
                val found = items.find { it.uid == key } ?: return@map 0.0
                found.itemWeight * value
            }.sum()

            LazyColumn(
                Modifier
                    .padding(24.sdp)
                    .fillMaxWidth()
                    .fillMaxHeight()
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
                            onValueChange = updateClientName,
                            label = { Text(text = "Client Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.DarkGray
                            ),
                            trailingIcon = {
                                IconButton(onClick = {
                                    updateClientName.invoke("")
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
                            onValueChange = updateClientContact,
                            label = { Text(text = "Contact Number") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                textColor = Color.DarkGray
                            ),
                            trailingIcon = {
                                IconButton(onClick = {
                                    updateClientContact.invoke("")
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
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        OptionDropDown(
                            title = "Order Status ${selectedOrderStatus.name}",
                            dropdownOptions = orderStatus.map { it.name },
                            onSelected = { index ->
                                onUpdateOrderStatus.invoke(orderStatus.get(index))
                            }
                        )
                    }

                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        OutlinedButton(
                            onClick = {
                                scope.launch {
                                    bottomSheet.show()
                                }
                            }) {
                            Text(
                                text = "+ Add Item",
                                fontSize = 14.ssp
                            )
                        }
                    }
                }

                itemsIndexed(shortlistedItem.entries.toList()) { index, (itemId, orderQty) ->
                    Column(
                        modifier = Modifier
                            .padding(vertical = 8.sdp)
                            .fillMaxWidth()
                            .border(2.sdp, Color.Gray)
                            .padding(8.sdp)
                    ) {

                        val item = items.find { it.uid == itemId }

                        if (item != null) {

                            val currentItemStock = remember(shortlistedItem) {
                                item.itemQuantity - (shortlistedItem.get(
                                    item.uid
                                ) ?: 0)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.sdp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                val imageUri = item.itemImage.toUri()

                                Box(
                                    modifier = Modifier
                                        .size(150.sdp)
                                        .border(2.sdp, Color.DarkGray)
                                        .clickable {
                                            navigator.goto(ImagePreviewScreen(uri = imageUri))
                                        }
                                ) {
                                    AsyncImage(
                                        model = imageUri,
                                        contentDescription = null,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }

                                Column {
                                    Text(text = item.itemName)
                                    Text(text = "Color : ${item.itemColor}")
                                    Text(text = "Size : ${item.itemSize}")
                                    Text(text = "Weight : ${item.itemWeight}")
                                    Text(text = "Total Quantity : $currentItemStock")
                                    Text(text = "Selling : ${item.itemSellingPrice}")
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        val current = shortlistedItem.toMutableMap()
                                        current.remove(item.uid)
                                        updateShortListedItem.invoke(current)
                                    },
                                ) {
                                    Text(
                                        text = "Delete Item",
                                        fontSize = 14.ssp
                                    )
                                }

                                Row(
                                    horizontalArrangement = Arrangement.SpaceAround,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {

                                    IconButton(onClick = {
                                        if (currentItemStock > 0) {
                                            val updated = orderQty + 1
                                            val current = shortlistedItem.toMutableMap()
                                            if (updated > 0) {
                                                current.put(itemId, updated)
                                            } else {
                                                current.remove(itemId)
                                            }
                                            updateShortListedItem.invoke(current)
                                        }
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
                                        val current = shortlistedItem.toMutableMap()
                                        if (updated > 0) {
                                            current.put(itemId, updated)
                                        } else {
                                            current.remove(itemId)
                                        }
                                        updateShortListedItem.invoke(current)
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
                }

                item {
                    OutlinedTextField(
                        value = orderComment,
                        onValueChange = updateOrderComment,
                        label = { Text(text = "Any Special Comment/Notes?") },
                        modifier = Modifier
                            .padding(vertical = 16.sdp)
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkGray
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                updateOrderComment.invoke("")
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
                            if (clientName.isEmpty()) return@OutlinedButton context.toast("Empty Name!")
                            if (clientContact.length != 10) return@OutlinedButton context.toast("10 digit Contact! (${clientContact.length})")
                            val orders = shortlistedItem.entries.filter { it.value > 0 }
                            if (orders.isEmpty()) return@OutlinedButton context.toast("Cart is Empty!")

                            userCaseProvider.updateOrder.execute(
                                originalOrder = originalOrder,
                                updatedOrder = originalOrder.copy(
                                    clientName = clientName,
                                    contact = clientContact,
                                    comment = orderComment,
                                    totalPrice = totalPrice,
                                    totalWeight = totalWeight,
                                    itemsIds = shortlistedItem.entries.map { (key, value) ->
                                        ItemOrder(key, value)
                                    },
                                    orderStatus = selectedOrderStatus,
                                    createdAt = originalOrder.createdAt
                                )
                            )
                            context.toast("Updating order!")
                            navigator.back()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "+ Update Order")
                    }
                }

            }
        }
    )
}


