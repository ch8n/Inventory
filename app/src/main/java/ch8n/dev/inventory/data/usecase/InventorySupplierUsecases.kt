package ch8n.dev.inventory.data.usecase

import android.util.Log
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.database.roomdb.LocalSuppliersDAO
import ch8n.dev.inventory.data.domain.InventorySupplier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class GetInventorySupplier(
    localSuppliersDAO: LocalSuppliersDAO = DataModule.Injector.localDatabase.localSuppliersDAO()
) {
    val value = localSuppliersDAO.getAll().distinctUntilChanged().map { entities ->
        Log.d("ch8n", "GetInventorySupplier called $entities")
        entities.map {
            InventorySupplier(
                id = it.uid,
                name = it.supplierName
            )
        }
    }.flowOn(Dispatchers.IO)
}


class DeleteInventorySupplier(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(supplier: InventorySupplier) {
        database.deleteSupplier(supplier)
    }
}
