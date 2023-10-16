package ch8n.dev.inventory.data.database.firestore


class RemoteDatabase {

    private val _remoteSupplierDAO by lazy { RemoteSupplierDAO() }
    val remoteSuppliersDAO get() = _remoteSupplierDAO


}

