package com.awais.jsonlauncher.ui.screens.appDrawer

import android.view.HapticFeedbackConstants
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun AlphabetSlider(
    modifier: Modifier = Modifier,
    onLetterSelected: (Char) -> Unit,
    onTopSelected: (() -> Unit)? = null,
) {
    var isTouching by remember { mutableStateOf(value = false) }
    var selectedLetter by remember { mutableStateOf<Char?>(null) }
    var isTopSelected by remember { mutableStateOf(value = false) }
    var touchY by remember { mutableFloatStateOf(0f) }
    var topOffsetPx by remember { mutableFloatStateOf(0f) }
    var sliderHeightPx by remember { mutableFloatStateOf(0f) }

    val coroutineScope = rememberCoroutineScope()
    var resetJob by remember { mutableStateOf<Job?>(null) }

    val alphabet = remember { ('A'..'Z').toList() }
    val density = LocalDensity.current
    val view = LocalView.current

    fun performStrongHaptic() {
        view.performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }

    val bubbleYOffset = remember(touchY, isTopSelected, selectedLetter, sliderHeightPx, topOffsetPx) {
        if (sliderHeightPx <= 0f) return@remember 0.dp
        val totalItems = alphabet.size + 1
        val itemHeight = sliderHeightPx / totalItems.toFloat()
        val rawIndex = if (isTopSelected) {
            0
        } else if (selectedLetter != null) {
            alphabet.indexOf(selectedLetter!!) + 1
        } else {
            ((touchY - topOffsetPx) / itemHeight).toInt().coerceIn(0, totalItems - 1)
        }
        val centerY = topOffsetPx + (rawIndex * itemHeight) + (itemHeight / 2f)
        val bubbleRadiusPx = with(density) { 24.dp.toPx() }
        val topClampedY = (centerY - bubbleRadiusPx).coerceIn(0f, (sliderHeightPx + (topOffsetPx * 2f) - (bubbleRadiusPx * 2f)).coerceAtLeast(0f))
        with(density) { topClampedY.toDp() }
    }

    val animatedBubbleY by animateDpAsState(
        targetValue = bubbleYOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessHigh,
        ),
        label = "bubbleYAnimation",
    )

    Box(
        modifier = modifier
            .fillMaxHeight()
            .width(40.dp)
            .pointerInput(alphabet) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    isTouching = true
                    resetJob?.cancel()

                    fun updateTouch(y: Float) {
                        touchY = y
                        val totalItems = alphabet.size + 1
                        if (sliderHeightPx > 0f) {
                            val itemHeight = sliderHeightPx / totalItems.toFloat()
                            val relativeY = y - topOffsetPx
                            val rawIndex = (relativeY / itemHeight)
                                .toInt()
                                .coerceIn(0, totalItems - 1)

                            if (rawIndex == 0) {
                                if (!isTopSelected) {
                                    isTopSelected = true
                                    selectedLetter = null
                                    performStrongHaptic()
                                    onTopSelected?.invoke()
                                }
                            } else {
                                isTopSelected = false
                                val letter = alphabet[rawIndex - 1]
                                if (letter != selectedLetter) {
                                    selectedLetter = letter
                                    performStrongHaptic()
                                    onLetterSelected(letter)
                                }
                            }
                        }
                    }

                    updateTouch(down.position.y)
                    down.consume()

                    while (true) {
                        val event = awaitPointerEvent()
                        val pointer = event.changes.firstOrNull { it.id == down.id } ?: break
                        if (!pointer.pressed) break
                        updateTouch(pointer.position.y)
                        pointer.consume()
                    }

                    resetJob = coroutineScope.launch {
                        delay(400.milliseconds)
                        isTouching = false
                        selectedLetter = null
                        isTopSelected = false
                    }
                }
            },
        contentAlignment = Alignment.CenterEnd,
    ) {
        // Floating preview bubble on the left
        AnimatedVisibility(
            visible = isTouching && ((selectedLetter != null) || isTopSelected),
            enter = fadeIn(tween(150)) + scaleIn(spring(stiffness = Spring.StiffnessMediumLow)),
            exit = fadeOut(tween(150)) + scaleOut(tween(150)),
            modifier = Modifier
                .padding(end = 40.dp)
                .align(Alignment.TopEnd)
                .offset(y = animatedBubbleY),
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                shadowElevation = 6.dp,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isTopSelected) "◉" else (selectedLetter?.toString() ?: ""),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }
            }
        }

        // Vertical Alphabet Column centered vertically
        Column(
            modifier = Modifier
                .fillMaxHeight(0.75f)
                .width(32.dp)
                .align(Alignment.CenterEnd)
                .onGloballyPositioned { coordinates ->
                    sliderHeightPx = coordinates.size.height.toFloat()
                    topOffsetPx = coordinates.positionInParent().y
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Top Item "◉"
            val isTopHighlighted = isTopSelected && isTouching
            val topScale by animateFloatAsState(
                targetValue = if (isTopHighlighted) 1.6f else 1.0f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow,
                ),
                label = "topScale",
            )
            val topColor by animateColorAsState(
                targetValue = if (isTopHighlighted) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                },
                animationSpec = tween(150),
                label = "topColor",
            )

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "◉",
                    fontSize = 16.sp,
                    color = topColor,
                    modifier = Modifier.graphicsLayer {
                        scaleX = topScale
                        scaleY = topScale
                    },
                )
            }

            // Alphabet Letters A-Z
            alphabet.forEach { letter ->
                val isLetterHighlighted = (letter == selectedLetter) && isTouching
                val scale by animateFloatAsState(
                    targetValue = if (isLetterHighlighted) 2.0f else 1.0f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow,
                    ),
                    label = "letterScale_$letter",
                )
                val color by animateColorAsState(
                    targetValue = if (isLetterHighlighted) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    },
                    animationSpec = tween(150),
                    label = "letterColor_$letter",
                )

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = letter.toString(),
                        fontSize = 14.sp,
                        fontWeight = if (isLetterHighlighted) FontWeight.Bold else FontWeight.Normal,
                        color = color,
                        modifier = Modifier.graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                        },
                    )
                }
            }
        }
    }
}