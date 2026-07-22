package com.awais.jsonlauncher.ui.home.notification

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NotificationsSectionUiState(
    val isCollapsed: Boolean = false
)

class NotificationsSectionViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationsSectionUiState())
    val uiState = _uiState.asStateFlow()

    fun onCollapseClick() {
//        Log.d("Json Item" , "${uiState.value.isCollapsed}")
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }
}