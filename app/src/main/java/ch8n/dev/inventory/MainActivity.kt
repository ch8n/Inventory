package ch8n.dev.inventory

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import ch8n.dev.inventory.data.domain.Order
import ch8n.dev.inventory.ui.LocalNavigator
import ch8n.dev.inventory.ui.WithAppStore
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
        ManageSupplierContent()
    }
}

object ManageItemScreen : Screen() {
    @Composable
    override fun Content() {
        ManageItemContent()
    }
}

object CreateOrderScreen : Screen() {
    @Composable
    override fun Content() {
        CreateOrderContent()
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
