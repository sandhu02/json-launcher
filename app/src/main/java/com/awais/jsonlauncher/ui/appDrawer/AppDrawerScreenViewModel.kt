package com.awais.jsonlauncher.ui.appDrawer

import androidx.compose.runtime.remember
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

data class AppDrawerScreenUiState(
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true,
    val searchQuery: String = ""
)

@HiltViewModel
class AppDrawerScreenViewModel @Inject constructor(
    private val repository: AppsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppDrawerScreenUiState())
    val uiState = _uiState.asStateFlow()

    fun onSearchQueryChange(query: String) {
        _uiState.update {
            it.copy(
                searchQuery = query,
            )
        }
    }

    fun onCollapseClick(packageName: String) {
        _uiState.update { state ->
            state.copy(
                apps = state.apps.map { app ->
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

            val apps = repository.getInstalledApps()

            _uiState.update {
                it.copy(
                    apps = apps,
                    isLoading = false
                )
            }
        }
    }

    fun launchShortcut(packageName: String, shortcutId: String) {
        viewModelScope.launch {
            repository.launchShortcut(packageName, shortcutId)
        }
    }


    init {
        loadApps()
    }
}