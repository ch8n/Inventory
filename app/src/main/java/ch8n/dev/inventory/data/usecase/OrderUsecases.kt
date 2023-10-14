package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.Order


data class ItemOrder(
    val itemId: String,
    val orderQty: Int
)

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
    ) {
        val order = Order(
            clientName = clientName,
            comment = comment,
            totalPrice = totalPrice,
            totalWeight = totalWeight,
            itemsIds = itemsIds,
            contact = contact
        )
        database.addNewOrder(order)
    }
}
