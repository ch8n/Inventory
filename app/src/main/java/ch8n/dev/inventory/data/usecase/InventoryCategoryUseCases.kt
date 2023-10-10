package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.CategoryAttribute
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import java.util.UUID


class CreateInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        name: String,
        attribute: List<CategoryAttribute>
    ) {
        val category = InventoryCategory(
            name = name,
            attributes = attribute,
            categoryId = UUID.randomUUID().toString(),
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
        attributes: List<CategoryAttribute> = current.attributes,
    ) {
        val item = current.copy(
            name = name,
            attributes = attributes,
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