@file:OptIn(ExperimentalFoundationApi::class)

package com.ccanahuire.animatedsidebar.ui.drawer

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animate
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
@ExperimentalFoundationApi
fun FancyNavigationDrawer(
    drawerContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerState: FancyNavigationDrawerState = rememberFancyNavigationDrawerState(),
    gesturesEnabled: Boolean = true,
    drawerBackgroundBrush: Brush = SolidColor(MaterialTheme.colorScheme.secondary),
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val minValue = -with(density) { MaximumSideBarWidth.toPx() }
    val maxValue = 0f

    SideEffect {
        drawerState.density = density
        drawerState.anchoredDraggableState.updateAnchors(
            DraggableAnchors {
                FancyDrawerValue.Closed at minValue
                FancyDrawerValue.Open at maxValue
            }
        )
    }

    Box(
        modifier = modifier
            .anchoredDraggable(
                drawerState.anchoredDraggableState, Orientation.Horizontal, gesturesEnabled
            )
            .background(drawerBackgroundBrush)
    ) {
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (drawerState.anchoredDraggableState.offset - minValue).roundToInt(), 0
                    )
                }
                .graphicsLayer {
                    val fraction = calculateFraction(
                        minValue, maxValue, drawerState.anchoredDraggableState.offset
                    )

                    rotationY = -fraction * 8f
                }
                .clip(
                    RoundedCornerShape(
                        if (drawerState.anchoredDraggableState.offset.isNaN()) {
                            0.0
                        } else {
                            calculateFraction(
                                minValue,
                                maxValue,
                                drawerState.anchoredDraggableState.offset) * 32.0
                        }.dp
                    )
                )
        ) {
            content()
        }

        CloseableTransparentOverlay(
            open = drawerState.anchoredDraggableState.currentValue == FancyDrawerValue.Open,
            onClose = {
                scope.launch {
                    drawerState.close()
                }
            }
        )

        Box(
            modifier = Modifier
                .width(MaximumSideBarWidth)
                .offset {
                    IntOffset(
                        drawerState.anchoredDraggableState.offset.roundToInt(), 0
                    )
                }
        ) {
            drawerContent()
        }
    }
}

@Composable
fun rememberFancyNavigationDrawerState(
    initialValue: FancyDrawerValue = FancyDrawerValue.Closed,
    confirmStateChange: (FancyDrawerValue) -> Boolean = { true }
): FancyNavigationDrawerState {
    return remember {
        FancyNavigationDrawerState(initialValue, confirmStateChange)
    }
}

private fun calculateFraction(a: Float, b: Float, pos: Float) =
    ((pos - a) / (b - a)).coerceIn(0f, 1f)

@Stable
@ExperimentalFoundationApi
class FancyNavigationDrawerState(
    initialValue: FancyDrawerValue = FancyDrawerValue.Closed,
    confirmStateChange: (FancyDrawerValue) -> Boolean = { true },
) {
    val anchoredDraggableState = AnchoredDraggableState(
        initialValue = initialValue,
        positionalThreshold = { distance -> distance * DrawerPositionalThreshold },
        velocityThreshold = { with(requireDensity()) { DrawerVelocityThreshold.toPx() } },
        animationSpec = AnimationSpec,
        confirmValueChange = confirmStateChange
    )

    internal var density: Density? by mutableStateOf(null)
    private fun requireDensity(): Density = requireNotNull(density) {
        "Density on FancyNavigationDrawerState was not provided, please make sure " +
                "to call this state inside a FancyNavigationDrawer"
    }

    private val currentOffset: Float get() = anchoredDraggableState.offset
    fun requireOffset(): Float = anchoredDraggableState.requireOffset()

    suspend fun open() {
        animateTo(FancyDrawerValue.Open)
    }

    suspend fun close() {
        animateTo(FancyDrawerValue.Closed)
    }

    private suspend fun animateTo(
        targetValue: FancyDrawerValue,
        animationSpec: AnimationSpec<Float> = AnimationSpec,
        velocity: Float = anchoredDraggableState.lastVelocity
    ) {
        anchoredDraggableState.anchoredDrag(targetValue = targetValue) { anchors, latestTarget ->
            val targetOffset = anchors.positionOf(latestTarget)
            if (!targetOffset.isNaN()) {
                var prev = if (currentOffset.isNaN()) 0f else currentOffset
                animate(prev, targetOffset, velocity, animationSpec) { value, velocity ->
                    // Our onDrag coerces the value within the bounds, but an animation may
                    // overshoot, for example a spring animation or an overshooting interpolator
                    // We respect the user's intention and allow the overshoot, but still use
                    // DraggableState's drag for its mutex.
                    dragTo(value, velocity)
                    prev = value
                }
            }
        }
    }
}

enum class FancyDrawerValue {
    Closed,
    Open
}

@Composable
private fun CloseableTransparentOverlay(
    open: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dismissModifier = if (open) {
        Modifier.pointerInput(onClose) {
            detectTapGestures {
                onClose()
            }
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(dismissModifier)
    )
}


private const val DrawerPositionalThreshold = 0.5f
private val DrawerVelocityThreshold = 400.dp

private val AnimationSpec = TweenSpec<Float>(250)
private val MaximumSideBarWidth = 250.dp