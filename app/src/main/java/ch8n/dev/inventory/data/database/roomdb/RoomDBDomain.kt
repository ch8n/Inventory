package ch8n.dev.inventory.data.database.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters


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