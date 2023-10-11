package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventoryItemVariant
import ch8n.dev.inventory.data.domain.InventorySupplier
import kotlinx.coroutines.flow.map
import java.util.UUID


class GetInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventoryItemsFlow.map {
        ComposeStable(it)
    }
}

class CreateInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        name: String,
        images: List<String>,
        category: InventoryCategory,
        itemVariant: List<InventoryItemVariant>,
        totalQuantity: Int,
        weight: Double,
        supplier: InventorySupplier,
        sellPrice: Int,
        purchasePrice: Int
    ) {
        val item = InventoryItem(
            id = UUID.randomUUID().toString(),
            name = name,
            images = images,
            category = category,
            itemVariant = itemVariant,
            totalQuantity = totalQuantity,
            weight = weight,
            supplier = supplier,
            sellingPrice = sellPrice,
            purchasePrice = purchasePrice,
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
        itemVariant: List<InventoryItemVariant> = current.itemVariant,
        totalQuantity: Int = current.totalQuantity,
        weight: Double = current.weight,
        supplier: InventorySupplier = current.supplier,
        sellPrice: Int = current.sellingPrice,
        purchasePrice: Int = current.purchasePrice
    ) {
        val item = current.copy(
            name = name,
            images = images,
            category = category,
            itemVariant = itemVariant,
            totalQuantity = totalQuantity,
            weight = weight,
            supplier = supplier,
            sellingPrice = sellPrice,
            purchasePrice = purchasePrice,
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