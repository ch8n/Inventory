package ch8n.dev.inventory.data.database.firestore


class RemoteDatabase {

    private val _remoteSupplierDAO by lazy { RemoteSupplierDAO() }
    private val _remoteCategoryDAO by lazy { RemoteCategoryDAO() }
    val remoteSuppliersDAO get() = _remoteSupplierDAO
    val remoteCategoryDAO get() = _remoteCategoryDAO


}

