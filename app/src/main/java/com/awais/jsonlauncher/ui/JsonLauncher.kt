package com.awais.jsonlauncher.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.awais.jsonlauncher.ui.appDrawer.AppDrawerScreen
import com.awais.jsonlauncher.ui.home.HomeScreen
import kotlin.math.absoluteValue

@Composable
fun JsonLauncher(
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(
        initialPage = 0
    ) { 2 }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { page ->

        val pageOffset = (
                (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                ).absoluteValue

        Box(
            modifier = Modifier.graphicsLayer {
                alpha = 1f - pageOffset.coerceIn(0f, 1f) * 0.3f

                val scale = 1f - pageOffset.coerceIn(0f, 1f) * 0.08f
                scaleX = scale
                scaleY = scale
            }
        ) {
            when (page) {
                0 -> HomeScreen()
                1 -> AppDrawerScreen()
            }
        }
    }
}