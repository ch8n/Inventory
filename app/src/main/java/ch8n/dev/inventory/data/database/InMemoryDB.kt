package ch8n.dev.inventory.data.database

import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object InMemoryDB {

    private val inventoryCategories = MutableStateFlow<List<InventoryCategory>>(emptyList())
    private val inventoryItems = MutableStateFlow<List<InventoryItem>>(emptyList())

    val inventoryCategoriesFlow = inventoryCategories.asStateFlow()
    val inventoryItemsFlow = inventoryItems.asStateFlow()

    fun addInventoryCategory(category: InventoryCategory) {
        inventoryCategories.update { it + category }
    }

    fun editInventoryCategory(category: InventoryCategory) {
        inventoryCategories.update { current ->
            current.filter { it.categoryId != category.categoryId } + category
        }
    }

    fun deleteInventoryCategory(categoryId: String) {
        inventoryCategories.update { current ->
            current.filter { it.categoryId != categoryId }
        }
    }

    fun addInventoryItem(item: InventoryItem) {
        inventoryItems.update { it + item }
    }

    fun editInventoryItem(updated: InventoryItem) {
        inventoryItems.update { current ->
            current.filter { it.itemId != updated.itemId } + updated
        }
    }

    fun deleteInventoryItem(itemId:String) {
        inventoryItems.update { current ->
            current.filter { it.itemId != itemId }
        }
    }
}