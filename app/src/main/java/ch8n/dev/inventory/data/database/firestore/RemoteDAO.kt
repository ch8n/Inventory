package ch8n.dev.inventory.data.database.firestore

import android.util.Log
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.usecase.toRemote
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    private fun DocumentSnapshot.getStringOrEmpty(key: String): String {
        return this.getString(key) ?: ""
    }

    private fun DocumentSnapshot.getIntOrZero(key: String): Int {
        return this.get(key) as? Int ?: 0
    }

    private fun DocumentSnapshot.getDoubleOrZero(key: String): Double {
        return this.getDouble(key) ?: 0.0
    }

    suspend fun getAllItems(): List<InventoryItemFS> {
        val querySnapShot = itemDocumentReference.get().await()
        val inventoryItemsFS = querySnapShot.documents.mapNotNull { snapshot ->
            InventoryItemFS(
                documentReferenceId = snapshot.id,
                itemName = snapshot.getStringOrEmpty("itemName"),
                itemCategoryDocumentReferenceId = snapshot.getStringOrEmpty("itemCategoryDocumentReferenceId"),
                itemImage = snapshot.getStringOrEmpty("itemImage"),
                itemQuantity = snapshot.getIntOrZero("itemQuantity"),
                itemWeight = snapshot.getDoubleOrZero("itemWeight"),
                itemSupplierDocumentReferenceId = snapshot.getStringOrEmpty("itemSupplierDocumentReferenceId"),
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