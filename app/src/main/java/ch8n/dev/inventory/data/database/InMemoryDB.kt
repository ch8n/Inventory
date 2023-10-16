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

    private val inventoryCategories = MutableStateFlow<List<InventoryCategory>>(emptyList())
    private val inventorySupplier = MutableStateFlow<List<InventorySupplier>>(emptyList())
    private val inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    private val orders = MutableStateFlow<List<Order>>(emptyList())

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