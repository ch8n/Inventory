package ch8n.dev.inventory.data.database.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RoomDAO<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: T)

    @Delete
    fun delete(supplier: T)
}

@Dao
interface LocalSuppliersDAO : RoomDAO<InventorySupplierEntity> {
    @Query("SELECT * FROM InventorySupplierEntity")
    fun getAll(): Flow<List<InventorySupplierEntity>>

    @Query("SELECT * FROM InventorySupplierEntity WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<InventorySupplierEntity>

    @Query("SELECT * FROM InventorySupplierEntity WHERE supplier_name LIKE :supplierName LIMIT 1")
    fun findByName(supplierName: String): InventorySupplierEntity?
}

@Dao
interface LocalCategoryDAO : RoomDAO<InventoryCategoryEntity> {
    @Query("SELECT * FROM InventoryCategoryEntity")
    fun getAll(): Flow<List<InventoryCategoryEntity>>

    @Query("SELECT * FROM InventoryCategoryEntity WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<InventoryCategoryEntity>

    @Query("SELECT * FROM InventoryCategoryEntity WHERE category_name LIKE :name LIMIT 1")
    fun findByName(name: String): InventoryCategoryEntity?
}


@Dao
interface LocalItemDAO : RoomDAO<InventoryItemEntity> {
    @Query("SELECT * FROM InventoryItemEntity")
    fun getAll(): Flow<List<InventoryItemEntity>>


    @Query("SELECT * FROM InventoryItemEntity WHERE uid IN (:ids)")
    fun loadAllByIds(ids: IntArray): List<InventoryItemEntity>

    @Query("SELECT * FROM InventoryItemEntity WHERE item_name LIKE :name LIMIT 1")
    fun findByName(name: String): InventoryItemEntity?

    @Query("SELECT * FROM InventoryItemEntity WHERE uid LIKE :id LIMIT 1")
    fun findById(id: String): InventoryItemEntity?
}