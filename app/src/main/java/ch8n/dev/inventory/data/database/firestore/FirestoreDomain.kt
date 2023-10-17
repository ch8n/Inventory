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

data class InventoryItemFS(
    val documentReferenceId: String,
    val itemName: String,
    val itemCategoryDocumentReferenceId: String,
    val itemImage: String,
    val itemQuantity: Int,
    val itemWeight: Double,
    val itemSupplierDocumentReferenceId: String,
    val itemSellingPrice: Int,
    val itemPurchasePrice: Int,
    val itemSize: String,
    val itemColor: String,
)