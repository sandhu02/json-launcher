package com.awais.jsonlauncher.ui.home.apps

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
    val isLoading: Boolean = true
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

//    fun onCollapseAppClicked (index: Int) {
//        _uiState.update { it.copy(isAppCollapsed = uiState.value.isAppCollapsed[index]) }
//    }


    private fun loadApps() {

        viewModelScope.launch(Dispatchers.IO) {

            val apps = repository.getInstalledApps()

            Log.d("InstalledApp" , apps.toString())

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