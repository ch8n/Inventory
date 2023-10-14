package ch8n.dev.inventory.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import ch8n.dev.inventory.ComposeStable
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.usecase.CreateInventoryCategory
import ch8n.dev.inventory.data.usecase.CreateInventoryItem
import ch8n.dev.inventory.data.usecase.CreateInventorySuppliers
import ch8n.dev.inventory.data.usecase.DeleteInventoryCategory
import ch8n.dev.inventory.data.usecase.DeleteInventoryItem
import ch8n.dev.inventory.data.usecase.GetInventoryCategory
import ch8n.dev.inventory.data.usecase.GetInventoryItem
import ch8n.dev.inventory.data.usecase.GetInventorySupplier
import ch8n.dev.inventory.data.usecase.UpdateInventoryCategory
import ch8n.dev.inventory.data.usecase.UpdateInventoryItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip

val LocalAppStore = compositionLocalOf<AppStore> { error("AppStore not created!") }


@Composable
fun WithAppStore(content: @Composable () -> Unit) {
    val appStore = remember { AppStore() }
    CompositionLocalProvider(LocalAppStore provides appStore) {
        content.invoke()
    }
}

class AppStore(
    val getCategory: GetInventoryCategory = GetInventoryCategory(),
    val getSupplier: GetInventorySupplier = GetInventorySupplier(),
    val getItems: GetInventoryItem = GetInventoryItem(),
    val createCategory: CreateInventoryCategory = CreateInventoryCategory(),
    val createSuppliers: CreateInventorySuppliers = CreateInventorySuppliers(),
    val updateCategory: UpdateInventoryCategory = UpdateInventoryCategory(),
    val deleteCategory: DeleteInventoryCategory = DeleteInventoryCategory(),
    val createItem: CreateInventoryItem = CreateInventoryItem(),
    val updateItem: UpdateInventoryItem = UpdateInventoryItem(),
    val deleteItem: DeleteInventoryItem = DeleteInventoryItem(),
) {

    var query = MutableStateFlow("")
    var selectedCategory = MutableStateFlow(InventoryCategory.Empty)

    val getQueryItem = query.combine(selectedCategory) { query, category ->
        if (category.name.isEmpty()) return@combine emptyList<InventoryItem>()
        if (query.isEmpty()) {
            val items = getItems.value.firstOrNull() ?: emptyList<InventoryItem>()
            return@combine items.filter {
                it.category.name.equals(
                    category.name,
                    ignoreCase = true
                )
            }
        }
        val items = getItems.value.firstOrNull() ?: emptyList<InventoryItem>()
        items.filter {
            it.name.contains(query, ignoreCase = true) && (it.category.name.equals(
                category.name,
                ignoreCase = true
            ))
        }
    }.map {
        ComposeStable(it)
    }

}