package ch8n.dev.inventory.data.database.firestore

import android.util.Log
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

    suspend fun createSupplier(supplierName: String): InventorySupplierFS {
        val documentReference =
            suppliersDocumentReference.add(hashMapOf("name" to supplierName)).await()
        return InventorySupplierFS(documentReference.id, supplierName)
    }

}