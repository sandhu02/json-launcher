package com.awais.jsonlauncher.ui.home.apps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.repositories.AppsRepository
import com.awais.jsonlauncher.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AppsSectionUiState(
    val isCollapsed: Boolean = false,
    val isAppCollapsed: List<Boolean> = emptyList(),
    val apps: List<AppInfo> = emptyList(),
    val pinnedApps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AppsSectionViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppsSectionUiState())
    val uiState = _uiState.asStateFlow()

    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }

    fun onAppCollapseClick(packageName: String) {
        _uiState.update { state ->
            state.copy(
                pinnedApps = state.pinnedApps.map { app ->
                    if (app.packageName == packageName)
                        app.copy(isCollapsed = !app.isCollapsed)
                    else
                        app
                }
            )
        }
    }

    private fun loadApps() {
        viewModelScope.launch {
            combine(
                settingsRepository.pinnedApps,
                appsRepository.apps
            ) { pinnedPackages, apps ->
                apps to pinnedPackages
            }.collect { (apps, pinnedPackages) ->

                val pinned = pinnedPackages.mapNotNull { packageName ->
                    apps.find { it.packageName == packageName }
                }

                _uiState.update {
                    it.copy(
                        apps = apps,
                        pinnedApps = pinned,
                        isLoading = false
                    )
                }
            }
        }

        appsRepository.refreshApps()
    }


    fun launchShortcut(packageName: String, shortcutId: String) {
        viewModelScope.launch {
            appsRepository.launchShortcut(packageName, shortcutId)
        }
    }

    init {
        loadApps()
    }
}
