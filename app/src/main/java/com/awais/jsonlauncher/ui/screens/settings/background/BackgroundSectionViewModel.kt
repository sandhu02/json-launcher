package com.awais.jsonlauncher.ui.screens.settings.background

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.BackgroundMode
import com.awais.jsonlauncher.models.ThemeMode
import com.awais.jsonlauncher.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BackgroundSectionUiState(
    val backgroundMode: BackgroundMode = BackgroundMode.DEFAULT,
    val isCollapsed: Boolean = false
)


@HiltViewModel
class BackgroundSectionViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(BackgroundSectionUiState())
    val uiState = _uiState.asStateFlow()

    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }

    fun onDefaultClicked() {
        viewModelScope.launch {
            settingsRepository.setBackgroundMode(BackgroundMode.DEFAULT)
        }
    }

    fun onWallpaperClicked() {
        viewModelScope.launch {
            settingsRepository.setBackgroundMode(BackgroundMode.WALLPAPER)
        }
    }

    init {
        viewModelScope.launch {
            settingsRepository.backgroundMode.collect { backgroundMode ->
                _uiState.update {
                    it.copy(backgroundMode = backgroundMode)
                }
            }
        }
    }
}