package ch8n.dev.inventory.data.database.firestore

import android.util.Log
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.roomdb.LocalItemDAO
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.data.usecase.ItemOrder
import ch8n.dev.inventory.data.usecase.toRemote
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await

class RemoteSupplierDAO {
    object Schema {
        const val INVENTORY_SUPPLIERS = "inventory_suppliers"
    }

    private val remoteDB = Firebase.firestore
    private val suppliersDocumentReference = remoteDB.collection(Schema.INVENTORY_SUPPLIERS)

    fun observeSuppliersSnapShot(
        onChange: (inventorySuppliersFS: List<InventorySupplierFS>) -> Unit
    ) {
        suppliersDocumentReference.addSnapshotListener { querySnapShot, error ->
            if (error != null || querySnapShot == null) {
                Log.e("ch8n", "firebase suppliersDocumentReference error $error")
                Log.d("ch8n", "firebase suppliersDocumentReference querySnapShot $querySnapShot")
                return@addSnapshotListener
            }

            val inventorySuppliersFS = querySnapShot.documents.mapNotNull { snapshot ->
                val supplierName = snapshot.getString("name")
                if (!supplierName.isNullOrEmpty()) {
                    InventorySupplierFS(
                        documentReferenceId = snapshot.id,
                        supplierName = supplierName
                    )
                } else {
                    null
                }
            }
            onChange.invoke(inventorySuppliersFS)
        }
    }

    suspend fun getAllSuppliers(): List<InventorySupplierFS> {
        val querySnapShot = suppliersDocumentReference.get().await()
        val inventorySuppliersFS = querySnapShot.documents.mapNotNull { snapshot ->
            val supplierName = snapshot.getString("name")
            if (!supplierName.isNullOrEmpty()) {
                InventorySupplierFS(
                    documentReferenceId = snapshot.id,
                    supplierName = supplierName
                )
            } else {
                null
            }
        }
        return inventorySuppliersFS
    }

    suspend fun createSupplier(supplierName: String): InventorySupplierFS {
        val documentReference =
            suppliersDocumentReference.add(hashMapOf("name" to supplierName)).await()
        return InventorySupplierFS(documentReference.id, supplierName)
    }

    suspend fun deleteSupplier(supplierId: String) {
        suppliersDocumentReference.document(supplierId).delete().await()
    }

}


class RemoteCategoryDAO {
    object Schema {
        const val INVENTORY_CATEGORY = "inventory_category"
    }

    private val remoteDB = Firebase.firestore
    private val categoryDocumentReference = remoteDB.collection(Schema.INVENTORY_CATEGORY)

    suspend fun getAllCategory(): List<InventoryCategoryFS> {
        val querySnapShot = categoryDocumentReference.get().await()
        val categoriesSuppliersFS = querySnapShot.documents.mapNotNull { snapshot ->
            val categoryName = snapshot.getString("name")
            val categorySizes = snapshot.get("sizes") as? List<String> ?: emptyList()

            if (!categoryName.isNullOrEmpty()) {
                InventoryCategoryFS(
                    documentReferenceId = snapshot.id,
                    categoryName = categoryName,
                    itemSize = categorySizes
                )
            } else {
                null
            }
        }
        return categoriesSuppliersFS
    }

    suspend fun createCategory(
        categoryName: String,
        sizes: List<String>
    ): InventoryCategoryFS {
        val documentReference =
            categoryDocumentReference.add(
                hashMapOf(
                    "name" to categoryName,
                    "sizes" to sizes
                )
            ).await()
        return InventoryCategoryFS(
            documentReferenceId = documentReference.id,
            categoryName = categoryName,
            itemSize = sizes
        )
    }

    suspend fun deleteCategory(categoryId: String) {
        categoryDocumentReference.document(categoryId).delete().await()
    }

}


class RemoteItemDAO {
    object Schema {
        const val INVENTORY_ITEM = "inventory_item"
    }

    private val remoteDB = Firebase.firestore
    private val itemDocumentReference = remoteDB.collection(Schema.INVENTORY_ITEM)

    fun getItemDocumentCollection() = itemDocumentReference

