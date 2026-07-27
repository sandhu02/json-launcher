package com.awais.jsonlauncher.ui.settings.homeApps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.repositories.AppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val repository: AppsRepository
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

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            val apps = repository.getInstalledApps()

            val pinned = getPinnedPackages().mapNotNull { packageName ->
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

    private fun getPinnedPackages() : List<String> {
        return listOf(
            "com.google.android.dialer",
            "com.google.android.apps.messaging",
            "com.whatsapp",
            "com.google.android.GoogleCamera"
        )
    }

    init {
        loadApps()
    }
}