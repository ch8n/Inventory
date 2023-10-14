package ch8n.dev.inventory

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ch8n.dev.inventory.ui.LocalNavigator
import ch8n.dev.inventory.ui.WithAppStore
import ch8n.dev.inventory.ui.WithNavigator
import ch8n.dev.inventory.ui.screens.CreateCategoryScreen
import ch8n.dev.inventory.ui.screens.HomeScreen
import ch8n.dev.inventory.ui.screens.CreateItemScreen
import ch8n.dev.inventory.ui.screens.CreateSupplierScreen
import ch8n.dev.inventory.ui.screens.CreateOrderScreen
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
                            Destinations.CreateCategoryScreen -> CreateCategoryScreen()
                            Destinations.HomeScreen, null -> HomeScreen()
                            Destinations.CreateItemScreen -> CreateItemScreen()
                            Destinations.CreateOrderScreen -> CreateOrderScreen()
                            Destinations.CreateSupplierScreen -> CreateSupplierScreen()
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
    object CreateSupplierScreen : Destinations()
    object CreateItemScreen : Destinations()
    object CreateOrderScreen : Destinations()
}