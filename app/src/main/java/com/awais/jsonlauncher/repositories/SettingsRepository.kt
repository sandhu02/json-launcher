package com.awais.jsonlauncher.repositories

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
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
        val PINNED_APPS = stringSetPreferencesKey("pinned_apps")
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
}