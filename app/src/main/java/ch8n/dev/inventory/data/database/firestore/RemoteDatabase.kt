package ch8n.dev.inventory.data.database.firestore

import ch8n.dev.inventory.data.database.firestorage.RemoteUploadDAO


class RemoteDatabase {

    private val _remoteSupplierDAO by lazy { RemoteSupplierDAO() }
    private val _remoteCategoryDAO by lazy { RemoteCategoryDAO() }
    private val _remoteItemDAO by lazy { RemoteItemDAO() }
    private val _remoteUploadDAO by lazy { RemoteUploadDAO() }
    private val _remoteOrderDAO by lazy { RemoteOrderDAO() }
    val remoteSuppliersDAO get() = _remoteSupplierDAO
    val remoteCategoryDAO get() = _remoteCategoryDAO
    val remoteItemDAO get() = _remoteItemDAO
    val remoteUploadDAO get() = _remoteUploadDAO
    val remoteOrderDAO get() = _remoteOrderDAO

}

