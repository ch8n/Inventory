package ch8n.dev.inventory.data.database.firestore


class RemoteDatabase {

    private val _remoteSupplierDAO by lazy { RemoteSupplierDAO() }
    private val _remoteCategoryDAO by lazy { RemoteCategoryDAO() }
    private val _remoteItemDAO by lazy { RemoteItemDAO() }
    val remoteSuppliersDAO get() = _remoteSupplierDAO
    val remoteCategoryDAO get() = _remoteCategoryDAO
    val remoteItemDAO get() = _remoteItemDAO

}

