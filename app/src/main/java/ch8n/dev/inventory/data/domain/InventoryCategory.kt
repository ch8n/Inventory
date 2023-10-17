package ch8n.dev.inventory.data.domain

import androidx.compose.runtime.Stable
import ch8n.dev.inventory.data.usecase.ItemOrder
import java.util.UUID

data class InventoryItem(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val images: List<String>,
    val category: InventoryCategory,
    val itemQuantity: Int,
    val weight: Double,
    val supplier: InventorySupplier,
    val sellingPrice: Int,
    val purchasePrice: Int,
    val itemSize: String,
    val itemColor: String,
) {
    companion object {

        val New
            get() = InventoryItem(
                name = "",
                images = listOf(),
                category = InventoryCategory.Empty,
                supplier = InventorySupplier.Empty,
                sellingPrice = 0,
                purchasePrice = 0,
                itemQuantity = 0,
                weight = 0.0,
                itemSize = "",
                itemColor = "",
            )

        val Empty = InventoryItem(
            id = "",
            name = "",
            images = listOf(),
            category = InventoryCategory.Empty,
            supplier = InventorySupplier.Empty,
            sellingPrice = 0,
            purchasePrice = 0,
            itemQuantity = 0,
            weight = 0.0,
            itemSize = "",
            itemColor = "",
        )
    }
}


@Stable
data class InventoryCategory(
    val id:String,
    val name: String,
    val sizes: List<String>
) {
    companion object {
        val Empty = InventoryCategory(
            id = "",
            name = "",
            sizes = emptyList()
        )
    }
}


@Stable
data class InventorySupplier(
    val id: String,
    val name: String
) {
    companion object {
        val Empty = InventorySupplier(
            id = "",
            name = ""
        )
    }
}


enum class OrderStatus {
    NEW_ORDER,
    PACKING,
    PACKED,
    DISPATCHED,
    DELIVERED,
    ISSUE
}

data class Order(
    val id: String = UUID.randomUUID().toString(),
    val clientName: String,
    val contact: String,
    val comment: String,
    val totalPrice: Int,
    val totalWeight: Double,
    val itemsIds: List<ItemOrder>,
    val orderStatus: OrderStatus,
    val createdAt: Long = System.currentTimeMillis(),
)