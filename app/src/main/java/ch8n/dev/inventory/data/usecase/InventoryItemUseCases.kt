package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.UseCaseScope
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.InMemoryDB
import ch8n.dev.inventory.data.database.firestore.InventoryItemFS
import ch8n.dev.inventory.data.database.firestore.RemoteItemDAO
import ch8n.dev.inventory.data.database.roomdb.InventoryItemEntity
import ch8n.dev.inventory.data.database.roomdb.LocalItemDAO
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


fun InventoryItem.toRemote(): InventoryItemFS {
    return InventoryItemFS(
        documentReferenceId = uid,
        itemName = itemName,
        itemCategoryDocumentReferenceId = itemCategoryId,
        itemImage = itemImage,
        itemQuantity = itemQuantity,
        itemWeight = itemWeight,
        itemSupplierDocumentReferenceId = itemSupplierId,
        itemSellingPrice = itemSellingPrice,
        itemPurchasePrice = itemPurchasePrice,
        itemSize = itemSize,
        itemColor = itemColor
    )
}

fun InventoryItemFS.toEntity(): InventoryItemEntity {
    return InventoryItemEntity(
        uid = documentReferenceId,
        itemName = itemName,
        itemCategoryId = itemCategoryDocumentReferenceId,
        itemImage = itemImage,
        itemQuantity = itemQuantity,
        itemWeight = itemWeight,
        itemSupplierId = itemSupplierDocumentReferenceId,
        itemSellingPrice = itemSellingPrice,
        itemPurchasePrice = itemPurchasePrice,
        itemSize = itemSize,
        itemColor = itemColor
    )
}

fun InventoryItemEntity.toView(): InventoryItem {
    return InventoryItem(
        uid = uid,
        itemName = itemName,
        itemCategoryId = itemCategoryId,
        itemImage = itemImage,
        itemQuantity = itemQuantity,
        itemWeight = itemWeight,
        itemSupplierId = itemSupplierId,
        itemSellingPrice = itemSellingPrice,
        itemPurchasePrice = itemPurchasePrice,
        itemSize = itemSize,
        itemColor = itemColor
    )
}


class GetInventoryItem(
    private val remoteItemDAO: RemoteItemDAO = DataModule.Injector.remoteDatabase.remoteItemDAO,
    private val localItemDAO: LocalItemDAO = DataModule.Injector.localDatabase.localItemDAO(),
) : UseCaseScope {
    val local = localItemDAO.getAll()
        .distinctUntilChanged()
        .map { entities ->
            entities.map { it.toView() }
        }

    fun invalidate() {
        launch {
            val remoteItems = remoteItemDAO.getAllItems()
            localItemDAO.insertAll(*remoteItems.map { it.toEntity() }.toTypedArray())
        }
    }

    fun filter(
        searchQuery: String,
        selectedCategory: InventoryCategory
    ): Flow<List<InventoryItem>> {
        return local.map { items ->
            items.filter {
                if (selectedCategory == InventoryCategory.Empty) return@filter true
                return@filter it.itemCategoryId.equals(selectedCategory.id)
            }.filter {
                if (searchQuery.isNotEmpty()) {
                    return@filter it.itemName.contains(searchQuery, ignoreCase = true)
                } else {
                    true
                }
            }
        }.flowOn(Dispatchers.IO)
    }
}


class UpsertInventoryItem(
    private val remoteItemDAO: RemoteItemDAO = DataModule.Injector.remoteDatabase.remoteItemDAO,
    private val localItemDAO: LocalItemDAO = DataModule.Injector.localDatabase.localItemDAO(),
) : UseCaseScope {
    fun execute(
        item: InventoryItem
    ) {
        launch {
            val remoteItem = remoteItemDAO.upsertInventoryItem(item)
            localItemDAO.insertAll(remoteItem.toEntity())
        }
    }
}

class DeleteInventoryItem(
    private val database: InMemoryDB = InMemoryDB,
) {
    fun execute(
        itemId: String,
    ) {
        database.deleteInventoryItem(itemId)
    }
}