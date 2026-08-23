package com.awais.jsonlauncher.ui.screens.settings.colorTheme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.ThemeMode
import com.awais.jsonlauncher.repositories.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ColorThemeUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isCollapsed: Boolean = false
)

@HiltViewModel
class ColorThemeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ColorThemeUiState())
    val uiState = _uiState.asStateFlow()

    fun onLightClicked() {
        viewModelScope.launch {
            settingsRepository.setThemeMode(ThemeMode.LIGHT)
        }
    }

    fun onDarkClicked() {
        viewModelScope.launch {
            settingsRepository.setThemeMode(ThemeMode.DARK)
        }
    }

    fun onSystemClicked() {
        viewModelScope.launch {
            settingsRepository.setThemeMode(ThemeMode.SYSTEM)
        }
    }

    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }

    init {
        viewModelScope.launch {
            settingsRepository.themeMode.collect { themeMode ->
                _uiState.update {
                    it.copy(themeMode = themeMode)
                }
            }
        }
    }
}