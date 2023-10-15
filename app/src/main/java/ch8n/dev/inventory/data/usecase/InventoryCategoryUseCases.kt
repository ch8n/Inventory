package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventorySupplier


class GetInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventoryCategoriesFlow
}


class CreateInventorySuppliers(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        supplier: String
    ) {
        database.addSupplier(InventorySupplier(name = supplier))
    }
}

class CreateInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        category : InventoryCategory
    ) {
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