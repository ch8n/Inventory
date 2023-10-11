package ch8n.dev.inventory.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch8n.dev.inventory.Destinations
import ch8n.dev.inventory.data.domain.CategoryAttribute
import ch8n.dev.inventory.data.usecase.CreateInventoryCategory
import ch8n.dev.inventory.data.usecase.CreateInventoryItem
import ch8n.dev.inventory.data.usecase.DeleteInventoryCategory
import ch8n.dev.inventory.data.usecase.DeleteInventoryItem
import ch8n.dev.inventory.data.usecase.UpdateInventoryCategory
import ch8n.dev.inventory.data.usecase.UpdateInventoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

val LocalAppStore = compositionLocalOf<AppStore> { error("AppStore not created!") }
val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator not created!") }

@Composable
fun WithAppStore(content: @Composable () -> Unit) {
    val appStore = remember { AppStore() }
    CompositionLocalProvider(LocalAppStore provides appStore) {
        content.invoke()
    }
}

@Composable
fun WithNavigator(
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val navigator = remember { Navigator() }
    CompositionLocalProvider(LocalNavigator provides navigator) {
        content.invoke()
    }

    BackHandler {
        navigator.back { canGoBack ->
            if (!canGoBack) {
                (context as ComponentActivity).finishAndRemoveTask()
            }
        }
    }
}


class Navigator {

    private val _backStack = MutableStateFlow<List<Destinations>>(emptyList())
    val backstack = _backStack.asStateFlow()

    val currentDestination = backstack.map { it.lastOrNull() }

    fun goto(destination: Destinations) {
        _backStack.update { current -> current + destination }
    }

    fun back(onGoBack: (canGoBack: Boolean) -> Unit) {
        val current = _backStack.value
        _backStack.update { current -> current.dropLast(1) }
        val updated = _backStack.value
        onGoBack.invoke(current != updated)
    }

    fun backTill(destination: Destinations) {
        _backStack.update { current ->
            val index = current.indexOfFirst { it == destination }
            current.take(index)
        }
    }
}


class AppStore(
    val createCategory: CreateInventoryCategory = CreateInventoryCategory(),
    val updateCategory: UpdateInventoryCategory = UpdateInventoryCategory(),
    val deleteCategory: DeleteInventoryCategory = DeleteInventoryCategory(),
    val createItem: CreateInventoryItem = CreateInventoryItem(),
    val updateItem: UpdateInventoryItem = UpdateInventoryItem(),
    val deleteItem: DeleteInventoryItem = DeleteInventoryItem(),
) {

    fun createCategory(name: String, attribute: List<CategoryAttribute>) {
        createCategory.execute(name, attribute)
    }

}
