package ch8n.dev.inventory.data.database.firestore

data class InventorySupplierFS(
    val documentReferenceId: String,
    val supplierName: String,
)

data class InventoryCategoryFS(
    val documentReferenceId: String,
    val categoryName: String,
    val itemSize: List<String>
)