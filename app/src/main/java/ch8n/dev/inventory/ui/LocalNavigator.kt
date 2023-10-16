package ch8n.dev.inventory.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import ch8n.dev.inventory.HomeScreen
import ch8n.dev.inventory.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator not created!") }

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

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backstack = _backStack.asStateFlow()

    val currentDestination = backstack.map { it.lastOrNull() }

    fun goto(destination: Screen) {
        _backStack.update { current -> current + destination }
    }

    fun back(onGoBack: (canGoBack: Boolean) -> Unit = {}) {
        val current = _backStack.value
        _backStack.update { current -> current.dropLast(1) }
        val updated = _backStack.value
        onGoBack.invoke(current != updated)
    }

    fun backTill(destination: Screen) {
        _backStack.update { current ->
            val index = current.indexOfFirst { it == destination }
            current.take(index)
        }
    }
}
