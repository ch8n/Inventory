package ch8n.dev.inventory.data.database.firestore

import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.data.usecase.ItemOrder

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
    val itemCategoryDocumentReferenceId: String,
    val itemImage: String,
    val itemColor: String,
    val itemName: String,
    val itemPurchasePrice: Int,
    val itemQuantity: Int,
    val itemSellingPrice: Int,
    val itemSize: String,
    val itemSupplierDocumentReferenceId: String,
    val itemWeight: Double,
)

data class OrderFS(
    val documentReferenceId: String,
    val clientName: String,
    val contact: String,
    val comment: String,
    val totalPrice: Int,
    val totalWeight: Double,
    val itemsIds: List<ItemOrder>,
    val orderStatus: OrderStatus,
    val createdAt : Long,
)