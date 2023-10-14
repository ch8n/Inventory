package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventorySupplier
import java.util.UUID


class GetInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventoryItemsFlow
}


class CreateInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        name: String,
        images: List<String>,
        category: InventoryCategory,
        itemQuantity: Int,
        weight: Double,
        supplier: InventorySupplier,
        sellPrice: Int,
        purchasePrice: Int,
        itemSize: String,
        itemColor : String
    ) {
        val item = InventoryItem(
            id = UUID.randomUUID().toString(),
            name = name,
            images = images,
            category = category,
            itemQuantity = itemQuantity,
            weight = weight,
            supplier = supplier,
            sellingPrice = sellPrice,
            purchasePrice = purchasePrice,
            itemSize = itemSize,
            itemColor = itemColor
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
        images: List<String> = current.images,
        category: InventoryCategory = current.category,
        itemSize: String = current.itemSize,
        totalQuantity: Int = current.itemQuantity,
        weight: Double = current.weight,
        supplier: InventorySupplier = current.supplier,
        sellPrice: Int = current.sellingPrice,
        purchasePrice: Int = current.purchasePrice,
        itemColor : String = current.itemColor
    ) {
        val item = current.copy(
            name = name,
            images = images,
            category = category,
            itemSize = itemSize,
            itemQuantity = totalQuantity,
            weight = weight,
            supplier = supplier,
            sellingPrice = sellPrice,
            purchasePrice = purchasePrice,
            itemColor = itemColor
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