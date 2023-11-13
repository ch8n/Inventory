package ch8n.dev.inventory

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ch8n.dev.inventory.data.DataModule
import ch8n.dev.inventory.data.domain.InventoryCategory
import ch8n.dev.inventory.data.domain.InventoryItem
import ch8n.dev.inventory.data.domain.InventorySupplier
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.data.domain.OrderStatus
import ch8n.dev.inventory.ui.LocalNavigator
import ch8n.dev.inventory.ui.LocalUseCaseProvider
import ch8n.dev.inventory.ui.WithUseCaseProvider
import ch8n.dev.inventory.ui.WithNavigator
import ch8n.dev.inventory.ui.screens.CreateOrderContent
import ch8n.dev.inventory.ui.screens.HomeContent
import ch8n.dev.inventory.ui.screens.ImagePreviewContent
import ch8n.dev.inventory.ui.screens.ManageCategoryContent
import ch8n.dev.inventory.ui.screens.ManageItemContent
import ch8n.dev.inventory.ui.screens.ManageOrderContent
import ch8n.dev.inventory.ui.screens.ManageSupplierContent
import ch8n.dev.inventory.ui.screens.UpdateOrderContent
import ch8n.dev.inventory.ui.theme.InventoryTheme
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataModule.Injector.provideAppContext(applicationContext)

        setContent {
            InventoryTheme {
                WithNavigator {
                    WithUseCaseProvider {

                        val userCaseProvider = LocalUseCaseProvider.current

                        LaunchedEffect(Unit) {
                            userCaseProvider.getSupplier.invalidate()
                            userCaseProvider.getCategory.invalidate()
                            userCaseProvider.getItems.invalidate()
                            userCaseProvider.getOrders.invalidate()
                        }

                        val navigator = LocalNavigator.current

                        val currentDestination by navigator
                            .currentDestination
                            .collectAsState(initial = HomeScreen)

                        if (currentDestination == null) {
                            finishAndRemoveTask()
                        } else {
                            currentDestination?.Content()
                        }
                    }
                }
            }
        }
    }


}


sealed class Screen {
    @Composable
    abstract fun Content()
}

object HomeScreen : Screen() {
    @Composable
    override fun Content() {
        HomeContent()
    }
}

object ManageCategoryScreen : Screen() {
    @Composable
    override fun Content() {
        ManageCategoryContent()
    }
}

object ManageSupplierScreen : Screen() {


    @Composable
    override fun Content() {

        ManageSupplierContent(

        )
    }
}

class ManageItemScreen : Screen() {

    private val selectedItem = MutableStateFlow(InventoryItem.New)
    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow(InventoryCategory.Empty)
    private val selectedSupplier = MutableStateFlow(InventorySupplier.Empty)
    private val scrollPosition = MutableStateFlow(0)

    @Composable
    override fun Content() {
        val selectedItem by selectedItem.collectAsState()
        val searchQuery by searchQuery.collectAsState()
        val selectedCategory by selectedCategory.collectAsState()
        val selectedSupplier by selectedSupplier.collectAsState()
        val scrollPosition by scrollPosition.collectAsState()

        ManageItemContent(
            selectedItem = selectedItem,
            onUpdateSelectedItem = { updated ->
                this.selectedItem.tryEmit(updated)
            },
            searchQuery = searchQuery,
            updateSearchQuery = {
                this.searchQuery.tryEmit(it)
            },
            selectedCategory = selectedCategory,
            updateSelectedCategory = {
                this.selectedCategory.tryEmit(it)
            },
            initialScrollPosition = scrollPosition,
            onScrollPositionChanged = {
                this.scrollPosition.tryEmit(it)
            },
            selectedSupplier = selectedSupplier,
            updateSelectedSupplier = {
                this.selectedSupplier.tryEmit(it)
            }
        )
    }
}

class CreateOrderScreen() : Screen() {

    private val selectedOrderStatus = MutableStateFlow(OrderStatus.NEW_ORDER)
    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow(InventoryCategory.Empty)
    private val selectedSupplier = MutableStateFlow(InventorySupplier.Empty)
    private val scrollPosition = MutableStateFlow(0)
    private val clientName = MutableStateFlow("")
    private val clientContact = MutableStateFlow("")
    private val orderComment = MutableStateFlow("")
    private val shortlistedItem = MutableStateFlow(mapOf<String, Int>())

    @Composable
    override fun Content() {

        val selectedOrderStatus by selectedOrderStatus.collectAsState()
        val shortlistedItem by shortlistedItem.collectAsState()

        val searchQuery by searchQuery.collectAsState()
        val selectedCategory by selectedCategory.collectAsState()
        val selectedSupplier by selectedSupplier.collectAsState()
        val scrollPosition by scrollPosition.collectAsState()
        val clientName by clientName.collectAsState()
        val clientContact by clientContact.collectAsState()
        val orderComment by orderComment.collectAsState()

        CreateOrderContent(
            shortlistedItem = shortlistedItem,
            updateShortListedItem = {
                this.shortlistedItem.tryEmit(it)
            },
            selectedOrderStatus = selectedOrderStatus,
            onUpdateOrderStatus = {
                this.selectedOrderStatus.tryEmit(it)
            },
            clientName = clientName,
            updateClientName = {
                this.clientName.tryEmit(it)
            },
            clientContact = clientContact,
            updateClientContact = {
                this.clientContact.tryEmit(it)
            },
            orderComment = orderComment,
            updateOrderComment = {
                this.orderComment.tryEmit(it)
            },
            searchQuery = searchQuery,
            updateSearchQuery = {
                this.searchQuery.tryEmit(it)
            },
            selectedCategory = selectedCategory,
            selectedSupplier = selectedSupplier,
            updateSelectedCategory = {
                this.selectedCategory.tryEmit(it)
            },
            updateSelectedSupplier = {
                this.selectedSupplier.tryEmit(it)
            },
            initialScrollPosition = scrollPosition,
            onScrollPositionChanged = {
                this.scrollPosition.tryEmit(it)
            },
        )
    }

}

object ManageOrdersScreen : Screen() {
    @Composable
    override fun Content() {
        ManageOrderContent()
    }

}

data class UpdateOrderScreen(val order: Order) : Screen() {
    @Composable
    override fun Content() {
        UpdateOrderContent(order)
    }
}

data class ImagePreviewScreen(val uri: Uri) : Screen() {
    @Composable
    override fun Content() {
        ImagePreviewContent(uri)
    }
}
