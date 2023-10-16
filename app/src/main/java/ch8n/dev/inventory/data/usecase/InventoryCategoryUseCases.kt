package ch8n.dev.inventory.data.usecase

import android.util.Log
import ch8n.dev.inventory.UseCaseScope
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.database.firestore.InventorySupplierFS
import ch8n.dev.inventory.data.database.firestore.RemoteSupplierDAO
import ch8n.dev.inventory.data.database.roomdb.InventorySupplierEntity
import ch8n.dev.inventory.data.database.roomdb.LocalSuppliersDAO
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventorySupplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GetInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    val value = database.inventoryCategoriesFlow
}


class ObserveRemoteInventorySuppliersChange(
    remoteSupplierDAO: RemoteSupplierDAO = DataModule.Injector.remoteDatabase.remoteSuppliersDAO,
    localSuppliersDAO: LocalSuppliersDAO = DataModule.Injector.localDatabase.localSuppliersDAO(),
) : UseCaseScope {

    init {
        remoteSupplierDAO.observeSuppliersSnapShot { updatedSuppliersFS ->
            Log.d("ch8n", "ObserveRemoteInventorySuppliersChange $updatedSuppliersFS")
            launch(Dispatchers.IO) {
                localSuppliersDAO.insertAll(*updatedSuppliersFS.toEntity().toTypedArray())
            }
        }
    }

    private fun List<InventorySupplierFS>.toEntity(): List<InventorySupplierEntity> {
        return map {
            InventorySupplierEntity(
                uid = it.documentReferenceId,
                supplierName = it.supplierName
            )
        }
    }

}


class CreateInventorySuppliers(
    private val remoteSupplierDAO: RemoteSupplierDAO = DataModule.Injector.remoteDatabase.remoteSuppliersDAO
) : UseCaseScope {

    fun execute(supplier: String) {
        launch {
            Log.d("ch8n", "CreateInventorySuppliers $supplier")
            remoteSupplierDAO.createSupplier(supplier)
        }
    }
}

class CreateInventoryCategory(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        category: InventoryCategory
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