package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.UseCaseScope
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.firestore.OrderFS
import ch8n.dev.inventory.data.database.firestore.RemoteOrderDAO
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
    private val remoteOrderDAO: RemoteOrderDAO = DataModule.Injector.remoteDatabase.remoteOrderDAO,
    private val localOrderDAO: LocalOrderDAO = DataModule.Injector.localDatabase.localOrderDAO(),
) : UseCaseScope {
    fun execute(
        order: Order
    ) {
        launch(NonCancellable) {
            val remoteOrder = remoteOrderDAO.createOrder(order)
            localOrderDAO.insertAll(remoteOrder.toEntity())
        }
    }
}

class UpdateOrder(
    private val remoteOrderDAO: RemoteOrderDAO = DataModule.Injector.remoteDatabase.remoteOrderDAO,
    private val localOrderDAO: LocalOrderDAO = DataModule.Injector.localDatabase.localOrderDAO(),
) : UseCaseScope {
    fun execute(
        originalOrder: Order,
        updatedOrder: Order,
    ) {
        launch(NonCancellable) {
            val remoteOrder = remoteOrderDAO.updateOrder(
                original = originalOrder,
                updated = updatedOrder
            )
            localOrderDAO.insertAll(remoteOrder.toEntity())
        }
    }
}