    private fun DocumentSnapshot.getStringOrEmpty(key: String): String {
        return this.getString(key) ?: ""
    }

    private fun DocumentSnapshot.getIntOrZero(key: String): Int {
        return this.getDouble(key)?.toInt() ?: 0
    }

    private fun DocumentSnapshot.getDoubleOrZero(key: String): Double {
        return this.getDouble(key) ?: 0.0
    }

    suspend fun getAllItems(): List<InventoryItemFS> {
        val querySnapShot = itemDocumentReference.get().await()
        val inventoryItemsFS = querySnapShot.documents.map { snapshot ->
            InventoryItemFS(
                documentReferenceId = snapshot.id,
                itemName = snapshot.getStringOrEmpty("itemName"),
                itemCategoryDocumentReferenceId = snapshot.getStringOrEmpty("itemCategoryDocumentReferenceId"),
                itemImage = snapshot.getStringOrEmpty("itemImage"),
                itemWeight = snapshot.getDoubleOrZero("itemWeight"),
                itemSupplierDocumentReferenceId = snapshot.getStringOrEmpty("itemSupplierDocumentReferenceId"),
                itemQuantity = snapshot.getIntOrZero("itemQuantity"),
                itemSellingPrice = snapshot.getIntOrZero("itemSellingPrice"),
                itemPurchasePrice = snapshot.getIntOrZero("itemPurchasePrice"),
                itemSize = snapshot.getStringOrEmpty("itemSize"),
                itemColor = snapshot.getStringOrEmpty("itemColor"),
            )
        }
        return inventoryItemsFS
    }

    suspend fun upsertInventoryItem(
        inventoryItem: InventoryItem
    ): InventoryItemFS {

        val attributes = hashMapOf(
            "itemName" to inventoryItem.itemName,
            "itemCategoryDocumentReferenceId" to inventoryItem.itemCategoryId,
            "itemImage" to inventoryItem.itemImage,
            "itemQuantity" to inventoryItem.itemQuantity,
            "itemWeight" to inventoryItem.itemWeight,
            "itemSupplierDocumentReferenceId" to inventoryItem.itemSupplierId,
            "itemSellingPrice" to inventoryItem.itemSellingPrice,
            "itemPurchasePrice" to inventoryItem.itemPurchasePrice,
            "itemSize" to inventoryItem.itemSize,
            "itemColor" to inventoryItem.itemColor,
        )

        val _documentReferenceId = itemDocumentReference
            .run {
                if (inventoryItem.uid.isNotEmpty()) {
                    document(inventoryItem.uid).set(attributes).await()
                    inventoryItem.uid
                } else {
                    val documentReference = add(attributes).await()
                    documentReference.id
                }
            }

        return inventoryItem.toRemote().copy(documentReferenceId = _documentReferenceId)
    }

    suspend fun deleteInventoryItem(itemId: String) {
        itemDocumentReference.document(itemId).delete().await()
    }

}


