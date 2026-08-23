package com.awais.jsonlauncher.ui.screens.home

import androidx.lifecycle.ViewModel
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.repositories.AppsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomeScreenUiState(
    val apps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: AppsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeScreenUiState())
    val uiState = _uiState.asStateFlow()

}