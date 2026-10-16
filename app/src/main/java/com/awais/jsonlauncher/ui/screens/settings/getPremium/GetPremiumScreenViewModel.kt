package com.awais.jsonlauncher.ui.screens.settings.getPremium

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class GetPremiumScreenUiState(
    val isCollapsed: Boolean = false
)

@HiltViewModel
class GetPremiumScreenViewModel @Inject constructor(

): ViewModel() {
    private val _uiState = MutableStateFlow(GetPremiumScreenUiState())
    val uiState = _uiState.asStateFlow()



    fun onCollapseClick() {
        _uiState.update { it.copy(isCollapsed = !uiState.value.isCollapsed) }
    }


}