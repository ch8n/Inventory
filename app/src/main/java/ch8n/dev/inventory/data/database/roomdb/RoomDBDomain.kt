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