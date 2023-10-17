package ch8n.dev.inventory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import ch8n.dev.inventory.data.usecase.CreateInventoryCategory
import ch8n.dev.inventory.data.usecase.CreateInventorySuppliers
import ch8n.dev.inventory.data.usecase.CreateOrder
import ch8n.dev.inventory.data.usecase.DeleteInventoryCategory
import ch8n.dev.inventory.data.usecase.DeleteInventoryItem
import ch8n.dev.inventory.data.usecase.DeleteInventorySupplier
import ch8n.dev.inventory.data.usecase.GetInventoryCategory
import ch8n.dev.inventory.data.usecase.GetInventoryItem
import ch8n.dev.inventory.data.usecase.GetInventorySupplier
import ch8n.dev.inventory.data.usecase.GetOrders
import ch8n.dev.inventory.data.usecase.UploadItemImageToServer
import ch8n.dev.inventory.data.usecase.UpsertInventoryItem

val LocalUseCaseProvider = compositionLocalOf<UserCaseProvider> { error("AppStore not created!") }

@Composable
fun WithUseCaseProvider(content: @Composable () -> Unit) {
    val userCaseProvider = remember { UserCaseProvider() }
    CompositionLocalProvider(LocalUseCaseProvider provides userCaseProvider) {
        content.invoke()
    }
}

class UserCaseProvider(
    //val observeRemoteSuppliersChange: ObserveRemoteInventorySuppliersChange = ObserveRemoteInventorySuppliersChange(),
    val getSupplier: GetInventorySupplier = GetInventorySupplier(),
    val deleteSupplier: DeleteInventorySupplier = DeleteInventorySupplier(),
    val getCategory: GetInventoryCategory = GetInventoryCategory(),
    val createCategory: CreateInventoryCategory = CreateInventoryCategory(),
    val deleteCategory: DeleteInventoryCategory = DeleteInventoryCategory(),
    val uploadImage: UploadItemImageToServer = UploadItemImageToServer(),

    val getItems: GetInventoryItem = GetInventoryItem(),
    val getOrders: GetOrders = GetOrders(),
    val createSuppliers: CreateInventorySuppliers = CreateInventorySuppliers(),
    val upsertItem: UpsertInventoryItem = UpsertInventoryItem(),
    val createOrder: CreateOrder = CreateOrder(),
    val updateOrder: CreateOrder = CreateOrder(),
    val deleteItem: DeleteInventoryItem = DeleteInventoryItem(),
)