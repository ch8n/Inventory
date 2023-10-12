package ch8n.dev.inventory.data.database

import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventoryItemVariant
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
                    name = "Tracie Howard",
                    sizes = listOf()
                ),
                itemVariant = listOf(
                    InventoryItemVariant(
                        id = "nonumes",
                        color = "tempus",
                        quantity = 4939,
                        size = "2.2"
                    ),
                    InventoryItemVariant(
                        id = "praesent",
                        color = "vestibulum",
                        quantity = 5842,
                        size = "2.4"
                    )
                ),
                totalQuantity = 20,
                weight = 6.7,
                supplier = InventorySupplier(
                    id = "fusce",
                    name = "Marcie Barlow"
                ),
                sellingPrice = 9861,
                purchasePrice = 9662
            ),
            InventoryItem(
                id = "epicuri",
                name = "Wiley Ryan",
                images = listOf(),
                category = InventoryCategory(
                    id = "montes",
                    name = "Bernard McMahon",
                    sizes = listOf()
                ),
                itemVariant = listOf(
                    InventoryItemVariant(
                        id = "nonumes",
                        color = "tempus",
                        quantity = 4939,
                        size = "2.2"
                    ),
                    InventoryItemVariant(
                        id = "praesent",
                        color = "vestibulum",
                        quantity = 5842,
                        size = "2.4"
                    )
                ),
                totalQuantity = 4212,
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

    val inventoryCategoriesFlow = inventoryCategories.asStateFlow()
    val inventoryItemsFlow = inventoryItems.asStateFlow()
    val inventorySupplierFlow = inventorySupplier.asStateFlow()

    fun addSuppliers(suppliers: List<InventorySupplier>) {
        inventorySupplier.update { current ->
            (current + suppliers).distinctBy { it.name }
        }
    }

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