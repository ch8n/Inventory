package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventorySupplier
import kotlinx.coroutines.flow.map


class GetInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventoryCategoriesFlow.map {
        ComposeStable(it)
    }
}


class CreateInventorySuppliers(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        suppliers: List<String>
    ) {
        val suppliers = suppliers.map { name ->
            InventorySupplier(name = name)
        }
        database.addSuppliers(suppliers)
    }
}

class CreateInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        name: String,
        sizes: List<String>
    ) {
        val category = InventoryCategory(
            name = name,
            sizes = sizes
        )
        database.addInventoryCategory(category)
    }
}

class UpdateInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        current: InventoryCategory,
        name: String = current.name,
        sizes: List<String> = current.sizes
    ) {
        val item = current.copy(
            name = name,
            sizes = sizes,
        )
        database.editInventoryCategory(item)
    }
}

class DeleteInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        categoryId: String,
    ) {
        database.deleteInventoryCategory(categoryId)
    }
}