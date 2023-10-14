package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.database.InMemoryDB
import kotlinx.coroutines.flow.map

class GetInventorySupplier(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventorySupplierFlow.map {
        ComposeStable(it)
    }
}
