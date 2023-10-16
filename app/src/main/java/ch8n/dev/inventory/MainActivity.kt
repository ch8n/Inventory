package ch8n.dev.inventory

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.ui.LocalNavigator
import ch8n.dev.inventory.ui.WithAppStore
import ch8n.dev.inventory.ui.WithNavigator
import ch8n.dev.inventory.ui.screens.ManageCategoryScreen
import ch8n.dev.inventory.ui.screens.HomeScreen
import ch8n.dev.inventory.ui.screens.ManageItemScreen
import ch8n.dev.inventory.ui.screens.ManageSupplierScreen
import ch8n.dev.inventory.ui.screens.CreateOrderScreen
import ch8n.dev.inventory.ui.screens.ImagePreviewScreen
import ch8n.dev.inventory.ui.screens.ManageOrderScreen
import ch8n.dev.inventory.ui.screens.UpdateOrderScreen
import ch8n.dev.inventory.ui.theme.InventoryTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InventoryTheme {
                WithNavigator {
                    WithAppStore {
                        val navigator = LocalNavigator.current

                        val currentDestination by navigator
                            .currentDestination
                            .collectAsState(initial = Destinations.HomeScreen)

                        when (currentDestination) {
                            is Destinations.CreateCategoryScreen -> ManageCategoryScreen()
                            is Destinations.HomeScreen, null -> HomeScreen()
                            is Destinations.ManageItemScreen -> ManageItemScreen()
                            is Destinations.CreateOrderScreen -> CreateOrderScreen()
                            is Destinations.ManageSupplierScreen -> ManageSupplierScreen()
                            is Destinations.ManageOrdersScreen -> ManageOrderScreen()
                            is Destinations.UpdateOrdersScreen -> UpdateOrderScreen(order = (currentDestination as Destinations.UpdateOrdersScreen).order)
                            is Destinations.ImagePreviewScreen -> ImagePreviewScreen(uri = (currentDestination as Destinations.ImagePreviewScreen).uri)
                        }
                    }
                }
            }
        }
    }


}

sealed class Destinations {
    object HomeScreen : Destinations()
    object CreateCategoryScreen : Destinations()
    object ManageSupplierScreen : Destinations()
    object ManageItemScreen : Destinations()
    object CreateOrderScreen : Destinations()
    object ManageOrdersScreen : Destinations()
    data class UpdateOrdersScreen(val order: Order) : Destinations()
    data class ImagePreviewScreen(val uri: Uri) : Destinations()
}
