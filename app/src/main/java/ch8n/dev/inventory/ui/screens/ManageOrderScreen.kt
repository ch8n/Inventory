package ch8n.dev.inventory.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.rememberMutableState
import ch8n.dev.inventory.sdp
import ch8n.dev.inventory.ssp
import ch8n.dev.inventory.ui.LocalAppStore
import ch8n.dev.inventory.ui.LocalNavigator


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ManageOrderScreen() {

    val scope = rememberCoroutineScope()
    val store = LocalAppStore.current
    val navigator = LocalNavigator.current

    val orderStatuses = OrderStatus.values()
    val pageState = rememberPagerState()

    HorizontalPager(
        pageCount = orderStatuses.size,
        state = pageState
    ) { pageIndex ->

        val orderStatus = orderStatuses.get(pageIndex)
        var searchQuery by rememberMutableState(init = "")
        val orders by store.getOrders.filter(orderStatus, searchQuery)
            .collectAsState(emptyList())

        LazyColumn {

            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    label = { Text(text = "Search by Contact or Email") },
                    modifier = Modifier
                        .padding(16.sdp)
                        .fillMaxWidth(),
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
                Text(
                    text = "${orderStatus.name} Tab",
                    fontSize = 32.ssp,
                    color = Color.DarkGray
                )
            }

            item {
                Divider(modifier = Modifier.padding(bottom = 24.sdp))
            }

            items(orders) { order ->
                Column(
                    modifier = Modifier
                        .padding(16.sdp)
                        .fillMaxWidth()
                        .border(2.sdp, Color.DarkGray)
                        .padding(8.sdp)
                ) {
                    Text(text = order.clientName)
                    Text(text = "Contact : ${order.contact}")
                    Text(text = "Ordered At : ${order.createdAt}")
                    Text(text = "Total Order Items : ${order.itemsIds.size}")
                    Text(text = "Total Weight : ${order.totalWeight} gms")
                    Text(text = "Total Price : Rs.${order.totalPrice}")
                    Text(
                        text = "Comment : ${order.comment}",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 2
                    )
                }
            }
        }

    }

}
