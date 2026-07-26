package com.awais.jsonlauncher.ui.home.apps

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

data class AppsSectionUiState(
    val isCollapsed: Boolean = false,
    val isAppCollapsed: List<Boolean> = emptyList(),
    val apps: List<AppInfo> = emptyList(),
    val pinnedApps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class AppsSectionViewModel @Inject constructor(
    private val repository: AppsRepository
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

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }

            val apps = repository.getInstalledApps()

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

    init {
        loadApps()
    }
}

private val pinnedPackages = listOf(
    "com.google.android.dialer",
    "com.google.android.apps.messaging",
    "com.whatsapp",
    "com.google.android.GoogleCamera"
)