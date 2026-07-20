package com.awais.jsonlauncher.ui.home

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.repositories.AppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class HomeScreenUiState(
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true
)

@RequiresApi(Build.VERSION_CODES.BAKLAVA)
class HomeScreenViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository = AppsRepository(application)

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadApps()
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
}