package ch8n.dev.inventory.data.usecase

import ch8n.dev.inventory.UseCaseScope
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.database.firestore.InventoryCategoryFS
import ch8n.dev.inventory.data.database.firestore.RemoteCategoryDAO
import ch8n.dev.inventory.data.database.roomdb.InventoryCategoryEntity
import ch8n.dev.inventory.data.database.roomdb.LocalCategoryDAO
import ch8n.dev.inventory.data.domain.InventoryCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


fun InventoryCategoryFS.toEntity(): InventoryCategoryEntity {
    return InventoryCategoryEntity(
        uid = documentReferenceId,
        categoryName = categoryName,
        itemSize = itemSize
    )
}

fun InventoryCategory.toEntity(): InventoryCategoryEntity {
    return InventoryCategoryEntity(
        uid = id,
        categoryName = name,
        itemSize = sizes
    )
}

fun InventoryCategoryEntity.toView(): InventoryCategory {
    return InventoryCategory(
        id = uid,
        name = categoryName,
        sizes = itemSize
    )
}

class GetInventoryCategory(
    private val remoteCategoryDAO: RemoteCategoryDAO = DataModule.Injector.remoteDatabase.remoteCategoryDAO,
    private val localCategoryDAO: LocalCategoryDAO = DataModule.Injector.localDatabase.localCategoryDAO(),
) : UseCaseScope {

    val local = localCategoryDAO.getAll().map { categories ->
        categories.map { it.toView() }
    }.flowOn(Dispatchers.IO)

    fun invalidate() {
        launch {
            val remoteCategories = remoteCategoryDAO.getAllCategory()
            localCategoryDAO.insertAll(*remoteCategories.map { it.toEntity() }.toTypedArray())
        }
    }
}

class CreateInventoryCategory(
    private val remoteCategoryDAO: RemoteCategoryDAO = DataModule.Injector.remoteDatabase.remoteCategoryDAO,
    private val localCategoryDAO: LocalCategoryDAO = DataModule.Injector.localDatabase.localCategoryDAO(),
) : UseCaseScope {
    fun execute(
        category: InventoryCategory
    ) {
        launch {
            val remoteCategory = remoteCategoryDAO.createCategory(
                categoryName = category.name,
                sizes = category.sizes
            )
            localCategoryDAO.insertAll(remoteCategory.toEntity())
        }
    }
}

class DeleteInventoryCategory(
    private val remoteCategoryDAO: RemoteCategoryDAO = DataModule.Injector.remoteDatabase.remoteCategoryDAO,
    private val localCategoryDAO: LocalCategoryDAO = DataModule.Injector.localDatabase.localCategoryDAO(),
) : UseCaseScope {
    fun execute(
        category: InventoryCategory,
    ) {
        launch {
            remoteCategoryDAO.deleteCategory(category.id)
            localCategoryDAO.delete(category.toEntity())
        }
    }
}