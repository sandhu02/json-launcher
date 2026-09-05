package com.awais.jsonlauncher.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.awais.jsonlauncher.models.BackgroundMode
import com.awais.jsonlauncher.models.ThemeMode
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private const val SETTINGS_NAME = "launcher_settings"

val Context.dataStore by preferencesDataStore(
    name = SETTINGS_NAME
)

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private val PINNED_APPS = stringSetPreferencesKey("pinned_apps")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val BACKGROUND_MODE = stringPreferencesKey("background_mode")
    }

    val pinnedApps: Flow<List<String>> =
        context.dataStore.data.map { preferences ->
            preferences[PINNED_APPS]?.toList() ?: defaultPinnedApps()
        }

    suspend fun savePinnedApps(packages: List<String>) {
        context.dataStore.edit { preferences ->
            preferences[PINNED_APPS] = packages.toSet()
        }
    }

    private fun defaultPinnedApps() = listOf(
        "com.google.android.dialer",
        "com.google.android.apps.messaging",
        "com.whatsapp",
        "com.google.android.GoogleCamera",
        "com.google.android.youtube"
    )

    // Theme
    val themeMode: Flow<ThemeMode> =
        context.dataStore.data.map { preferences ->
            when (preferences[THEME_MODE]) {
                "light" -> ThemeMode.LIGHT
                "dark" -> ThemeMode.DARK
                else -> ThemeMode.SYSTEM
            }
        }

    val backgroundMode: Flow<BackgroundMode> =
        context.dataStore.data.map { preferences ->
            when (preferences[BACKGROUND_MODE]) {
                "default" -> BackgroundMode.DEFAULT
                "wallpaper" -> BackgroundMode.WALLPAPER
                else -> BackgroundMode.DEFAULT
            }
        }

    suspend fun setThemeMode(themeMode: ThemeMode) {
        context.dataStore.edit { preferences ->
            preferences[THEME_MODE] = when (themeMode) {
                ThemeMode.LIGHT -> "light"
                ThemeMode.DARK -> "dark"
                ThemeMode.SYSTEM -> "system"
            }
        }
    }

    suspend fun setBackgroundMode(backgroundMode: BackgroundMode) {
        context.dataStore.edit { preferences ->
            preferences[BACKGROUND_MODE] = when (backgroundMode) {
                BackgroundMode.DEFAULT -> "default"
                BackgroundMode.WALLPAPER -> "wallpaper"
            }
        }
    }


}