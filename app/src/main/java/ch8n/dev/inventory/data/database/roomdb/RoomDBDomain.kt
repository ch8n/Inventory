package ch8n.dev.inventory.data.database.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.data.usecase.ItemOrder
import com.google.gson.Gson
import java.util.UUID


@Entity
data class InventorySupplierEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "supplier_name") val supplierName: String,
)

class StringListConverter {
    @TypeConverter
    fun fromString(value: String): List<String> {
        return value.split(",").map { it }
    }

    @TypeConverter
    fun toString(value: List<String>): String {
        return value.joinToString(",")
    }
}

abstract class GsonListConverter<T> {

    private val gson = Gson()

    @TypeConverter
    fun fromString(json: String): List<T> {
        return gson.fromJson(json, List::class.java).map { it as T }
    }

    @TypeConverter
    fun toString(value: List<ItemOrder>): String {
        return gson.toJson(value)
    }
}

class ItemOrderListConverter : GsonListConverter<ItemOrder>()


@Entity
@TypeConverters(StringListConverter::class)
data class InventoryCategoryEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    @ColumnInfo(name = "item_sizes") val itemSize: List<String>
)

@Entity
data class InventoryItemEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "item_name") val itemName: String,
    @ColumnInfo(name = "item_category_id") val itemCategoryId: String,
    @ColumnInfo(name = "item_image") val itemImage: String,
    @ColumnInfo(name = "item_weight") val itemWeight: Double,
    @ColumnInfo(name = "item_supplier_id") val itemSupplierId: String,
    @ColumnInfo(name = "item_size") val itemSize: String,
    @ColumnInfo(name = "item_color") val itemColor: String,

    @ColumnInfo(name = "item_quantity") val itemQuantity: Int,
    @ColumnInfo(name = "item_selling_price") val itemSellingPrice: Int,
    @ColumnInfo(name = "item_purchase_price") val itemPurchasePrice: Int,
)

@Entity
@TypeConverters(ItemOrderListConverter::class)
data class OrderEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "client_name") val clientName: String,
    @ColumnInfo(name = "contact") val contact: String,
    @ColumnInfo(name = "comment") val comment: String,
    @ColumnInfo(name = "total_price") val totalPrice: Int,
    @ColumnInfo(name = "total_weight") val totalWeight: Double,
    @ColumnInfo(name = "items_ids") val itemsIds: List<ItemOrder>,
    @ColumnInfo(name = "order_status") val orderStatus: OrderStatus,
    @ColumnInfo(name = "created_at") val createdAt: Long,
)