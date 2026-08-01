package com.awais.jsonlauncher.ui.settings.homeApps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.repositories.AppsRepository
import com.awais.jsonlauncher.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


data class HomeAppsUiState(
    val isCollapsed: Boolean = false,
    val isAddCollapsed: Boolean = true,
    val apps: List<AppInfo> = emptyList(),
    val pinnedApps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = false
)


@HiltViewModel
class HomeAppsViewModel @Inject constructor(
    private val appsRepository: AppsRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeAppsUiState())
    val uiState = _uiState.asStateFlow()

    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }

    fun onAddCollapseClick() {
        _uiState.update { it.copy(isAddCollapsed = !uiState.value.isAddCollapsed) }
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

    fun addToHomeApps(packageName : String) {
        viewModelScope.launch {

            val pinned = settingsRepository.pinnedApps.first().toMutableList()

            if (packageName !in pinned) {
                pinned.add(packageName)
                settingsRepository.savePinnedApps(pinned)
            }
        }
    }

    fun removeFromHomeApps(packageName: String) {
        viewModelScope.launch {

            val pinned = settingsRepository.pinnedApps.first().toMutableList()

            pinned.remove(packageName)
            settingsRepository.savePinnedApps(pinned)
        }
    }

    init {
        loadApps()
    }

}