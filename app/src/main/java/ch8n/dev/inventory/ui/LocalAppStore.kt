package ch8n.dev.inventory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import ch8n.dev.inventory.data.usecase.CreateInventoryCategory
import ch8n.dev.inventory.data.usecase.UpsertInventoryItem
import ch8n.dev.inventory.data.usecase.CreateInventorySuppliers
import ch8n.dev.inventory.data.usecase.CreateOrder
import ch8n.dev.inventory.data.usecase.DeleteInventoryCategory
import ch8n.dev.inventory.data.usecase.DeleteInventoryItem
import ch8n.dev.inventory.data.usecase.DeleteInventorySupplier
import ch8n.dev.inventory.data.usecase.GetInventoryCategory
import ch8n.dev.inventory.data.usecase.GetInventoryItem
import ch8n.dev.inventory.data.usecase.GetInventorySupplier
import ch8n.dev.inventory.data.usecase.GetOrders
import ch8n.dev.inventory.data.usecase.UpdateInventoryCategory

val LocalAppStore = compositionLocalOf<AppStore> { error("AppStore not created!") }


@Composable
fun WithAppStore(content: @Composable () -> Unit) {
    val appStore = remember { AppStore() }
    CompositionLocalProvider(LocalAppStore provides appStore) {
        content.invoke()
    }
}

class AppStore(
    val getSupplier: GetInventorySupplier = GetInventorySupplier(),
    val deleteSupplier: DeleteInventorySupplier = DeleteInventorySupplier(),
    val getCategory: GetInventoryCategory = GetInventoryCategory(),
    val getItems: GetInventoryItem = GetInventoryItem(),
    val getOrders: GetOrders = GetOrders(),
    val createCategory: CreateInventoryCategory = CreateInventoryCategory(),
    val createSuppliers: CreateInventorySuppliers = CreateInventorySuppliers(),
    val updateCategory: UpdateInventoryCategory = UpdateInventoryCategory(),
    val deleteCategory: DeleteInventoryCategory = DeleteInventoryCategory(),
    val upsertItem: UpsertInventoryItem = UpsertInventoryItem(),
    val createOrder: CreateOrder = CreateOrder(),
    val updateOrder: CreateOrder = CreateOrder(),
    val deleteItem: DeleteInventoryItem = DeleteInventoryItem(),
)