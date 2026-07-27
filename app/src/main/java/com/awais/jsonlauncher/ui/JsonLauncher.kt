package com.awais.jsonlauncher.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.awais.jsonlauncher.ui.appDrawer.AppDrawerScreen
import com.awais.jsonlauncher.ui.home.HomeScreen
import com.awais.jsonlauncher.ui.settings.LauncherSettings
import kotlin.math.absoluteValue

enum class JsonLauncherAppScreens(val title : String) {
    MainScreen(title = "MainScreen"),
    SettingsScreen(title = "SettingsScreen"),
}


@Composable
fun JsonLauncher(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = JsonLauncherAppScreens.MainScreen.name,
    ){
        composable(route = JsonLauncherAppScreens.MainScreen.name) {
//

            val pagerState = rememberPagerState(
                initialPage = 0
            ) { 2 }

            HorizontalPager(
                state = pagerState,
                beyondViewportPageCount = 1,
                modifier = modifier
            ) { page ->

                val pageOffset = (
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                        ).absoluteValue

                Box(
                    modifier = Modifier.padding(4.dp)
                        .fillMaxSize().graphicsLayer {
                        alpha = 1f - pageOffset.coerceIn(0f, 1f) * 0.3f

                        val scale = 1f - pageOffset.coerceIn(0f, 1f) * 0.08f
                        scaleX = scale
                        scaleY = scale
                    }
                ) {
                    when (page) {
                        0 -> HomeScreen(navController = navController)
                        1 -> AppDrawerScreen(navController = navController)
                    }
                }
            }
        }

        composable(
            route = JsonLauncherAppScreens.SettingsScreen.name,
            enterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    AnimatedContentTransitionScope.SlideDirection.Left
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    AnimatedContentTransitionScope.SlideDirection.Right
                )
            }
        ) {
            LauncherSettings(
                modifier = modifier,
                navController = navController
            )
        }
    }


}