class RemoteOrderDAO(
    private val remoteItemDAO: RemoteItemDAO = DataModule.Injector.remoteDatabase.remoteItemDAO,
    private val localItemDAO: LocalItemDAO = DataModule.Injector.localDatabase.localItemDAO()
) {
    object Schema {
        const val ORDERS = "orders"
    }

    private val remoteDB = Firebase.firestore
    private val orderDocumentReference = remoteDB.collection(Schema.ORDERS)

    fun Order.toMap(): HashMap<String, Any> {
        val order = this
        return hashMapOf(
            "clientName" to order.clientName,
            "contact" to order.contact,
            "comment" to order.comment,
            "totalPrice" to order.totalPrice,
            "totalWeight" to order.totalWeight,
            "itemsIds" to order.itemsIds,
            "orderStatus" to order.orderStatus,
            "createdAt" to order.createdAt
        )
    }

    private fun DocumentSnapshot.getStringOrEmpty(key: String): String {
        return this.getString(key) ?: ""
    }

    private fun DocumentSnapshot.getIntOrZero(key: String): Int {
        return this.getDouble(key)?.toInt() ?: 0
    }

    private fun DocumentSnapshot.getLongOrZero(key: String): Long {
        return this.getLong(key) ?: 0L
    }

    private fun DocumentSnapshot.getDoubleOrZero(key: String): Double {
        return this.getDouble(key) ?: 0.0
    }

    private inline fun <reified T> DocumentSnapshot.getList(key: String): List<T> {
        return kotlin.runCatching {
            val gson = Gson()
            val json = this.get(key)?.toString() ?: ""
            val list = gson.fromJson(json, List::class.java)
            list.map { gson.toJson(it) }
                .map { gson.fromJson(it, T::class.java) }
        }.getOrNull() ?: emptyList()
    }

    suspend fun getAllOrders(): List<OrderFS> {
        val querySnapShot = orderDocumentReference.get().await()
        val orderFS = querySnapShot.documents.map { snapshot ->
            OrderFS(
                documentReferenceId = snapshot.id,
                clientName = snapshot.getStringOrEmpty("clientName"),
                contact = snapshot.getStringOrEmpty("contact"),
                comment = snapshot.getStringOrEmpty("comment"),
                totalPrice = snapshot.getIntOrZero("totalPrice"),
                totalWeight = snapshot.getDoubleOrZero("totalWeight"),
                itemsIds = snapshot.getList<ItemOrder>("itemsIds"),
                orderStatus = OrderStatus.getOrIssue(snapshot.getStringOrEmpty("orderStatus")),
                createdAt = snapshot.getLongOrZero("createdAt")
            )
        }
        return orderFS
    }


    suspend fun createOrder(order: Order): OrderFS {
        val batch = remoteDB.batch()
        val itemCollection = remoteItemDAO.getItemDocumentCollection()
        val updatedInventoryItems = order.itemsIds.mapNotNull { (id, qty) ->
            val localItem = localItemDAO.findById(id) ?: return@mapNotNull null
            val itemRef = itemCollection.document(id)
            val updatedQty = localItem.itemQuantity - qty
            batch.update(itemRef, "itemQuantity", updatedQty)
            localItem.copy(itemQuantity = updatedQty)
        }
        batch.commit()
        localItemDAO.insertAll(*updatedInventoryItems.toTypedArray())
        return upsertOrderItem(order)
    }

    suspend fun updateOrder(
        original: Order,
        updated: Order,
    ): OrderFS {
        val originalCart = original.itemsIds.associate { it.itemId to it.orderQty }
        val updatedCart = updated.itemsIds.associate { it.itemId to it.orderQty }
        val batch = remoteDB.batch()
        val itemCollection = remoteItemDAO.getItemDocumentCollection()
        originalCart.forEach { (id, qty) ->
            val updatedQty = updatedCart.get(id) ?: 0
            val localItem = localItemDAO.findById(id) ?: return@forEach
            val itemRef = itemCollection.document(id)
            when {
                updatedQty > qty -> {
                    val change = updatedQty - qty
                    val newQty = localItem.itemQuantity - change
                    batch.update(itemRef, "itemQuantity", newQty)
                    localItemDAO.insertAll(localItem.copy(itemQuantity = newQty))
                }

                updatedQty < qty -> {
                    val change = qty - updatedQty
                    val newQty = localItem.itemQuantity + change
                    batch.update(itemRef, "itemQuantity", newQty)
                    localItemDAO.insertAll(localItem.copy(itemQuantity = newQty))
                }

                else -> {
                    /**Do nothing**/
                }
            }
        }
        batch.commit()
        return upsertOrderItem(updated)
    }

    suspend fun upsertOrderItem(
        order: Order
    ): OrderFS {

        val attributes = order.toMap()

        val _documentReferenceId = orderDocumentReference
            .run {
                if (order.uid.isNotEmpty()) {
                    document(order.uid).set(attributes).await()
                    order.uid
                } else {
                    val documentReference = add(attributes).await()
                    documentReference.id
                }
            }

        return order.toRemote().copy(documentReferenceId = _documentReferenceId)
    }

    suspend fun deleteOrder(itemId: String) {
        orderDocumentReference.document(itemId).delete().await()
    }

}