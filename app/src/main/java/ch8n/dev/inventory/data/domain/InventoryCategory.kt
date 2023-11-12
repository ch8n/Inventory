package ch8n.dev.inventory.data.domain

import androidx.compose.runtime.Stable
import ch8n.dev.inventory.data.usecase.ItemOrder
import java.util.UUID


@Stable
data class InventoryItem(
    val uid: String,
    val itemName: String,
    val itemImage: String,
    val itemCategoryId: String,
    val itemWeight: Double,
    val itemSupplierId: String,
    val itemSize: String,
    val itemColor: String,
    val itemQuantity: Int,
    val itemSellingPrice: Int,
    val itemPurchasePrice: Int,
) {
    companion object {

        val New
            get() = InventoryItem(
                uid = "",
                itemName = "",
                itemImage = "",
                itemCategoryId = "",
                itemSupplierId = "",
                itemSellingPrice = 0,
                itemPurchasePrice = 0,
                itemQuantity = 0,
                itemWeight = 0.0,
                itemSize = "",
                itemColor = "",
            )
    }
}


@Stable
data class InventoryCategory(
    val id: String,
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
    ISSUE;

    companion object {
        fun getOrIssue(string: String): OrderStatus {
            return kotlin.runCatching { valueOf(string) }.getOrNull() ?: ISSUE
        }
    }
}

data class Order(
    val uid: String = UUID.randomUUID().toString(),
    val clientName: String,
    val contact: String,
    val comment: String,
    val totalPrice: Int,
    val totalWeight: Double,
    val itemsIds: List<ItemOrder>,
    val orderStatus: OrderStatus,
    val createdAt: Long
)