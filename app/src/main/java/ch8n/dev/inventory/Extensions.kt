package ch8n.dev.inventory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

