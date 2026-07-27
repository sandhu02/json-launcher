package com.awais.jsonlauncher.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.awais.jsonlauncher.models.AppInfo
import com.awais.jsonlauncher.repositories.AppsRepository
import com.awais.jsonlauncher.ui.home.apps.AppsSectionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

//data class LauncherSettingsUiState(
//
//)

@HiltViewModel
class LauncherSettingsViewModel @Inject constructor(
    private val repository: AppsRepository
) : ViewModel() {

}