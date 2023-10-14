package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


data class ItemOrder(
    val itemId: String,
    val orderQty: Int
)

class GetOrders(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.ordersFlow

    fun filter(orderStatus: OrderStatus, searchQuery: String): Flow<List<Order>> {
        return value.map {
            it.filter { it.orderStatus == orderStatus }
                .filter {
                    if (searchQuery.isNotEmpty()) {
                        return@filter it.contact.contains(
                            searchQuery,
                            ignoreCase = true
                        ) || it.clientName.contains(
                            searchQuery, ignoreCase = true
                        )
                    } else {
                        true
                    }
                }
        }
    }
}

class CreateOrder(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        clientName: String,
        contact: String,
        comment: String,
        totalPrice: Int,
        totalWeight: Double,
        itemsIds: List<ItemOrder>,
        orderStatus: OrderStatus,
    ) {
        val order = Order(
            clientName = clientName,
            comment = comment,
            totalPrice = totalPrice,
            totalWeight = totalWeight,
            itemsIds = itemsIds,
            contact = contact,
            orderStatus = orderStatus
        )
        database.addNewOrder(order)
    }
}
