package ch8n.dev.inventory

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Composable
inline fun <T> rememberMutableState(init: T): MutableState<T> {
    return remember { mutableStateOf(init) }
}

@Composable
inline fun <T> rememberDerivedStateOf(noinline calculation: () -> T): State<T> {
    return remember { derivedStateOf(calculation = calculation) }
}


@Stable
@JvmInline
value class ComposeStable<T>(val value: T) {
    operator fun component1(): T = value
}

@Immutable
@JvmInline
value class ComposeImmutable<T>(val value: T) {
    operator fun component1(): T = value
}


interface UseCaseScope : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + SupervisorJob()

}

val referenceWidth = 420.dp

@Stable
val Float.ssp: TextUnit
    @Composable get() {
        val referenceWidth = referenceWidth // Baseline width in dp
        val config = LocalConfiguration.current
        val scaleFactor = config.screenWidthDp / referenceWidth.value
        return (this * scaleFactor).sp
    }

@Stable
val Float.sdp: Dp
    @Composable get() {
        val referenceWidth = referenceWidth // Baseline width in dp
        val config = LocalConfiguration.current
        val scaleFactor = config.screenWidthDp / referenceWidth.value
        return (this * scaleFactor).dp
    }

@Stable
val Int.sdp: Dp
    @Composable get() = toFloat().sdp

@Stable
val Int.ssp: TextUnit
    @Composable get() = toFloat().ssp

