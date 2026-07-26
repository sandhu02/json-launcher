package com.awais.jsonlauncher.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.awais.jsonlauncher.ui.home.apps.AppsSection
import com.awais.jsonlauncher.ui.home.notification.NotificationsSection
import com.awais.jsonlauncher.ui.home.system.SystemSection
import com.awais.jsonlauncher.ui.theme.JsonSpacing
import com.awais.jsonlauncher.ui.theme.JsonSyntax

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    val verticalScrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(verticalScrollState)
    ) {
        Text("{", color = JsonSyntax.parenthesis)
        SystemSection(navController = navController)
        NotificationsSection()
        AppsSection()
        Text("}", color = JsonSyntax.parenthesis)

        Spacer(
            modifier = Modifier.height(JsonSpacing.LG)
        )

        Text("/* Swipe Left for App Drawer */" , color = JsonSyntax.comment)

        Spacer(
            modifier = Modifier.height(JsonSpacing.LG)
        )
    }
}

