package com.awais.jsonlauncher.ui.screens.settings.defaultLauncher

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


data class DefaultLauncherSectionUiState(
    val isCollapsed: Boolean = false
)

@HiltViewModel
class DefaultLauncherSectionViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow(DefaultLauncherSectionUiState())
    val uiState = _uiState.asStateFlow()



    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }



}