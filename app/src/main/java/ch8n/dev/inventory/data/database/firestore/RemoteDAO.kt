package ch8n.dev.inventory.data.database.firestore

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
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
            val categorySizes = snapshot.getField<List<String>>("sizes") ?: emptyList()

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