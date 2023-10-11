package ch8n.dev.inventory.data.database

import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventorySupplier
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
    private val inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())
    private val inventorySupplier = MutableStateFlow<List<InventorySupplier>>(emptyList())

    val inventoryCategoriesFlow = inventoryCategories.asStateFlow()
    val inventoryItemsFlow = inventoryItems.asStateFlow()
    val inventorySupplierFlow = inventorySupplier.asStateFlow()

    fun addInventoryCategory(category: InventoryCategory) {
        val current = inventoryCategories.value
        val found = current.find { it.name.equals(category.name, ignoreCase = true) }
        if (found != null) {
            inventoryCategories.update { it + category }
        }
    }

    fun editInventoryCategory(category: InventoryCategory) {
        inventoryCategories.update { current ->
            current.filter { it.name != category.name } + category
        }
    }

    fun deleteInventoryCategory(categoryName: String) {
        inventoryCategories.update { current ->
            current.filter { it.name != categoryName }
        }
    }

    fun addInventoryItem(item: InventoryItem) {
        inventoryItems.update { it + item }
    }

    fun editInventoryItem(updated: InventoryItem) {
        inventoryItems.update { current ->
            current.filter { it.id != updated.id } + updated
        }
    }

    fun deleteInventoryItem(itemId: String) {
        inventoryItems.update { current ->
            current.filter { it.id != itemId }
        }
    }
}