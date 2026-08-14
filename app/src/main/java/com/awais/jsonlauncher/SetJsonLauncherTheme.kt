package com.awais.jsonlauncher

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.awais.jsonlauncher.models.ThemeMode
import com.awais.jsonlauncher.navigation.JsonLauncherNavHost
import com.awais.jsonlauncher.repositories.SettingsRepository
import com.awais.jsonlauncher.ui.theme.JsonLauncherTheme

@Composable
fun SetJsonLauncherTheme(
    settingsRepository: SettingsRepository,
    showNotificationDialog: Boolean,
    onDismissNotificationDialog: () -> Unit,
    showSetAsDefaultDialog: Boolean,
    onDismissSetAsDefaultDialog: () -> Unit,
    onOpenSetAsDefault: () -> Unit,

) {
    val themeMode by settingsRepository.themeMode
        .collectAsStateWithLifecycle(
            initialValue = ThemeMode.SYSTEM
        )

    JsonLauncherTheme(
        themeMode = themeMode,
    ) {
        // Json Launcher UI
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            JsonLauncherNavHost(
                modifier = Modifier.padding(innerPadding),
                showNotificationDialog = showNotificationDialog,
                onDismissNotificationDialog = onDismissNotificationDialog,
                showSetAsDefaultDialog = showSetAsDefaultDialog,
                onDismissSetAsDefaultDialog = onDismissSetAsDefaultDialog,
                onOpenSetAsDefault = onOpenSetAsDefault,
            )
        }
    }
}