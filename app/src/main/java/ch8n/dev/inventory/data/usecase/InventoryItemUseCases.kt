package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.CategoryAttribute
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import java.util.UUID


class CreateInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        name: String,
        attribute: List<CategoryAttribute>,
        categoryId: String,
    ) {
        val item = InventoryItem(
            itemId = UUID.randomUUID().toString(),
            name = name,
            attributeValues = attribute,
            categoryId = categoryId
        )
        database.addInventoryItem(item)
    }
}

class UpdateInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        current: InventoryItem,
        name: String = current.name,
        attribute: List<CategoryAttribute> = current.attributeValues,
        categoryId: String = current.categoryId
    ) {
        val item = current.copy(
            name = name,
            attributeValues = attribute,
            categoryId = categoryId
        )
        database.editInventoryItem(item)
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