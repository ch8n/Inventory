package ch8n.dev.inventory.data.usecase

import androidx.compose.runtime.currentComposer
import ch8n.dev.inventory.UseCaseScope
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.database.firestore.OrderFS
import ch8n.dev.inventory.data.database.firestore.RemoteItemDAO
import ch8n.dev.inventory.data.database.firestore.RemoteOrderDAO
import ch8n.dev.inventory.data.database.roomdb.LocalItemDAO
import ch8n.dev.inventory.data.database.roomdb.LocalOrderDAO
import ch8n.dev.inventory.data.database.roomdb.OrderEntity
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


data class ItemOrder(
    val itemId: String,
    val orderQty: Int
)

fun OrderFS.toEntity(): OrderEntity {
    return OrderEntity(
        uid = documentReferenceId,
        clientName = clientName,
        contact = contact,
        comment = comment,
        totalPrice = totalPrice,
        totalWeight = totalWeight,
        itemsIds = itemsIds,
        orderStatus = orderStatus,
        createdAt = createdAt
    )
}

fun Order.toRemote(): OrderFS {
    return OrderFS(
        documentReferenceId = uid,
        clientName = clientName,
        contact = contact,
        comment = comment,
        totalPrice = totalPrice,
        totalWeight = totalWeight,
        itemsIds = itemsIds,
        orderStatus = orderStatus,
        createdAt = createdAt
    )
}

fun OrderEntity.toView(): Order {
    return Order(
        uid = uid,
        clientName = clientName,
        contact = contact,
        comment = comment,
        totalPrice = totalPrice,
        totalWeight = totalWeight,
        itemsIds = itemsIds,
        orderStatus = orderStatus,
        createdAt = createdAt
    )
}


class GetOrders(
    private val remoteOrderDAO: RemoteOrderDAO = DataModule.Injector.remoteDatabase.remoteOrderDAO,
    private val localOrderDAO: LocalOrderDAO = DataModule.Injector.localDatabase.localOrderDAO(),
) : UseCaseScope {

    val value = localOrderDAO.getAll()
        .distinctUntilChanged()
        .map { entities ->
            entities.map { it.toView() }
        }

    fun filter(orderStatus: OrderStatus, searchQuery: String): Flow<List<Order>> {
        return value.map { orders ->
            orders.filter {
                it.orderStatus == orderStatus
            }.filter {
                if (searchQuery.isEmpty()) return@filter true
                return@filter it.contact.contains(searchQuery, ignoreCase = true)
                        || it.clientName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    fun invalidate() {
        launch(NonCancellable) {
            val remoteItems = remoteOrderDAO.getAllOrders()
            localOrderDAO.insertAll(*remoteItems.map { it.toEntity() }.toTypedArray())
        }
    }
}

class CreateOrder(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        order: Order
    ) {
        database.addNewOrder(order)
    }
}

class UpdateOrder(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        currentOrder: Order,
        clientName: String = currentOrder.clientName,
        contact: String = currentOrder.contact,
        comment: String = currentOrder.comment,
        totalPrice: Int = currentOrder.totalPrice,
        totalWeight: Double = currentOrder.totalWeight,
        itemsIds: List<ItemOrder> = currentOrder.itemsIds,
        orderStatus: OrderStatus = currentOrder.orderStatus,
    ) {
        val order = currentOrder.copy(
            clientName = clientName,
            comment = comment,
            totalPrice = totalPrice,
            totalWeight = totalWeight,
            itemsIds = itemsIds,
            contact = contact,
            orderStatus = orderStatus
        )
        database.updateNewOrder(order)
    }
}

