package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class GetInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventoryItemsFlow

    fun getOne(id:String): InventoryItem? {
        return value.value.find { it.id == id }
    }

    fun filter(
        searchQuery: String,
        selectedCategory: InventoryCategory
    ): Flow<List<InventoryItem>> {
        return value.map { items ->
            items
                .filter {
                    if (selectedCategory == InventoryCategory.Empty) return@filter true
                    return@filter it.category.name.equals(selectedCategory.name)
                }
                .filter {
                    if (searchQuery.isNotEmpty()) {
                        return@filter it.name.contains(searchQuery, ignoreCase = true)
                    } else {
                        true
                    }
                }

        }
    }
}


class UpsertInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        item: InventoryItem
    ) {
        database.upsertInventoryItem(item)
    }
}

class DeleteInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        itemId: String,
    ) {
        database.deleteInventoryItem(itemId)
    }
}