package ch8n.dev.inventory.data.database

import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventorySupplier
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object InMemoryDB {

    private val inventoryCategories = MutableStateFlow<List<InventoryCategory>>(
        listOf(
            InventoryCategory(
                name = "Bangles",
                sizes = listOf(
                    "2.2", "2.4", "2.6", "2.8"
                )
            ),
            InventoryCategory(
                name = "Tangle",
                sizes = listOf(
                    "2.2", "2.4", "2.6", "2.8"
                )
            )
        )
    )
    private val inventorySupplier = MutableStateFlow<List<InventorySupplier>>(
        listOf(
            InventorySupplier(
                name = "lovely"
            ),
            InventorySupplier(
                name = "Tubly"
            ),
        )
    )
    private val inventoryItems = MutableStateFlow<List<InventoryItem>>(
        listOf(
            InventoryItem(
                id = "suscipiantur",
                name = "Dawn Strong",
                images = listOf(),
                category = InventoryCategory(
                    id = "mazim",
                    name = "Bangles",
                    sizes = listOf()
                ),
                itemQuantity = 20,
                weight = 6.7,
                supplier = InventorySupplier(
                    id = "fusce",
                    name = "Lovely"
                ),
                sellingPrice = 9861,
                purchasePrice = 9662,
                itemSize = "2.2",
                itemColor = "Red"
            ),
            InventoryItem(
                id = "epicuri",
                name = "Wiley Ryan",
                images = listOf(),
                category = InventoryCategory(
                    id = "montes",
                    name = "Bangles",
                    sizes = listOf()
                ),
                itemSize = "2.4",
                itemColor = "Blue",
                itemQuantity = 4212,
                weight = 10.11,
                supplier = InventorySupplier(
                    id = "his",
                    name = "Jay Wall"
                ),
                sellingPrice = 5741,
                purchasePrice = 8908
            )
        )
    )

    private val orders = MutableStateFlow(
        listOf<Order>(
            Order(
                id = "utinam",
                clientName = "Jeremiah Cline 1",
                contact = "curabitur 1",
                comment = "sed",
                totalPrice = 2036,
                totalWeight = 4.5,
                itemsIds = listOf(),
                orderStatus = OrderStatus.NEW_ORDER,
                createdAt = 6257
            ),
            Order(
                id = "utinam",
                clientName = "Jeremiah Cline 2",
                contact = "curabitur  2",
                comment = "sed",
                totalPrice = 2036,
                totalWeight = 4.5,
                itemsIds = listOf(),
                orderStatus = OrderStatus.NEW_ORDER,
                createdAt = 6257
            ),
            Order(
                id = "utinam",
                clientName = "Jeremiah Cline 3",
                contact = "curabitur 3",
                comment = "sed",
                totalPrice = 2036,
                totalWeight = 4.5,
                itemsIds = listOf(),
                orderStatus = OrderStatus.NEW_ORDER,
                createdAt = 6257
            ),

            Order(
                id = "utinam",
                clientName = "Jeremiah Cline 3",
                contact = "curabitur 3",
                comment = "sed",
                totalPrice = 2036,
                totalWeight = 4.5,
                itemsIds = listOf(),
                orderStatus = OrderStatus.DELIVERED,
                createdAt = 6257
            ),
        )
    )

    val inventoryCategoriesFlow = inventoryCategories.asStateFlow()
    val inventoryItemsFlow = inventoryItems.asStateFlow()
    val inventorySupplierFlow = inventorySupplier.asStateFlow()
    val ordersFlow = orders.asStateFlow()

    fun addSupplier(supplier: InventorySupplier) {
        inventorySupplier.update { current ->
            (current + supplier).distinctBy { it.name.lowercase() }
        }
    }

    fun deleteSupplier(supplier: InventorySupplier) {
        inventorySupplier.update { current ->
            current.filter { it.id != supplier.id }
        }
    }

    fun addInventoryCategory(category: InventoryCategory) {
        inventoryCategories.update { it + category }
    }

    fun editInventoryCategory(category: InventoryCategory) {
        inventoryCategories.update { current ->
            current.filter { it.name != category.name } + category
        }
    }

    fun deleteInventoryCategory(categoryId: String) {
        inventoryCategories.update { current ->
            current.filter { it.id != categoryId }
        }
    }

    fun upsertInventoryItem(item: InventoryItem) {
        inventoryItems.update { current ->
            current.filter { it.id != item.id } + item
        }
    }

    fun deleteInventoryItem(itemId: String) {
        inventoryItems.update { current ->
            current.filter { it.id != itemId }
        }
    }

    fun addNewOrder(order: Order) {
        orders.update { current ->
            current + order
        }
    }

    fun updateNewOrder(order: Order) {
        orders.update { current ->
            current.filter { it.id != order.id } + order
        }
    }
}