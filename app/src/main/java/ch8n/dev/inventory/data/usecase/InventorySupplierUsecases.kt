package ch8n.dev.inventory.data.usecase

import android.util.Log
import ch8n.dev.inventory.UseCaseScope
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.database.firestore.InventorySupplierFS
import ch8n.dev.inventory.data.database.firestore.RemoteSupplierDAO
import ch8n.dev.inventory.data.database.roomdb.InventorySupplierEntity
import ch8n.dev.inventory.data.database.roomdb.LocalSuppliersDAO
import ch8n.dev.inventory.data.domain.InventorySupplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


fun InventorySupplierFS.toEntity(): InventorySupplierEntity {
    return InventorySupplierEntity(
        uid = documentReferenceId,
        supplierName = supplierName
    )
}

fun InventorySupplier.toEntity(): InventorySupplierEntity {
    return InventorySupplierEntity(
        uid = id,
        supplierName = name
    )
}

fun InventorySupplierEntity.toView(): InventorySupplier {
    return InventorySupplier(
        id = uid,
        name = supplierName
    )
}

class ObserveRemoteInventorySuppliersChange(
    remoteSupplierDAO: RemoteSupplierDAO = DataModule.Injector.remoteDatabase.remoteSuppliersDAO,
    localSuppliersDAO: LocalSuppliersDAO = DataModule.Injector.localDatabase.localSuppliersDAO(),
) : UseCaseScope {

    init {
        remoteSupplierDAO.observeSuppliersSnapShot { updatedSuppliersFS ->
            Log.d("ch8n", "ObserveRemoteInventorySuppliersChange $updatedSuppliersFS")
            launch(Dispatchers.IO) {
                localSuppliersDAO.insertAll(
                    *updatedSuppliersFS
                        .map { it.toEntity() }
                        .toTypedArray()
                )
            }
        }
    }


}


class CreateInventorySuppliers(
    private val remoteSupplierDAO: RemoteSupplierDAO = DataModule.Injector.remoteDatabase.remoteSuppliersDAO,
    private val localSuppliersDAO: LocalSuppliersDAO = DataModule.Injector.localDatabase.localSuppliersDAO(),
) : UseCaseScope {

    fun execute(supplier: String) {
        if (supplier.isEmpty() || supplier.isBlank()) return
        launch {
            Log.d("ch8n", "CreateInventorySuppliers $supplier")
            val remoteSupplier = remoteSupplierDAO.createSupplier(supplier)
            localSuppliersDAO.insertAll(remoteSupplier.toEntity())
        }
    }
}


class GetInventorySupplier(
    private val remoteSupplierDAO: RemoteSupplierDAO = DataModule.Injector.remoteDatabase.remoteSuppliersDAO,
    private val localSuppliersDAO: LocalSuppliersDAO = DataModule.Injector.localDatabase.localSuppliersDAO(),
) : UseCaseScope {


    val local = localSuppliersDAO.getAll().distinctUntilChanged().map { entities ->
        Log.d("ch8n", "GetInventorySupplier local $entities")
        entities.map { it.toView() }
    }.flowOn(Dispatchers.IO)

    fun invalidate() {
        launch {
            val remoteSuppliers = remoteSupplierDAO.getAllSuppliers()
            Log.d("ch8n", "GetInventorySupplier invalidate called $remoteSuppliers")
            localSuppliersDAO.insertAll(*remoteSuppliers.map { it.toEntity() }.toTypedArray())
        }
    }
}


class DeleteInventorySupplier(
    private val remoteSupplierDAO: RemoteSupplierDAO = DataModule.Injector.remoteDatabase.remoteSuppliersDAO,
    private val localSuppliersDAO: LocalSuppliersDAO = DataModule.Injector.localDatabase.localSuppliersDAO(),
) : UseCaseScope {
    fun execute(supplier: InventorySupplier) {
        launch {
            remoteSupplierDAO.deleteSupplier(supplier.id)
            localSuppliersDAO.delete(supplier.toEntity())
        }
    }
}
