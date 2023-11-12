package ch8n.dev.inventory.data.database.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
        InventorySupplierEntity::class,
        InventoryCategoryEntity::class,
        InventoryItemEntity::class,
        OrderEntity::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun localSuppliersDAO(): LocalSuppliersDAO
    abstract fun localCategoryDAO(): LocalCategoryDAO
    abstract fun localItemDAO(): LocalItemDAO
    abstract fun localOrderDAO(): LocalOrderDAO

    companion object {
        fun build(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "fashion-inventory-local"
            ).build()
        }
    }
}



