package ch8n.dev.inventory.data.database.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class InventorySupplierEntity(
    @PrimaryKey val uid: String,
    @ColumnInfo(name = "supplier_name") val supplierName: String,
)