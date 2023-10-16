package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateOrderContent(order: Order) {

    val scope = rememberCoroutineScope()
    val store = LocalAppStore.current
    val navigator = LocalNavigator.current
    var clientName by rememberMutableState(init = order.clientName)
    var clientContact by rememberMutableState(init = order.contact)
    var comment by rememberMutableState(init = order.comment)
    val totalPrice by rememberMutableState(init = order.totalPrice)
    val totalWeight by rememberMutableState(init = order.totalWeight)
    var orderStatus by rememberMutableState(init = order.orderStatus)
    val itemsIds by rememberMutableState(init = order.itemsIds)

    //TODO item listing add/remove more items to order??
    // update order status

    LazyColumn(
        Modifier
            .padding(24.sdp)
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
    ) {

        item {
            Text(
                text = "Update Order",
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
                    .padding(vertical = 16.sdp)
                    .fillMaxWidth()
                    .height(300.sdp)
                    .border(2.sdp, Color.DarkGray)
                    .padding(24.sdp),
                verticalArrangement = Arrangement.spacedBy(8.sdp)
            ) {

                Text(
                    text = "Order",
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
                            clientName = order.clientName
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
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
                            clientContact = order.contact
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Refresh,
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
            val orderStatuses = OrderStatus.values()

            OptionDropDown(
                title = "Order Status ${orderStatus.name}",
                dropdownOptions = orderStatuses.map { it.name },
                onSelected = { index ->
                    orderStatus = orderStatuses.get(index)
                }
            )
        }

        itemsIndexed(itemsIds) { index, itemOrder ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(2.sdp, Color.Gray)
                    .padding(8.sdp)
            ) {

                val item = remember(itemOrder) { store.getItems.getOne(itemOrder.itemId) }

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
                            Text(text = "Selling : ${item.sellingPrice}")
                            Text(text = "Order Qty : ${itemOrder.orderQty}")
                        }
                    }
                }
            }
        }


        item {
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text(text = "Any Special Comment/Notes?") },
                modifier = Modifier
                    .padding(16.sdp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = Color.DarkGray
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        comment = order.comment
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Refresh,
                            contentDescription = null
                        )
                    }
                }
            )
        }

        item {
            OutlinedButton(
                onClick = {
                    store.updateOrder.execute(
                        clientName = clientName,
                        contact = clientContact,
                        comment = comment,
                        totalPrice = totalPrice,
                        totalWeight = totalWeight,
                        itemsIds = itemsIds,
                        orderStatus = orderStatus
                    )
                    navigator.back()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "+ Update Order")
            }
        }

    }
}

